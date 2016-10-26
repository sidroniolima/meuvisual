package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.primefaces.model.UploadedFile;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.dao.ClienteDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cpf;
import net.mv.meuespaco.model.loja.Documento;
import net.mv.meuespaco.model.loja.StatusCliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.ParseFromCsv;
import net.mv.meuespaco.util.ValoresDoErp;

/**
 * Implementação da camada Service da entidade Cliente.
 * 
 * @author Sidronio
 * 22/12/2015
 */
@Stateless
public class ClienteServiceImpl extends SimpleServiceLayerImpl<Cliente, Long> implements ClienteService, Serializable {

	private static final long serialVersionUID = 5643535166157974287L;
	
	@Inject
	private ClienteDAO clienteDAO;
	
	@Inject
	private EscolhaService escolhaSrvc;
	
	@Inject
	private RegiaoService regiaoSrvc;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;

	public ClienteServiceImpl() {	}
	
	public ClienteServiceImpl(EscolhaService escolhaSrvc, Cliente clienteLogado) {
		this.escolhaSrvc = escolhaSrvc;
		this.clienteLogado = clienteLogado;
	}

	@Override
	public void validaInsercao(Cliente cliente) throws RegraDeNegocioException {
		
		cliente.valida();
		
	}

	@Override
	public void validaExclusao(Cliente cliente) throws RegraDeNegocioException {
		
		if (this.escolhaSrvc.clientePossuiEscolha(cliente)) {
			throw new RegraDeNegocioException("O cliente possui escolha(s). "
					+ "Para excluí-lo é necessário excluir a(s) escolha(s) primeiro.");
		}
	}

	@Override
	public Cliente buscaClientePorCpfESenha(Documento cpf, String senha) {
		
		Cliente cliente = new Cliente(cpf, senha);
		
		return this.clienteDAO.filtrar(
				cliente, 
				Arrays.asList("cpf","senha"),
				null,
				null,
				null).get(0);
	}
	
	@Override
	public Cliente buscaCompletoPeloCodigo(Long codigo) {
		return this.clienteDAO.buscarPeloCodigoComRelacionamento(
				codigo, 
				Arrays.asList("regiao","usuario"));
	}

	@Override
	public void salvaPreCadastro(Cliente cliente) throws RegraDeNegocioException {
		
		cliente.validaPreCadastro();
		
		this.verificaSeJaExisteClientePeloCfp(cliente.getCpf());
		
		this.clienteDAO.salvar(cliente);
	}
	
	@Override
	public void verificaSeJaExisteClientePeloCfp(Documento cpf) throws RegraDeNegocioException {
		
		if (null != this.clienteDAO.buscarPeloCpf(cpf)) {
			throw new RegraDeNegocioException("Já existe um Cliente com este Cpf.");
		}
	}

	@Override
	public Cliente buscaPeloCodigoComRegiao(Long codigo) {
		return this.clienteDAO.buscarPeloCodigoComRelacionamento(codigo, Arrays.asList("regiao"));
	}
	
	@Override
	public Cliente buscaClientePeloUsuario(Usuario usuario) {
		return this.clienteDAO.buscaClientePeloUsuario(usuario);
	}
	
	@Override
	public Cliente buscaClientePeloUsuarioLogado() {
		
		//Usuario usuario = loginBean.getUserLogged();
		
		//return this.clienteDAO.buscaClientePeloUsuario(usuario);
		return clienteLogado;
	}
	
	@Override
	public void verificaSeOUsuarioLogadoPodeEscolher() throws RegraDeNegocioException {

		Semana semana = this.getSemanaDoClienteLogado();

		if (!semana.isCicloAberto()) {
			
			throw new RegraDeNegocioException(
					String.format("Não há ciclo aberto. O próximo será aberto em %s dia(s) em %s .", 
							semana.diasAteOProximoCiclo(),
							semana.dataDoProximoCiclo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		} 
		
		if (this.qtdDisponivelParaEscolha() == 0) 
		{
			throw new RegraDeNegocioException(
					String.format("Você já escolheu todas as peças disponíveis para este ciclo. "
							+ "O próximo será aberto em %s dia(s) em %s .", 
							semana.diasAteOProximoCiclo(),
							semana.dataDoProximoCiclo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		}
		
		if (this.valorDisponivelParaEscolha().compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new RegraDeNegocioException(
					String.format("Você já escolheu o valor permitido para este ciclo. "
							+ "O próximo será aberto em %s dia(s) em %s .", 
							semana.diasAteOProximoCiclo(),
							semana.dataDoProximoCiclo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		
		}
		
	}
	
	/**
	 * Retorna a semana do cliente logado.
	 * 
	 * @return semana.
	 * @throws RegraDeNegocioException 
	 */
	@Override
	public Semana getSemanaDoClienteLogado() throws RegraDeNegocioException {
		return this.clienteLogado.semanaAtualDoCliente();
	}

	@Override
	public String criaMensagemParaClienteLogado() {

		try {
			Cliente cliente = this.buscaClientePeloUsuarioLogado();
			Semana semana = cliente.getRegiao().getSemana();
			
			verificaSeOUsuarioLogadoPodeEscolher();

			return String.format(
					"Você ainda tem até o dia %s para fazer a escolha de %s peça(s) ou "
					+ " peças no valor total de R$ %s. ", 
						semana.getDataFinal().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
						this.qtdDisponivelParaEscolha(),
						this.valorDisponivelParaEscolha().toString().replace('.', ','));

		} catch (RegraDeNegocioException e) {
			return e.getMessage();
		}
		
	}
	
	@Override
	public BigDecimal qtdDePecasEscolhidasNoCicloAtual() throws RegraDeNegocioException {
		
		Cliente cliente = this.buscaClientePeloUsuarioLogado();
		//Semana semana = cliente.getRegiao().getSemana();
		
		//BigDecimal qtdDePecasDescontaveis = this.escolhaSrvc.qtdDePecasEscolhidasPorPeriodo(
		//		cliente, 
		//		semana.getDataInicial(), 
		//		semana.getDataFinal());
		
		//return qtdDePecasDescontaveis;
		
		return this.escolhaSrvc.qtdDePecasEscolhidasDoCicloAtual(cliente);
	}

	@Override
	public BigDecimal valorEscolhidoNoCicloAtual() throws RegraDeNegocioException {
		
		Cliente cliente = this.buscaClientePeloUsuarioLogado();
		
		/*Semana semana = cliente.getRegiao().getSemana();
		
		Escolha escolha = this.escolhaSrvc
				.buscaPeloClientePorPeriodo(cliente, semana.getDataInicial(), semana.getDataFinal());
		
		if (null == escolha) {
			return BigDecimal.ZERO;
		}
		
		return escolha.valorDosItensDescontaveis();
		*/
		
		return this.escolhaSrvc.valorDePecasEscolhidasDescontaveisDoCicloAtual(cliente);
	}
	
	@Override
	public int qtdDisponivelParaEscolha() throws RegraDeNegocioException {
		
		Cliente cliente = this.buscaClientePeloUsuarioLogado();
		
		return 
			(int) (cliente.getQtdDePecasParaEscolha() 
					- this.qtdDePecasEscolhidasNoCicloAtual().floatValue());
				
	}
	
	@Override
	public BigDecimal valorDisponivelParaEscolha() throws RegraDeNegocioException {
		
		Cliente cliente = this.buscaClientePeloUsuarioLogado();

		return new BigDecimal(cliente.getValorParaEscolha())
					.subtract(this.valorEscolhidoNoCicloAtual());
	}
	
	@Override
	public List<Long> atualizaInformacoesVindasDoErp() {
		
		List<Long> clientesDoArquivo = new ArrayList<Long>();
		
		Map<String, ValoresDoErp> registros = ParseFromCsv.leArquivoDoUpload();

		registros.forEach(
				(p,v) -> {
					Cliente cliente = this.buscaPeloCodigoSiga(p);
					
					if (null != cliente) {
						
						try {
							
							clientesDoArquivo.add(cliente.getCodigo());
							cliente.atualizaValoresDoErp(v.getQtdPermitida(), v.getValorPermitido());
							this.salva(cliente);
						
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				});
		
		return clientesDoArquivo;
	}
	
	@Override
	public Cliente buscaPeloCodigoSiga(String codigoSiga) {
	
		return this.clienteDAO.buscarPeloCodigoSiga(codigoSiga);
	}

	/**
	 * Inativa os clientes que não estão nos registros ativos 
	 * exportados pelo Erp.
	 * 
	 * @param registros
	 */
	@Override
	public void inativaClientesQueNaoEstaoEntreOsCodigos(List<Long> codigos) {
		this.clienteDAO.inativaClientesQueNaoEstaoNaListagem(codigos);
	}
	
	@Override
	public void importaPreCadastrosDoERP(UploadedFile file) {

		Map<String, List<String>> registros = ParseFromCsv.geraInformaçõesDaExportaçãoDoERP(file);
		
		registros.forEach(
				(p,v) -> {
						
					try {
						
						Cliente cliente = Cliente.parse(p, v);
						this.salvaPreCadastro(cliente);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
		});
		
	}
	
	@Override
	public void atualizaSenhaDosCadastrosPeloSistemaLegado(UploadedFile file) {

		Map<String, List<String>> registros = ParseFromCsv.coletaASenhaDoSistemaLegado(file);

		registros.forEach(
				(p,v) -> {
						
					try {
						
						if (null != p && !p.isEmpty()) {
						
							Cliente cliente = this.buscarClientePeloCpf(new Cpf(p));
							
							if (null != cliente) {
								cliente.setSenha(v.get(0));
								
								cliente.efetivaCadastro();
								
								this.salva(cliente);
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
		});
		
	}
	
	@Override
	public void atualizaRegiaoDosClientesPeloERP(UploadedFile file) {

		Map<String, List<String>> registros = ParseFromCsv.atualizaARegiaoDosClientes(file);
	
		registros.forEach(
				(p,v) -> {
						
					try {
						
						if (null != p && !p.isEmpty()) {
						
							Cliente cliente = this.buscarClientePeloCpf(new Cpf(p.trim()));
							
							if (null != cliente) {
								
								Regiao regiao = this.regiaoSrvc.buscaPeloCodigo(new Long(v.get(0)));
								
								if (null != regiao) {
									cliente.setRegiao(regiao);
									this.salva(cliente);
									
								}
								
								
							}
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					
		});
		
	}
	
	@Override
	public Cliente buscarClientePeloCpf(Documento cpf) {
		return this.clienteDAO.buscarPeloCpf(cpf);
	}

	@Override
	public List<Cliente> listaClientesAtivos() {
		
		return clienteDAO.filtrar(
				new Cliente(StatusCliente.ATIVO), 
				Arrays.asList("status"), 
				null, 
				Arrays.asList("codigoSiga"), 
				null);
	}
	
	@Override
	public List<Cliente> filtraCliente(FiltroCliente filtro) {
		return this.clienteDAO
				.filtrarPelaPesquisa(filtro.getCodigoSiga(), filtro.getCpf(), filtro.getNome());
	}
	
	@Override
	public void efetivaCadastro(Cliente cliente) throws RegraDeNegocioException {
		
		if (jaExisteCodigoSiga(cliente.getCodigo(), cliente.getCodigoSiga())) {
			throw new RegraDeNegocioException("Já existe um outro cliente com este código siga.");
		}
		
		cliente.efetivaCadastro();
		
		this.salva(cliente);
	}

	@Override
	public boolean jaExisteCodigoSiga(Long codigo, String codigoSiga) {
		return null != this.clienteDAO.buscarOutroPeloCodigoSiga(codigo, codigoSiga);
	}
	
	@Override
	public List<Cliente> filtraPeloModoEspecifico(FiltroCliente filtro, Paginator paginator) {
		return this.clienteDAO.filtraPeloModoEspecifico(filtro, paginator);
	}
	
	@Override
	public GenericDAO getDAO() {
		return this.clienteDAO;
	}
	
}
