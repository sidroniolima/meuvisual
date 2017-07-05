package net.mv.meuespaco.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.List;

import org.primefaces.model.UploadedFile;

import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Permissao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.integracao.ClientesDoErp;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Documento;
import net.mv.meuespaco.util.Paginator;

/**
 * Abstração da camada Service da entidade Cliente.
 * 
 * @author Sidronio
 * 22/12/2015
 */
public interface ClienteService extends SimpleServiceLayer<Cliente, Long> {

	/**
	 * Busca um cliente pelo cpf e senha.
	 * 
	 * @param Cpf
	 * @param senha
	 * @return Cliente caso os dados de pesquisa sejam válidos.
	 */
	public Cliente buscaClientePorCpfESenha(Documento Cpf, String senha);
	
	/**
	 * Salva após a validação um pré-cadastro de cliente. 
	 * 
	 * @param cliente Pré-cadastro.
	 * @throws RegraDeNegocioException
	 */
	public void salvaPreCadastro(Cliente cliente) throws RegraDeNegocioException ;

	/**
	 * Retorna um cliente buscando pelo código com Região.
	 * 
	 * @param paramCodigo
	 * @return Cliente com região.
	 */
	public Cliente buscaPeloCodigoComRegiao(Long paramCodigo);

	/**
	 * Pesquisa um cliente pelo códigio trazendo 
	 * a Região e Usuário.
	 * 
	 * @param codigo
	 * @return
	 */
	public Cliente buscaCompletoPeloCodigo(Long codigo);
	
	/**
	 * Pesquisa um Cliente pelo Usuário.
	 * 
	 * @param usuario Usuário logado para pesquisa.
	 * @return Cliente que utiliza o usuário.
	 */
	public Cliente buscaClientePeloUsuario(Usuario usuario);
	
	/**
	 * Pesquisa um Cliente pelo Usuário logado.
	 * 
	 * @param usuario Usuário logado para pesquisa.
	 * @return Cliente que utiliza o usuário.
	 */
	public Cliente buscaClientePeloUsuarioLogado();	
	
	/**
	 * Verifica se um cliente pode fazer a escolha considerando 
	 * a data do ciclo e a quantidade já escolhida durante ele.
	 * 
	 * @return Se pode ou não efetuar uma escolha.
	 * @throws RegraDeNegocioException
	 */
	public void verificaSeOUsuarioLogadoPodeEscolher() throws RegraDeNegocioException;

	/**
	 * Cria a mensagem que será exibida na Top Bar relacionada ao ciclo e 
	 * quantidade de peças da escolha.
	 * 
	 * @return
	 */
	public String criaMensagemParaClienteLogado();
	
	/**
	 * Informa quantidade disponível para escolha, diminuindo o 
	 * que já foi utilizado em escolhas do atual cicloe da quantidade 
	 * definida pelo que foi vendido no acerto atual.
	 * 
	 * @return Quantidade disponível para escolha.
	 * @throws RegraDeNegocioException 
	 */
	public int qtdDisponivelParaEscolha() throws RegraDeNegocioException;
	
	/**
	 * Informa a quantidade de peças já escolhidas no atual ciclo.
	 * 
	 * @return Quantidade de peças.
	 * @throws RegraDeNegocioException caso não seja possível definir o 
	 * valor dos produtos. 
	 */
	public BigDecimal qtdDePecasEscolhidasNoCicloAtual() throws RegraDeNegocioException;
	
	/**
	 * Calcula o valor das peças descontáveis escolhidas no 
	 * ciclo atual.
	 * 
	 * @return
	 * @throws RegraDeNegocioException 
	 */
	public BigDecimal valorEscolhidoNoCicloAtual() throws RegraDeNegocioException;
	
	/**
	 * Calcula o valor disponível para escolha, diminuindo o 
	 * que já foi utilizado em escolhas do atual ciclo e do valor 
	 * permitido para o cliente.
	 * 
	 * @return Quantidade disponível para escolha.
	 * @throws RegraDeNegocioException 
	 */
	public BigDecimal valorDisponivelParaEscolha() throws RegraDeNegocioException;
	
	/**
	 * Atualiza a quantidade de peças para escolha para cada Cliente 
	 * ativo e inativa os que não estão na listagem.
	 * 
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public void atualizaInformacoesVindasDoErp() throws MalformedURLException, IOException;
	
	/**
	 * Lista clientes ativos.
	 * 
	 * @return Lista de clientes.
	 */
	public List<Cliente> listaClientesAtivos();
	
	/**
	 * Busca um cliente pelo código siga.
	 * 
	 * @param codigoSiga Código siga para pesquisa.
	 * @return Cliente.
	 */
	public Cliente buscaPeloCodigoSiga(String codigoSiga);

	/**
	 * Atualiza os clientes pelo arquivo exportado, utilizando 
	 * Upload.
	 * 
	 * @param file Arquivo de Upload com as informações do ERP.
	 */
	public void importaPreCadastrosDoERP(UploadedFile file);
	
	/**
	 * Atualiza a senha dos Clientes pelas informações do sistema 
	 * legado.
	 * 
	 * @param file Arquivo de clientes do sistema legado.
	 */
	public void atualizaSenhaDosCadastrosPeloSistemaLegado(UploadedFile file);
	
	/**
	 * Busca um registro pelo CPF.
	 * 
	 * @param cpf
	 * @return
	 */
	public Cliente buscarClientePeloCpf(Documento cpf);

	/**
	 * Atualiza a Região do Cadastro de Clientes pela exportação do 
	 * ERP.
	 * 
	 * @param file
	 */
	void atualizaRegiaoDosClientesPeloERP(UploadedFile file);

	/**
	 * Filtra um cliente pelo Filtro da Pesquisa, por meio do  
	 * Codigo Siga, Nome ou Cpf.
	 * 
	 * @param filtro
	 * @return 
	 */
	public List<Cliente> filtraCliente(FiltroCliente filtro); 
	
	/**
	 * Verifica se já existe um cliente com o cfp no 
	 * registro do pré-cadastro.
	 * 
	 * @param cpf
	 * @throws RegraDeNegocioException
	 */
	public void verificaSeJaExisteClientePeloCfp(Documento cpf) throws RegraDeNegocioException;

	/**
	 * Inativa os clientes pelos dados da atualização vinda do Erp. Os código 
	 * são os dos clientes que estão ativos, portanto deve-se inativar os que não 
	 * estão na listagem.
	 * 
	 * @param registros
	 */
	void inativaClientesQueNaoEstaoEntreOsCodigos(List<String> codigos);

	/**
	 * Efetiva um pré-cadastro, transformando-o em cliente, com 
	 * os dados do sistema legado.
	 * 
	 * @param cliente para efetivação.
	 * @param permissoes permissões para o usuário do cliente.
	 * @throws RegraDeNegocioException Lança exceção caso já exista um cliente com o 
	 * mesmo código siga ou a efetivação não seja validada.  
	 */
	public void efetivaCadastro(Cliente cliente, List<Permissao> permissoes) throws RegraDeNegocioException;

	/**
	 * Verifica se já existe um cliente com o código siga informado.
	 * 
	 * @param codigoSiga informado.
	 * @return se existe ou não cliente.
	 */
	boolean jaExisteCodigoSiga(Long codigo, String codigoSiga);

	/**
	 * Retorna a semana atual do cliente logado.
	 * 
	 * @return semana.
	 * @throws RegraDeNegocioException caso não esteja cadastrada a semana 
	 * do cliente.
	 */
	Semana getSemanaDoClienteLogado() throws RegraDeNegocioException;
	
	/**
	 * Filtra os registros de cliente pelo código siga, nome ou cpf.
	 * 
	 * @param filtro nome, cpf, ou código siga.
	 * @param paginator
	 * @return lista de clientes com paginação.
	 */
	public List<Cliente> filtraPeloModoEspecifico(FiltroCliente filtro, Paginator paginator);

	/**
	 * Importa os clientes vindo do ERP, atualizando os valores e efetivando os 
	 * pré-cadastros.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public void importaClientesDoErp(List<ClientesDoErp> registrosErp) throws MalformedURLException, IOException;

	/**
	 * Filtra os registros de cliente pelo código siga, nome ou cpf.
	 * 
	 * @param filtro nome, cpf, ou código siga.
	 * @return lista de clientes sem paginação.
	 */
	public List<Cliente> filtraPeloModoEspecifico(FiltroCliente filtro);
	
	/**
	 * Verifica se o ciclo do cliente está aberto e lança uma exceção 
	 * caso não esteja. Impedindo que o cliente faça a escolha de peças 
	 * no módulo consignado ou na área de brindes.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void verificaCicloAberto() throws RegraDeNegocioException;

	/**
	 * Verifica se a quantidade permitida para que o cliente faça a escolha de 
	 * peças consignadas. Cálculo feito com o valor do ciclo atual e os itens 
	 * já escolhidos neste período. Lança uma exceção caso não aja saldo positivo.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void verificaQtdPermitidaDoConsignado() throws RegraDeNegocioException;
	
	/**
	 * Verifica se o valor permitido para que o cliente faça a escolha de 
	 * peças consignadas. Cálculo feito com o valor do ciclo atual e os itens 
	 * já escolhidos neste período. Lança uma exceção caso não aja saldo positivo.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void verificaValorDisponivelParaConsignado() throws RegraDeNegocioException;
	
}
