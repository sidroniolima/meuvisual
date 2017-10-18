package net.mv.meuespaco.service.impl;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.timeout;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.jfree.util.Log;
import org.primefaces.model.UploadedFile;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.dao.ClienteDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Permissao;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.integracao.ClientesDoErp;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cpf;
import net.mv.meuespaco.model.loja.Documento;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.model.loja.StatusCliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.service.IntegracaoService;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.ParseCsv;

/**
 * Implementação da camada Service da entidade Cliente.
 * 
 * @author Sidronio
 * 22/12/2015
 */
@Stateless
public class ClienteServiceImpl extends SimpleServiceLayerImpl<Cliente, Long> implements ClienteService, Serializable {

	private static final long serialVersionUID = 5643535166157974287L;

	private Logger logger = Logger.getLogger(ClienteServiceImpl.class.getSimpleName());
	
	private static final String ATUALIZACAO_CLIENTES_INICIO = "ATUALIZACAO CLIENTES - Início";
	private static final String ATUALIZACAO_CLIENTES_FIM = "ATUALIZACAO CLIENTES - Fim";
	private static final Object ATUALIZACAO_CLIENTES_INATIVACAO = "ATUALIZACAO CLIENTES - Início do processo de inativação de clientes.";
	
	private final String msgEfetivacao = "Cliente %s efetivado.";
	private final String msgClienteNaoCadastrado = "O cliente código %s não está cadastrado no site.";
	private final String msgRegiaoNaoExite = "A região %s não existe.";
	private final String msgClienteAtualizado = "Cliente %s atualizado: qtd %s e valor %s.";
	private final String msgErroAoSalvar = "Não foi possível salvar (exceção) o cliente %s. %s";
	private final String msgErroAoEfetivar = "Não foi possível efetivar o cliente %s. %s";
	private final String msgAtualizacaoDeRegiao = "Autalizada a região %s para a %s do cliente %s.";
	private String msgErroAoSalvarRegraDeNegocio = "Não foi possível salvar (regra de negócio) o cliente %s. %s";;
	
	@Inject
	private ClienteDAO clienteDAO;
	
	@Inject
	private EscolhaService escolhaSrvc;
	
	@Inject
	private RegiaoService regiaoSrvc;
	
	@Inject
	private IntegracaoService integracaoSrvc;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	public ClienteServiceImpl() {	}
	
	public ClienteServiceImpl(EscolhaService escolhaSrvc, Cliente clienteLogado) {
		this.escolhaSrvc = escolhaSrvc;
		this.clienteLogado = clienteLogado;
	}
	
	public ClienteServiceImpl(ClienteDAO clienteDAO, IntegracaoService integracaoSrvc, 
			RegiaoService regiaoSrvc, EscolhaService escolhaSrvc, Cliente clienteLogado) 
	{
		this.clienteDAO = clienteDAO;
		this.integracaoSrvc = integracaoSrvc;
		this.regiaoSrvc = regiaoSrvc;
		this.escolhaSrvc = escolhaSrvc;
		this.clienteLogado = clienteLogado;
	}

	@Override
	public void validaInsercao(Cliente cliente) throws RegraDeNegocioException 
	{
		cliente.valida();
	}

	@Override
	public void validaExclusao(Cliente cliente) throws RegraDeNegocioException 
	{
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
	public Cliente buscaClientePeloUsuarioLogado() 
	{
		return clienteLogado;
	}
	
	@Override
	public void verificaSeOUsuarioLogadoPodeEscolher() throws RegraDeNegocioException {

		verificaCicloAberto(); 
		
		Optional<Escolha> escolhaAtual = Optional.ofNullable(this.escolhaSrvc.buscarEscolhaDaSemanaAtual(this.clienteLogado));
		
		BigDecimal valorJaEscolhido = 
				escolhaAtual.isPresent()
				? escolhaAtual.get().valorDosItensDescontaveis() 
				: BigDecimal.ZERO;
		
		BigDecimal qtdJaEscolhida = 
				escolhaAtual.isPresent() 
				? escolhaAtual.get().qtdDeItensDescontaveis()
				: BigDecimal.ZERO;
		
		verificaQtdPermitidaDoConsignado(qtdJaEscolhida);
		verificaValorDisponivelParaConsignado(valorJaEscolhido);
	}

	@Override
	public void verificaValorDisponivelParaConsignado(BigDecimal valorJaEscolhido) throws RegraDeNegocioException 
	{
		Semana semana = this.getSemanaDoClienteLogado();
		
		if (this.valorDisponivelParaEscolha(valorJaEscolhido).compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new RegraDeNegocioException(
					String.format("Você já escolheu o valor permitido para este ciclo. "
							+ "O próximo será aberto em %s dia(s) em %s .", 
							semana.diasAteOProximoCiclo(),
							semana.dataDoProximoCiclo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		
		}
	}

	@Override
	public void verificaQtdPermitidaDoConsignado(BigDecimal qtdJaEscolhida) throws RegraDeNegocioException 
	{
		Semana semana = this.getSemanaDoClienteLogado();
		
		if (this.qtdDisponivelParaEscolha(qtdJaEscolhida) <= 0) 
		{
			throw new RegraDeNegocioException(
					String.format("Você já escolheu todas as peças disponíveis para este ciclo. "
							+ "O próximo será aberto em %s dia(s) em %s .", 
							semana.diasAteOProximoCiclo(),
							semana.dataDoProximoCiclo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		}
	}

	@Override
	public void verificaValorDisponivelParaConsignado() throws RegraDeNegocioException 
	{
		Semana semana = this.getSemanaDoClienteLogado();
		
		if (this.valorDisponivelParaEscolha().compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new RegraDeNegocioException(
					String.format("Você já escolheu o valor permitido para este ciclo. "
							+ "O próximo será aberto em %s dia(s) em %s .", 
							semana.diasAteOProximoCiclo(),
							semana.dataDoProximoCiclo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		
		}
	}

	@Override
	public void verificaQtdPermitidaDoConsignado() throws RegraDeNegocioException 
	{
		Semana semana = this.getSemanaDoClienteLogado();
		
		if (this.qtdDisponivelParaEscolha() <= 0) 
		{
			throw new RegraDeNegocioException(
					String.format("Você já escolheu todas as peças disponíveis para este ciclo. "
							+ "O próximo será aberto em %s dia(s) em %s .", 
							semana.diasAteOProximoCiclo(),
							semana.dataDoProximoCiclo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		}
	}
	
	@Override
	public void verificaCicloAberto() throws RegraDeNegocioException 
	{
		Semana semana = this.getSemanaDoClienteLogado();
		
		if (!semana.isCicloAberto()) {
			
			throw new RegraDeNegocioException(
					String.format("Não há ciclo aberto. O próximo será aberto em %s dia(s) em %s .", 
							semana.diasAteOProximoCiclo(),
							semana.dataDoProximoCiclo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		}
	}
	
	/**
	 * Retorna a semana do cliente logado.
	 * 
	 * @return semana."Não foi possível a efetivação do cliente %s. %s"
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
	public BigDecimal qtdDePecasEscolhidasNoCicloAtual() throws RegraDeNegocioException 
	{
		Cliente cliente = this.buscaClientePeloUsuarioLogado();
		
		return this.escolhaSrvc.qtdDePecasEscolhidasDoCicloAtual(cliente);
	}

	@Override
	public BigDecimal valorEscolhidoNoCicloAtual() throws RegraDeNegocioException 
	{
		Cliente cliente = this.buscaClientePeloUsuarioLogado();
		
		return this.escolhaSrvc.valorDePecasEscolhidasDescontaveisDoCicloAtual(cliente);
	}
	
	@Override
	public int qtdDisponivelParaEscolha(BigDecimal qtdJaEscolhida) throws RegraDeNegocioException {
		
		Cliente cliente = this.buscaClientePeloUsuarioLogado();
		
		return 
			(int) (cliente.getQtdDePecasParaEscolha() - qtdJaEscolhida.floatValue());
				
	}
	
	@Override
	public BigDecimal valorDisponivelParaEscolha(BigDecimal valorJaEscolhido) throws RegraDeNegocioException {
		
		Cliente cliente = this.buscaClientePeloUsuarioLogado();

		return new BigDecimal(cliente.getValorParaEscolha()).subtract(valorJaEscolhido);
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
	public void atualizaInformacoesVindasDoErp() throws MalformedURLException, IOException 
	{
		logger.info(ATUALIZACAO_CLIENTES_INICIO);
		
		List<ClientesDoErp> registrosErp = this.integracaoSrvc.listaClientesDoErp();
		
		this.importaClientesDoErp(registrosErp);
		
		this.inativaClientesQueNaoEstaoEntreOsCodigos(
				registrosErp.parallelStream()
					.map(c -> c.getCodigoSiga())
					.collect(Collectors.toList())
				);
		
		logger.info(ATUALIZACAO_CLIENTES_FIM);
	}
	
	@Override
	public void importaClientesDoErp(List<ClientesDoErp> registrosErp)
	{
		List<Permissao> permissoesPadrao = asList(Permissao.ROLE_VENDA, Permissao.ROLE_BRINDE, Permissao.ROLE_CLIENTE);
		List<Permissao> permissoesRestritas = asList(Permissao.ROLE_VENDA, Permissao.ROLE_BRINDE);
		
		registrosErp.stream().forEach(c -> 
		{
			Optional<Cliente> optCliente = Optional.ofNullable(this.buscarClientePeloCpf(new Cpf(c.getCpf())));
			List<Permissao> permissoes = permissoesPadrao;
			
			List<Regiao> regioes = regiaoSrvc.buscaTodas();
			
			if (optCliente.isPresent())
			{
				Cliente cliente = optCliente.get();
				
				//TODO: cachear a lista de regiões
				//Optional<Regiao> optRegiao = Optional.ofNullable(regiaoSrvc.buscaPeloCodigoInterno(c.getCodigoRegiao()));
				Optional<Regiao> optRegiao = regioes
						.stream()
						.filter(r -> r.getCodigoInterno().equals(c.getCodigoRegiao()))
						.findFirst();
				
				if (optRegiao.isPresent())
				{
					Regiao regiaoDoErp = optRegiao.get();
					
					//TODO: permissões na região.
					if (regiaoDoErp.getCodigoInterno().equals("000001"))
					{
						permissoes = permissoesRestritas;
					} else 
					{
						permissoes = permissoesPadrao;
					}
					
					if (cliente.isPreCadastro())
					{
						try 
						{
							cliente.efetivaCadastro(c.getCodigoSiga(), optRegiao.get(), permissoes);
							logger.log(Level.INFO, String.format(this.msgEfetivacao, c.getCodigoSiga()));
							
						} catch (RegraDeNegocioException e) 
						{
							logger.log(Level.SEVERE, 
									String.format(this.msgErroAoEfetivar, c.getCodigoSiga(), e.getMessage()));
						}
					}
					
					if (!regiaoDoErp.equals(cliente.getRegiao()))
					{
						logger.log(Level.INFO, String.format(
								this.msgAtualizacaoDeRegiao, cliente.getRegiao().getCodigoInterno(), regiaoDoErp.getCodigoInterno(), c.getCodigoSiga()));						
						
						cliente.setRegiao(regiaoDoErp);
					}
					
				} else
				{
					logger.log(Level.WARNING, String.format(this.msgRegiaoNaoExite, c.getCodigoRegiao()));
				}
				
				try 
				{
					cliente.ativaCliente();
					cliente.atualizaValoresDoErp(c.getQtd(), c.getValor());	
					cliente.atualizaPermissoes(permissoes);
					
					this.salva(cliente);
					
					logger.log(Level.INFO, String.format(this.msgClienteAtualizado, c.getCodigoSiga(), c.getQtd(), c.getValor()));
					
				} catch (RegraDeNegocioException e) {
					
					logger.log(Level.SEVERE, 
							String.format(this.msgErroAoSalvarRegraDeNegocio, c.getCodigoSiga(), e.getMessage()));
				
				} catch (Exception e) 
				{
					logger.log(Level.SEVERE, 
							String.format(this.msgErroAoSalvar, c.getCodigoSiga(), e.getMessage()));					
				}
			} else
			{
				logger.log(Level.INFO, String.format(this.msgClienteNaoCadastrado, c.getCodigoSiga()));
			}
		});
	}
	
	@Override
	public Cliente buscaPeloCodigoSiga(String codigoSiga) 
	{
		return this.clienteDAO.buscarPeloCodigoSiga(codigoSiga);
	}

	/**
	 * Inativa os clientes que não estão nos registros ativos 
	 * exportados pelo Erp.
	 * 
	 * @param registros
	 */
	@Override
	public void inativaClientesQueNaoEstaoEntreOsCodigos(List<String> codigosSiga) 
	{
		Log.info(ATUALIZACAO_CLIENTES_INATIVACAO);
		this.clienteDAO.inativaClientesQueNaoEstaoNaListagem(codigosSiga);
	}
	
	@Override
	public void importaPreCadastrosDoERP(UploadedFile file) {

		Map<String, List<String>> registros = ParseCsv.geraInformaçõesDaExportaçãoDoERP(file);
		
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

		Map<String, List<String>> registros = ParseCsv.coletaASenhaDoSistemaLegado(file);

		registros.forEach(
				(p,v) -> {
						
					try {
						
						if (null != p && !p.isEmpty()) {
						
							Cliente cliente = this.buscarClientePeloCpf(new Cpf(p));
							
							if (null != cliente) {
								cliente.setSenha(v.get(0));
								
								cliente.efetivaCadastro(Arrays.asList(Permissao.ROLE_VENDA, Permissao.ROLE_BRINDE));
								
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

		Map<String, List<String>> registros = ParseCsv.atualizaARegiaoDosClientes(file);
	
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
	public void efetivaCadastro(Cliente cliente, List<Permissao> permissoes) throws RegraDeNegocioException {
		
		if (jaExisteCodigoSiga(cliente.getCodigo(), cliente.getCodigoSiga())) {
			throw new RegraDeNegocioException("Já existe um outro cliente com este código siga.");
		}
		
		cliente.efetivaCadastro(permissoes);
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
	public List<Cliente> filtraPeloModoEspecifico(FiltroCliente filtro) {
		return this.clienteDAO.filtraPeloModoEspecifico(filtro);
	}
	
	@Override
	public GenericDAO<Cliente, Long> getDAO() {
		return this.clienteDAO;
	}
	
}
