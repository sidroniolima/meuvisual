package net.mv.meuespaco.model.loja;

import java.math.BigDecimal;

import net.mv.meuespaco.exception.RegraDeNegocioException;

/**
 * Carrinho de brindes.
 * 
 * @author sidronio
 * @created 02/06/2017
 */
public class CarrinhoBrinde extends Carrinho
{
	private BigDecimal valorPermitido;
	
	public CarrinhoBrinde() {	}
	
	public CarrinhoBrinde(Long pontos) 
	{
		this.valorPermitido = new BigDecimal(pontos);
	}

	@Override
	public void adicionaItem(ItemCarrinho item) throws RegraDeNegocioException 
	{
		item.valida();
		
		if (this.valorDosItens().add(item.valorTotal()).compareTo(this.valorPermitido) >= 0)
		{
			throw new RegraDeNegocioException("O cliente já atingiu o saldo de estrelas disponíveis.");
		}
		
		super.add(item);
	}
	
	/**
	 * Atualiza saldo do valor.
	 * @throws RegraDeNegocioException 
	 */
	public void atualizaSaldo() throws RegraDeNegocioException 
	{
		this.valorPermitido = this.valorPermitido.subtract(this.valorDosItens());
	}
	
	@Override
	public void adicionaItem(ItemCarrinho item, BigDecimal saldo) throws RegraDeNegocioException 
	{
		if (this.valorDosItens().add(item.valorTotal()).compareTo(saldo) >= 0)
		{
			throw new RegraDeNegocioException("O cliente já atingiu o saldo de estrelas disponíveis.");
		}
		
		this.add(item);
	}

	public BigDecimal getValorPermitido() {
		return valorPermitido;
	}
}
