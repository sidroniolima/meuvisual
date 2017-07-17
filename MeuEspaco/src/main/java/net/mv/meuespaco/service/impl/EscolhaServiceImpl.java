package net.mv.meuespaco.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.controller.EnvProperties;
import net.mv.meuespaco.controller.LoginBean;
import net.mv.meuespaco.controller.filtro.FiltroEscolha;
import net.mv.meuespaco.dao.EscolhaDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.factory.EscolhaFactory;
import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.relatorio.RelatorioEscolha;
import net.mv.meuespaco.service.CarrinhoService;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.ParseCsv;

/**
 * Implementação da camada DAO da entidade Escolha;
 * 
 * @author Sidronio
 * 08/01/2016
 */
@Stateless
public class EscolhaServiceImpl extends SimpleServiceLayerImpl<Escolha, Long> implements EscolhaService {

	private static final String MSG_EXPORTACAO_ERROR = "Não foi possível enviar o arquivo de escolhas.";
	private static final String MSG_EXPORTACAO_OK = "Exportação de %s escolhas efetuada com sucesso.";

	private final Logger log = Logger.getLogger(EscolhaServiceImpl.class.getSimpleName());
	
	@Inject
	private EscolhaDAO escolhaDAO;	
	
	@Inject
	private CarrinhoService carrinhoSrvc;
	
	@Inject
	private EstoqueService estoqueSrvc;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private EnvProperties props;
	
	@Override
	public GenericDAO getDAO() {
		return escolhaDAO;
	}
	
	@Override
	public void validaInsercao(Escolha escolha) throws RegraDeNegocioException {
		escolha.valida();
	}

	@Override
	public void validaExclusao(Escolha escolha) throws RegraDeNegocioException {
		
		if (!loginBean.getUserLogged().isAdmin()) {
			throw new RegraDeNegocioException("Apenas usuários Administrativos podem excluir uma escolha.");
		}
		
		if (!escolha.isPodeSerAtendida()) {
			throw new RegraDeNegocioException("A escolha já está em processo de separação ou finalizada.");
		}
		
	}
	
	@Override
	public void exclui(Long id) throws RegraDeNegocioException, DeleteException 
	{
		Escolha escolha = this.buscaPeloCodigoComItens(id);
		super.exclui(id);

		estoqueSrvc.estorna(escolha.getItens(), OrigemMovimento.ESTORNO_ESCOLHA);
	}
	
	@Override
	public List<Escolha> listaEscolhasDoCliente(Cliente cliente) {

		return this.escolhaDAO.filtrar(
				new Escolha(cliente), 
				Arrays.asList("cliente"), 
				Arrays.asList("cliente", "itens"), 
				Arrays.asList("data"), 
				null);
	}
	
	@Override
	public void criaEscolhaPeloCarrinho(List<ItemCarrinho> carrinho, Cliente cliente) throws RegraDeNegocioException {
		/*
		Escolha escolha = this.buscaEscolhaAbertaDoCliente(cliente);
		
		if (null == escolha) {
			
			escolha = new EscolhaFactory()
					.doCliente(cliente)
					.naData(LocalDate.now())
					.comEnivoPara(cliente.informaDataFinalDaSemana())
					.comQtdMaximaDeItens(cliente.getQtdDePecasParaEscolha())
					.comOsItens(carrinho)
					.cria();
			
			estoqueSrvc.movimentaEscolha(escolha.getItens());
			
		} else {
			
			escolha.adicionaItensDoCarrinho(carrinho);
			estoqueSrvc.movimentaEscolha(carrinho);
		}
		
		carrinhoSrvc.esvazia();
		
		this.salva(escolha);
		*/
	}

	@Override
	public void criaEscolhaPeloCarrinho(Carrinho carrinho, Cliente cliente) throws RegraDeNegocioException {
		
		Escolha escolha = this.buscaEscolhaAbertaDoCliente(cliente);
		
		if (null == escolha) {
			
			escolha = new EscolhaFactory()
					.doCliente(cliente)
					.naData(LocalDateTime.now())
					.comEnivoPara(cliente.dataFinalDoCicloAtual().atTime(23, 59, 59))
					.comQtdMaximaDeItens(cliente.getQtdDePecasParaEscolha())
					.comOValorMaximo(new BigDecimal(cliente.getValorParaEscolha()))
					.comOsItens(carrinho.getItens())
					.cria();
			
			estoqueSrvc.movimenta(escolha.getItens(), OrigemMovimento.ESCOLHA);
			
		} else 
		{
			escolha.setQtdMaximaPermitida(new BigDecimal(cliente.getQtdDePecasParaEscolha()));
			escolha.setValorMaximoPermitido(new BigDecimal(cliente.getValorParaEscolha()));
			escolha.adicionaItensDoCarrinho(carrinho.getItens());
			
			estoqueSrvc.movimenta(carrinho.getItens(), OrigemMovimento.ESCOLHA);
		}
		
		carrinhoSrvc.esvazia();
		
		this.salva(escolha);
	}
	
	@Override
	public Escolha buscaEscolhaAbertaDoCliente(Cliente cliente) {
		return this.escolhaDAO.buscarAbertaDoCliente(cliente);
	}

	@Override
	public Escolha buscaPeloCodigoComItens(Long codigoEscolha) 
	{
		return escolhaDAO.buscarComRelacionamentos(codigoEscolha);
	}
	
	@Override
	public Escolha buscarParaAtendimento(Long codigo) {
	
		return escolhaDAO
				.buscarPeloCodigoComItensOrdenadoPelaLocalizacao(codigo);
	}
	
	@Override
	public List<Escolha> filtraPelaPesquisa(FiltroEscolha filtro, Paginator paginator) {
		
		return escolhaDAO.filtrarPeloModoEspecifico(
				filtro, 
				paginator);
	}
	
	@Override
	public BigDecimal qtdDePecasEscolhidasPorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim) {
		BigDecimal qtd = escolhaDAO.calcularQtdDePecasEscolhidasPorPeriodo(cliente, inicio, fim);
		
		return qtd == null ? BigDecimal.ZERO : qtd; 
	}
	
	@Override
	public Escolha buscaPeloClientePorPeriodo(Cliente cliente, LocalDate dataInicial, LocalDate dataFinal) {
		return this.escolhaDAO.buscarEscolhaPorClienteEPeriodo(cliente, dataInicial, dataFinal);
	}

	@Override
	public List<RelatorioEscolha> geraRelatorioDeEscolha(Long codigo) {
		return this.escolhaDAO.gerarInformacoesDoRelatorio(codigo);
	}
	
	@Override
	public boolean clientePossuiEscolha(Cliente cliente) {
		return this.listaEscolhasDoCliente(cliente).size() > 0;
	}
	
	@Override
	public Escolha buscaComRelacionamentos(Long codigo) {
		return this.escolhaDAO.buscarComRelacionamentos(codigo);
	}
	
	@Override
	public void enviaEscolhasJaVencidas() {
		this.escolhaDAO.atualizaStatusEDataDeEnvioDeEscolhasVencidas();
	}
	
	@Override
	public Escolha buscarEscolhaDaSemanaAtual(Cliente cliente) throws RegraDeNegocioException {
		return this.escolhaDAO.buscarEscolhaPorClienteEPeriodo(
				cliente, 
				cliente.dataInicialDoCicloAtual(), 
				cliente.dataFinalDoCicloAtual());
	}
	
	@Override
	public BigDecimal valorDePecasEscolhidasDescontaveisDoCicloAtual(Cliente cliente) throws RegraDeNegocioException 
	{
		Optional<Escolha> atual = Optional.ofNullable(this.buscarEscolhaDaSemanaAtual(cliente));
		
		if (!atual.isPresent())
		{
			return BigDecimal.ZERO;
		}
		
		return atual.get().valorDosItensDescontaveis();
	}
	
	@Override
	public BigDecimal qtdDePecasEscolhidasDoCicloAtual(Cliente cliente) throws RegraDeNegocioException 
	{
		Optional<Escolha> atual = Optional.ofNullable(this.buscarEscolhaDaSemanaAtual(cliente));
		
		if (!atual.isPresent())
		{
			return BigDecimal.ZERO;
		}
		
		return atual.get().qtdDeItensDescontaveis();
	}
	
	@Override
	public List<Escolha> filtraPelaPesquisaSemPaginacao(FiltroEscolha filtro) 
	{
		return this.escolhaDAO.filtrarPeloModoEspecificoSemPaginacao(filtro);
	}
	
	@Override
	public int exportaEscolhas(List<Escolha> escolhas) throws IOException, IntegracaoException 
	{
		
		File file = ParseCsv.criaArquivoCsvFromStream(
				escolhas.stream().map(Escolha::generateCsv),  
				props.get("file-name-escolhas"));
		
		String server = props.get("ftp-server");
		String path = props.get("ftp-path-integracao");
		String user = props.get("ftp-user");
		String pass = props.get("ftp-pass");
		String remoteFileName = props.get("file-name-escolhas");
		
		boolean success = ParseCsv.sendFtp(file, server, path, user, pass, remoteFileName);
		
		if(!success)
		{
			log.info(String.format(MSG_EXPORTACAO_ERROR));
			throw new IntegracaoException(MSG_EXPORTACAO_ERROR);
		}
		
		int qtd = escolhas.size();
		
		log.info(String.format(MSG_EXPORTACAO_OK, qtd));
		
		return qtd;
	}
}
