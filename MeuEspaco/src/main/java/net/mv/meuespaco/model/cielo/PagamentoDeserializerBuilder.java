package net.mv.meuespaco.model.cielo;

/**
 * Fábrica para criação de instâncias de PagamentoDeserializer, 
 * para Credit e Debit.
 * 
 * @author sidronio
 * @created 07/11/2016
 */
public class PagamentoDeserializerBuilder {

	/**
	 * Cria a instâcia de acordo com o tipo.
	 * 
	 * @param type
	 * @return
	 */
	public PagamentoDeserializer cria(PaymentType type)
	{
		if (type.equals(PaymentType.CreditCard))
		{
			return new PagamentoCreditDeserializer();
		}
		
		if (type.equals(PaymentType.DebitCard))
		{
			return new PagamentoDebitDeserializer();
		}
		
		return null;
	}
}
