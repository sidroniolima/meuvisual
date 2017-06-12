package net.mv.meuespaco.model.loja;

import java.math.BigDecimal;

import net.mv.meuespaco.exception.RegraDeNegocioException;

/**
 * Implementação do carrinho utilizado na venda consignada. Neste 
 * caso o carrinho tem dois limites, quantidade e valor.
 * 
 * @author Sidronio
 * @created 24/05/2016
 * @updated 08/08/2016
 */
public class CarrinhoConsignado extends Carrinho {

	private BigDecimal valorPermitido;
	private int qtdPermitida;
	
	public CarrinhoConsignado(BigDecimal valorPermitido, int qtdPermitida) 
	{
		super();
		this.valorPermitido = valorPermitido;
		this.qtdPermitida = qtdPermitida;
	}
	
	@Override
	public void adicionaItem(ItemCarrinho item) throws RegraDeNegocioException {
		item.valida();
		
		if (item.isDescontavel()) {
			if (!haSaldoDeQtdParaAdicaoDoItem(item.getQtd())) 
			{
				throw new RegraDeNegocioException("Não há saldo de quantidade para adição deste item ao carrinho.");
			}
			
			if (!haSaldoDeValorParaAdicaoDoItem(item.valorTotal()))
			{
				throw new RegraDeNegocioException("Não há saldo para o valor do item selecionado.");
			}
		}
		
		super.add(item);
	}
	

	/**
	 * Verifica se há saldo suficiente para o valor do item novo.
	 * 
	 * @param valorTotal do item a ser adicionado.
	 * @return se há ou não saldo.
	 * @throws RegraDeNegocioException caso não seja possível calcular 
	 * a quantidade descontável.
	 */
	boolean haSaldoDeValorParaAdicaoDoItem(BigDecimal valorDoItemNovo) throws RegraDeNegocioException {
		return this.calculaSaldoDeValor().compareTo(valorDoItemNovo) >= 0;
	}

	/**
	 * Verifica se há saldo para inclusão do item de acordo com a
	 * quantidade permitida informada na criação do carrinho.
	 * 
	 * @param qtd do item.
	 * @return se há ou não saldo suficiente.
	 * @throws RegraDeNegocioException caso não seja possível calcular 
	 * a quantidade descontável.
	 */
	boolean haSaldoDeQtdParaAdicaoDoItem(BigDecimal qtd) throws RegraDeNegocioException {
		return new BigDecimal(this.saldoDeQuantidade()).compareTo(qtd) >= 0;
	}

	/** 
	 * Calcula o saldo da quantidade dos itens 
	 * permitidos e os já adicionados. 
	 * 
	 * @return saldo da quantidade.
	 * @throws RegraDeNegocioException caso não seja possível calcular 
	 * a quantidade descontável.
	 */
	public int saldoDeQuantidade() throws RegraDeNegocioException 
	{
		BigDecimal qtdDescontavel = this.calculaQtdDescontavel();
		
		return new BigDecimal(qtdPermitida).subtract(qtdDescontavel).intValue();
	}
	
	/**
	 * Calcula o saldo do valor dos itens, considerando o valor descontável.
	 * 
	 * @return saldo de valor.
	 * @throws RegraDeNegocioException lança exceção se não puder verificar se 
	 * o item é descontável.
	 */
	public BigDecimal calculaSaldoDeValor() throws RegraDeNegocioException {
		BigDecimal valorDescontavel = this.calculaValorDescontavel();
		
		return valorPermitido.subtract(valorDescontavel);
	}
	
	/**
	 * Calcula o valor dos itens descontáveis. 
	 * 
	 * @return valor descontável.
	 * @throws RegraDeNegocioException lança exceção se não puder verificar se 
	 * o item é descontável.
	 */
	public BigDecimal calculaValorDescontavel() throws RegraDeNegocioException {
		
		BigDecimal valor = BigDecimal.ZERO;
		
		for (ItemCarrinho i : this.getItens()) {
			
			if (i.isDescontavel()) {
				valor = valor.add(i.valorTotal());
			}
			
		}
		
		return valor;
	}
	
	/**
	 * Calcula o quantidade descontável de produtos dos 
	 * itens já adicionados.
	 * 
	 * @return qtd descontável.
	 * @throws RegraDeNegocioException caso não seja possível verificar se o 
	 * produto é ou não descontável.
	 */
	public BigDecimal calculaQtdDescontavel() throws RegraDeNegocioException {
		BigDecimal qtdDescontavel = BigDecimal.ZERO;
		
		for (ItemCarrinho item : this.getItens()) {
			if (item.isDescontavel()) {
				qtdDescontavel = qtdDescontavel.add(item.getQtd());
			}
		}
		return qtdDescontavel;
	}
	
	/**
	 * Atualiza saldos de quantidade e valor.
	 * @throws RegraDeNegocioException 
	 */
	public void atualizaSaldos() throws RegraDeNegocioException {
		this.qtdPermitida = this.saldoDeQuantidade();
		this.valorPermitido = this.calculaSaldoDeValor();
	}

	@Override
	public void adicionaItem(ItemCarrinho item, BigDecimal saldo) throws RegraDeNegocioException 
	{
		this.add(item);
	}
}
