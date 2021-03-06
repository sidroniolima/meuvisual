package net.mv.meuespaco.service;

import java.time.LocalDateTime;
import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaVenda;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.CarrinhoVenda;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cupom;
import net.mv.meuespaco.model.loja.ItemVenda;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface Service para a entidade Venda.
 * 
 * @author Sidronio
 * @created 23/08/2016
 */
/**
 * @author sidronio
 *
 */
public interface VendaService extends SimpleServiceLayer<Venda, Long> {

	/**
	 * Lista as vendas de um cliente.
	 * 
	 * @param cliente
	 * @return lista de vendas.
	 */
	public List<Venda> vendasDoCliente(Cliente cliente);
	
	/**
	 * Cria a venda pelo itens do carrinho.
	 * 
	 * @param carrinho
	 * @param cliente
	 * @return a venda criada e salva
	 * @throws RegraDeNegocioException 
	 */
	public Venda criaVendaPeloCarrinho(Carrinho carrinho, Cliente cliente) throws RegraDeNegocioException;

	/**
	 * Busca os itens de uma venda pelo código com 
	 * as informações do produto, fotos, subgrupo e grade.
	 * @param codigo da venda.
	 * @return itens completos.
	 */
	public List<ItemVenda> buscaItensCompleto(Long codigo);

	/**
	 * Busca uma compra com todos os seus relacionamentos.
	 * 
	 * @param codigo da venda
	 * @return venda com itens, produtos, fotos, subgrupo e grade.
	 */
	public Venda buscaCompletaPeloCodigo(Long paramCodigo);

	/**
	 * Filtra os registros de venda de forma paginada e de 
	 * modo específico pelo código, cliente, data de venda e status.
	 * 
	 * @param filtro código, cliente, data de venda e status.
	 * @param paginator
	 * @return lista de vendas que obedecem ao critério.
	 */
	public List<Venda> filtraPelaPesquisa(FiltroPesquisaVenda filtro, Paginator paginator);

	/**
	 * Cria uma venda pelo carrinho com cupom.
	 * 
	 * @param carrinho
	 * @param cliente
	 * @param cupom
	 * @return a venda criada
	 * @throws RegraDeNegocioException
	 */
	public Venda criaVendaPeloCarrinho(CarrinhoVenda carrinho, Cliente cliente, Cupom cupom)  throws RegraDeNegocioException;
	
	/**
	 * Busca e retorna a última compra do cliente.
	 * 
	 * @param cliente
	 * @return última compra do cliente
	 */
	public Venda buscaUtilmaDoCliente(Cliente cliente);

	/**
	 * Registra e salva o pagamento de uma venda.
	 * @param venda atual
	 * 
	 * @param paymentId id do pagamento gerado pela Api.
	 * @param localDateTime horario do pagamento
	 * @throws RegraDeNegocioException 
	 */
	public void registraPagamento(Venda venda, String paymentId, LocalDateTime localDateTime) throws RegraDeNegocioException;

	/**
	 * Cancela uma venda se possível e salva o horário e atualiza o 
	 * status.
	 * @param vendaSelecionada
	 * @throws RegraDeNegocioException caso não seja possível cancelar
	 * @throws IntegracaoException 
	 * @throws CieloException 
	 */
	public void cancelaVenda(Venda vendaSelecionada) throws RegraDeNegocioException, IntegracaoException, CieloException;

	/**
	 * Busca uma venda pelo Id do pagamento (PaymentId)
	 * 
	 * @param paymentId
	 * @return Venda
	 */
	public Venda buscaPeloPaymentId(String paymentId);

	/**
	 * Registra o pagamento via integração Cielo.
	 * @param venda
	 * @param pagamento
	 * @throws IntegracaoException 
	 * @throws CieloException 
	 * @throws RegraDeNegocioException 
	 */
	public void registraPagamento(Venda venda, Pagamento pagamento) throws CieloException, IntegracaoException, RegraDeNegocioException;

	/**
	 * Consulta o pagamento de uma Venda.
	 * 
	 * @param venda
	 * @return Pagamento da venda.
	 * @throws IntegracaoException 
	 * @throws CieloException 
	 */
	public Pagamento consultaPagamento(Venda venda) throws CieloException, IntegracaoException;

	public Venda criaVendaDoNada();
	
}
