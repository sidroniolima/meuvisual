package net.mv.meuespaco.model.loja;

import java.math.BigDecimal;

import net.mv.meuespaco.exception.RegraDeNegocioException;

/**
 * Implementação do carrinho utilizado para venda. Nele não 
 * existe limite para quantidade ou valor.
 * 
 * @author Sidronio
 * @date 08/08/2016
 */
public class CarrinhoVenda extends Carrinho {
	
	public CarrinhoVenda() {
		super();
	}

	@Override
	public void adicionaItem(ItemCarrinho item) throws RegraDeNegocioException {
		item.valida();
		super.add(item);
	}

	@Override
	public void adicionaItem(ItemCarrinho item, BigDecimal saldo) throws RegraDeNegocioException 
	{
		this.add(item);
	}

}
