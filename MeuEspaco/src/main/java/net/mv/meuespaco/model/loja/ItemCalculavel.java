package net.mv.meuespaco.model.loja;

import java.math.BigDecimal;

import net.mv.meuespaco.exception.RegraDeNegocioException;

public interface ItemCalculavel {
	
	/**
	 * Calcula o valor dos itens baseado na quantidade e 
	 * no valor unitário.
	 * 
	 * @return valor total do item.
	 */
	public BigDecimal valorTotal();
	
	/**
	 * Verifica se o item é descontável ou não, isto é, se ele é 
	 * utilizado no cálculo de valor e quantidade para informar o saldo.
	 * 
	 * @return se é ou não descontável.
	 * @throws RegraDeNegocioException caso não seja possível definir.
	 */
	public boolean isDescontavel() throws RegraDeNegocioException;
}
