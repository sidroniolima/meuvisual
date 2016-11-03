package net.mv.meuespaco.model.cielo;

public class PagamentoCreditDeserializer extends PagamentoDeserializer
{

	@Override
	public Payment getPayment() 
	{
		return new CreditPayment();
	}

}
