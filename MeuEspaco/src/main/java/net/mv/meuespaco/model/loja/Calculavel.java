package net.mv.meuespaco.model.loja;

import java.math.BigDecimal;
import java.util.List;

import net.mv.meuespaco.exception.RegraDeNegocioException;

/**
 * Abstração das classes que utilizam os itens 
 * para cálculo dos valores totais e dos descontáveis.
 * 
 * @author Sidronio
 * 24/05/2016
 */
public interface Calculavel {

	/**
	 * Calcula o valor dos itens da Escolha.
	 * 
	 * @return
	 */
	public default BigDecimal valorDosItens() {
		
		return this.getItens()
				.stream()
				.map(i -> i.valorTotal())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula o valor dos itens descontáveis utilizado 
	 * nos cálculos para limite de peças escolhidas.
	 * 
	 * @return Valor dos itens descontáveis.
	 * @throws RegraDeNegocioException Lança uma exceção caso naõ seja possível verificar 
	 * se o produto é ou não descontável.
	 */
	public default BigDecimal valorDosItensDescontaveis() throws RegraDeNegocioException {
		
		BigDecimal soma = BigDecimal.ZERO;
		
		for (ItemCalculavel item : this.getItens()) 
		{
			if (item.isDescontavel()) 
			{
				soma = soma.add(item.valorTotal());
			}
		}
		
		return soma;
	}
	
	/**
	 * Retorna os itens calculáveis para os cálculos.
	 * 
	 * @return itens.
	 */
	public List<ItemCalculavel> getItens();
	
}
