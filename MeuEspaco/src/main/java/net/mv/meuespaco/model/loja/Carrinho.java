package net.mv.meuespaco.model.loja;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Grupo;

/**
 * Abstração de carrinho utilizado para armazenar itens na 
 * escolha e itens para venda. O carrinho possui um resumo 
 * com a quantidade de itens agrupados (pelo grupo).
 * 
 * @author Sidronio
 * @date 08/08/2016
 */
public abstract class Carrinho {

	private List<ItemCarrinho> itens = new ArrayList<ItemCarrinho>();
	private Map<Grupo, BigDecimal> resumo = new HashMap<Grupo, BigDecimal>();
	
	private BigDecimal desconto;
	
	/**
	 * Construtor padrão.
	 */
	public Carrinho() {	
		this.desconto = BigDecimal.ZERO;
	}
	
	/**
	 * Adiciona um item ao carrinho validando-o e verificando o saldo se
	 * necessário, tanto de quantidade como de valor.
	 * 
	 * @param item
	 * @throws RegraDeNegocioException
	 */
	public abstract void adicionaItem(ItemCarrinho item) throws RegraDeNegocioException; 
	
	/**
	 * Adiciona um item ao carrinho validando-o, passando o saldo e verificando
	 * a quantidade.
	 * 
	 * @param item
	 * @param saldo disponível para o cliente.
	 * @throws RegraDeNegocioException
	 */
	public abstract void adicionaItem(ItemCarrinho item, BigDecimal saldo) throws RegraDeNegocioException; 
	
	/**
	 * Calcula a quantidade de itens.
	 * 
	 * @return qtd de itens.
	 */
	public BigDecimal qtdDeItens() {
		return this.getItens().stream().map(i -> i.getQtd()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula o valor dos itens.
	 * 
	 * @return valor dos itens.
	 */
	public BigDecimal valorDosItens() 
	{
		return this.getItens()
				.stream()
				.map(i -> i.valorTotal())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula o valor dos itens com desconto.
	 * 
	 * @return valor dos itens com desconto.
	 */
	public BigDecimal valorComDesconto() {
		return this.getItens()
				.stream()
				.map(i -> i.valorTotal())
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.subtract(this.valorDoDesconto());
	}
	
	/**
	 * Calcula o valor do desconto.
	 * 
	 * @return
	 */
	public BigDecimal valorDoDesconto()
	{
		return this.valorDosItens()
						.multiply(
								this.desconto
								.divide(new BigDecimal(100)));
	}
	
	/**
	 * Adiciona um item ao carrinho caso seja válido e exista saldo suficiente.
	 * 
	 * @param item escolhido.
	 * @throws RegraDeNegocioException lança exceção caso seja inválido.
	 */
	protected void add(ItemCarrinho item) throws RegraDeNegocioException 
	{	
		Optional<ItemCarrinho> itemJaAdicionado = this.jaExisteOItem(item);
		
		if (itemJaAdicionado.isPresent()) {
			
			itemJaAdicionado.get().atualizaQtd(item.getQtd());
			item = itemJaAdicionado.get();
			
		} else {
			
			itens.add(item);
		}
		
		resumo.merge(item.getGrupo(), item.getQtd(), BigDecimal::add);
	}
	
	/**
	 * Remove um item do carrinho se existente.
	 * 
	 * @param item a ser removido.
	 * @throws RegraDeNegocioException lança excecção se não houver o item.
	 */
	public void removeItem(ItemCarrinho itemParaRemocao) throws RegraDeNegocioException {
		Optional<ItemCarrinho> item = this.jaExisteOItem(itemParaRemocao);

		if (!item.isPresent()) 
		{
			throw new RegraDeNegocioException("Não é possível remover este item pois não pertence ao carrinho.");
		}
		
		ItemCarrinho itemLocalizado = item.get();
		
		this.getItens().remove(itemLocalizado);
		resumo.merge(itemLocalizado.getGrupo(), itemLocalizado.getQtd(), BigDecimal::subtract);
		
	}
	
	/**
	 * Verifica se já existe este item no carrinho.
	 * 
	 * @param item adicionado.
	 * @return se existe ou não no carrinho.
	 */
	public Optional<ItemCarrinho> jaExisteOItem(ItemCarrinho item) {
		
		Optional<ItemCarrinho> itemExistente = 
				this.getItens().stream().filter(i -> i.equals(item)).findFirst();
		
		return itemExistente;
	}
	
	/**
	 * Esvazia o carrinho.
	 */
	public void esvazia()
	{
		this.getItens().clear();
		this.desconto = BigDecimal.ZERO;
		this.resumo.clear();
	}
	
	/**
	 * Verifica se o carrinho está vazio.
	 * 
	 * @return
	 */
	public boolean isVazio()
	{
		return this.getItens().isEmpty();
	}

	public List<ItemCarrinho> getItens() {
		return itens;
	}

	public Map<Grupo, BigDecimal> getResumo() {
		return resumo;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}
}
