package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.ReservaProdutoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Abstração do Carrinho para utilização do consignado e do de venda.
 * 
 * @author Sidronio
 * @created 17/08/16
 */
public abstract class CarrinhoAbstractBean implements Serializable {

	private static final long serialVersionUID = -801776575097155986L;
	
	@Inject
	protected ReservaProdutoService reservaProdutoSrvc;
	
	@Inject
	protected EstoqueService estoqueSrvc;

	@Inject
	@ClienteLogado
	private Cliente clienteLogado;	
			
	protected ItemCarrinho itemSelecionado;
	
	public CarrinhoAbstractBean() {	}
	
	public CarrinhoAbstractBean(ReservaProdutoService reservaProdutoSrvc, EstoqueService estoqueSrvc) {
		this.reservaProdutoSrvc = reservaProdutoSrvc;
		this.estoqueSrvc = estoqueSrvc;
	}

	@PostConstruct
	public void init() {
		//reservaProdutoSrvc.imprimeReserva();
	}
	
	/**
	 * Cria um carrinho com o valor e qtd disponíveis. É criado 
	 * no momento do login.
	 * 
	 * @param valor disponível para adição de itens.
	 * @param qtd disponível para adição de itens.
	 * @throws RegraDeNegocioException 
	 */
	public abstract void criaCarrinho() throws RegraDeNegocioException;
	
	/**
	 * Informa a quantidade de itens no carrinho.
	 * 
	 * @return
	 */
	public BigDecimal getQtdDeItens() {
		return this.getCarrinho().qtdDeItens();
	}
	
	/**
	 * Adiciona o produto com quantidade e grade ao carrinho.
	 * 
	 * @param produto selecionado.
	 * @param qtd definida.
	 * @param gradeSelecionada selecionada.
	 * @throws RegraDeNegocioException lança exceção se não for possível 
	 * adicionar o produto.
	 */
	public void adicionaProduto(Produto produto, BigDecimal qtd, Grade gradeSelecionada) throws RegraDeNegocioException {
		
		BigDecimal qtdDisponivel = this.qtdDisponivelDoProduto(produto, gradeSelecionada);
		
		if (qtdDisponivel.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("Não há produtos disponíveis desta grade.");
		}
		
		if (qtd.compareTo(qtdDisponivel) > 0) {
			
			throw new RegraDeNegocioException(
					String.format("Para a grade seleciona há apenas %s produtos disponíveis.", 
							qtdDisponivel.setScale(0)));
		}
		
		this.getCarrinho().adicionaItem(new ItemCarrinho(produto, qtd, gradeSelecionada));
		reservaProdutoSrvc.adicionaReserva(produto, gradeSelecionada, qtd);
	}
	
	/**
	 * Informa a quantidade disponível do produto de acordo com 
	 * seu saldo em estoque e reserva de produto.
	 * 
	 * @param produto
	 * @param grade
	 * @return
	 */
	public BigDecimal qtdDisponivelDoProduto(Produto produto, Grade grade) {
		BigDecimal qtd = estoqueSrvc.qtdDisponivelParaVenda(produto, grade)
				.subtract(reservaProdutoSrvc.qtdReservadaDoProduto(produto, grade));
		
		return qtd;
	}

	/**
	 * Remove o item selecionado do carrinho.
	 */
	public void removeItem() {
		
		if (null == itemSelecionado)
		{
			FacesUtil.addErrorMessage("Este item não pode ser removido. "
							+ "Recarregue a página para atualização dos dados.");
			
			return;
		}
		
		try 
		{
			this.getCarrinho().removeItem(itemSelecionado);
			
			reservaProdutoSrvc.removeReserva(
					itemSelecionado.getProduto(), 
					itemSelecionado.getGrade(), 
					itemSelecionado.getQtd());
			
			FacesUtil.addSuccessMessage(
					String.format("Item %s removido com sucesso.", itemSelecionado.getProduto().getDescricao()));
			
		} catch (RegraDeNegocioException e) 
		{
			FacesUtil.addErrorMessage(
					String.format("Não foi possível remover o item %s. %s", 
							itemSelecionado.getProduto().getDescricao(),
							e.getMessage()));
		}
		
		itemSelecionado = null;
	}
	
	/**
	 * Esvazia o carrinho.
	 */
	public void esvazia() {
		
		if (null == this.getCarrinho()) {
			return;
		}
		
		for (ItemCarrinho item : this.getCarrinho().getItens()) 
		{
			this.reservaProdutoSrvc.removeReserva(item.getProduto(), item.getGrade(), item.getQtd());
		}
		
		this.getCarrinho().esvazia();
	}
	
	/**
	 * Cancela o carrinho caso exista peças quando 
	 * a sessão for finalizada.
	 */
	public void cancelaCarrinho() {
		this.esvazia();
	}
	
	/**
	 * Finaliza o carrinho, gerando o pedido e 
	 * redirecinando para os pedidos do cliente. 
	 * 
	 * @return
	 */
	public abstract String finalizaCarrinho();
	
	/**
	 * Retorna o carrinho.
	 * 
	 * @return
	 */
	public abstract Carrinho getCarrinho();
	
	/**
	 * @return the itemSelecionado
	 */
	public ItemCarrinho getItemSelecionado() {
		return itemSelecionado;
	}
	/**
	 * @param itemSelecionado the itemSelecionado to set
	 */
	public void setItemSelecionado(ItemCarrinho itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}

	/**
	 * Retorna o cliente logado.
	 * @return
	 */
	public Cliente getClienteLogado() 
	{
		return clienteLogado;
	}
}
