package net.mv.meuespaco.model.cielo;

import static org.junit.Assert.*;

import org.junit.Test;

public class PaymentTest {

	private Payment payment;
	
	@Test
	public void deveCriarComValorEmCentavos() 
	{
		CreditCard card = new CreditCard("0000000000000001", "07/2017");
		payment = new Payment(PaymentType.CreditCard, 1099f, 1, card);
		
		assertEquals("Valor em centavos...", 1099f, payment.getAmount(), 0.00f);
	}

}
