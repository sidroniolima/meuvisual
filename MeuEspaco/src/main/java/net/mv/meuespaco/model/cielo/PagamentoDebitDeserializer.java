package net.mv.meuespaco.model.cielo;

public class PagamentoDebitDeserializer extends PagamentoDeserializer {

	@Override
	public Payment getPayment() 
	{
		return new DebitPayment();
	}

}
