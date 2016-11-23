package net.mv.meuespaco.model.cielo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;

public class PaymentTest {

	private Payment payment;
	private final String respostaCancelamento = "{"+
			    "\"Status\": 10,"+
			    "\"ReturnCode\": \"9\","+
			    "\"ReturnMessage\": \"Operation Successful\","+
			    "\"Links\": ["+
			        "{"+
			            "\"Method\": \"GET\","+
			            "\"Rel\": \"self\","+
			            "\"Href\": \"https://apiquerysandbox.cieloecommerce.cielo.com.br/1/sales/{PaymentId}\""+
			        "}"+
			    "]"+
			"}";
	
	@Test
	public void deveCriarComValorEmCentavos() 
	{
		CreditCard card = new CreditCard("0000000000000001", "07/2017");
		payment = new Payment(PaymentType.CreditCard, 1099f, 1, card);
		
		assertEquals("Valor em centavos...", 1099f, payment.getAmount(), 0.00f);
	}

	@Test
	public void deveConverterOCancelamentoParaPagamento()
	{
		payment = new Gson().fromJson(respostaCancelamento, Payment.class);
		assertTrue("Pagamento convertido.", null != payment);
		
		assertEquals("Status de retorno", "10", payment.getStatus());
		assertEquals("Código de retorno", "9", payment.getReturnCode());
		assertEquals("Mensagem de retorno", "Operation Successful", payment.getReturnMessage());
	}
	
	@Test
	public void deveIdentificarComoCancelamentoBemSucedido()
	{
		payment = new Gson().fromJson(respostaCancelamento, Payment.class);
		
		assertTrue("Cancelamento Ok.", payment.isCancelamentoEfetuado());
	}
}
