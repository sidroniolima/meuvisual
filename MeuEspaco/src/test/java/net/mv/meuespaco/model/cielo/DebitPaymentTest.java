package net.mv.meuespaco.model.cielo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DebitPaymentTest {

	private final String jsonDebit = "{"
			+ "\"MerchantOrderId\": \"000061\","
			+ "\"Customer\": {"
			+ "    \"Name\": \"Sidronio Lima\""
			+ "  },"
			+ "  \"Payment\": {"
			+ "    \"Type\": \"DebitCard\","
			+ "    \"Amount\": 15099,"
			+ "    \"ReturnUrl\": \"http://www.meuvisualsemijoias.com/private/site/index.xhtml\","
			+ "    \"DebitCard\": {"
			+ "      \"CardNumber\": \"0000000000000001\","
			+ "      \"Holder\": \"Teste Holder\","
			+ "      \"ExpirationDate\": \"09/2017\","
			+ "      \"SecurityCode\": \"123\","
			+ "      \"Brand\": \"Visa\""
			+ "    }"
			+ "  }"
			+ "}";
	
	private final String respostaJson = "{\"DebitCard\":"
			+ "	{\"CardNumber\":\"000000******0001\","
			+ "	\"Holder\":\"Teste Holder\","
			+ "	\"ExpirationDate\":\"09/2017\","
			+ "	\"SaveCard\":false,"
			+ "	\"Brand\":\"Visa\"},"
			+ "	\"Provider\":\"Simulado\","
			+ "	\"AuthenticationUrl\":\"https://authorizationmocksandbox.cieloecommerce.cielo.com.br/CardAuthenticator/Receive/3326599c-feef-4a89-9295-e6d1b36f12d2\","
			+ "	\"Tid\":\"1107121347299\","
			+ "	\"ProofOfSale\":\"1347299\","
			+ "	\"PaymentId\":\"3326599c-feef-4a89-9295-e6d1b36f12d2\","
			+ "	\"Type\":\"DebitCard\","
			+ "	\"Amount\":15099,"
			+ "	\"ReceivedDate\":\"2016-11-07 12:13:47\","
			+ "	\"Currency\":\"BRL\","
			+ "	\"Country\":\"BRA\","
			+ "	\"ReturnUrl\":\"http://www.meuvisualsemijoias.com/private/site/index.xhtml\","
			+ "	\"ReturnCode\":\"1\","
			+ "	\"Status\":0,"
			+ "	\"Links\":["
			+ "	{"
			+ "		\"Method\":\"GET\",\"Rel\":\"self\","
			+ "		\"Href\":\"https://apiquerysandbox.cieloecommerce.cielo.com.br/1/sales/3326599c-feef-4a89-9295-e6d1b36f12d2\"}]}";	
	
	private DebitPayment payment;
	private Card card;
	
	@Before
	public void init()
	{
		card = new Card("0000000000000001", "HOLDER", "05/2017", Brand.Visa);
		payment = new DebitPayment(150.99f, card);
	}
	
	@Test
	public void deveCriarOPagamentoViaDebito() 
	{
		assertEquals("Return Url", "http://www.meuvisualsemijoias.com/private/site/index.xhtml", payment.getReturnUrl());
		assertEquals("Amount", 150.99f, payment.getAmount(), 0.0f);
	}
	
	@Test
	public void deveSerializar()
	{
		String json = payment.toJson();

		assertTrue("Type", json.contains("\"Type\": \"DebitCard\""));
		assertTrue("Amount", json.contains("\"Amount\": 15099"));
		assertTrue("Amount", json.contains("\"ReturnUrl\": \"http://www.meuvisualsemijoias.com/private/site/index.xhtml\""));
	}
	
	@Test
	public void deveDeserializar()
	{
		DebitPayment debit = new DebitPayment().fromJson(respostaJson);
		
		assertEquals("Amount", 150.99f, debit.getAmount(), 0.00f);
		assertEquals("Amount", 150.99f, debit.getAmount(), 0.00f);
		assertTrue("Auth Url", debit.getAuthenticationUrl().contains("https://authorizationmocksandbox.cieloecommerce.cielo.com.br/CardAuthenticator/Receive/"));
		assertEquals("Proof", "1347299", debit.getProofOfSale());
		assertEquals("PayId", 150.99f, debit.getAmount(), 0.00f);
		assertEquals("Type", 150.99f, debit.getAmount(), 0.00f);
	}

}

