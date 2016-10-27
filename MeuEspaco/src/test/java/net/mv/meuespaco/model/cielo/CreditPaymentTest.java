package net.mv.meuespaco.model.cielo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class CreditPaymentTest {

	private Payment payment;
	private Card creditCard;
	
	private String strJsonPayment = "{"
			  + "\"Type\": \"CreditCard\"," 
			  + "\"Installments\": 1, "
			  + "\"CreditCard\": { "
			  + "	\"CardNumber\": \"1234123412341234\","
			  + "	\"Holder\": \"Teste Holder\" ,"
			  + "	\"ExpirationDate\": \"09/2016\","
			  + " 	\"SecurityCode\": \"123\","
			  + "	\"Brand\": \"Visa\" "
			  + "	}, "
			  + "\"ProofOfSale\": \"674532\", "
			  + "\"Tid\": \"0305023644309\","
		      + "\"AuthorizationCode\": \"123456\","
		      + "\"PaymentId\": \"24bc8366-fc31-4d6c-8555-17049a836a07\","
		      + "\"Type\": \"CreditCard\","
		      + "\"Amount\": 15700,"
		      + "\"Currency\": \"BRL\","
		      + "\"Country\": \"BRA\","
		      + "\"ExtraDataCollection\": [],"
		      + "\"Status\": 1,"
		      + "\"ReturnCode\": \"4\","
		      + "\"ReturnMessage\": \"Operation Successful\""
			  + "}";
	
	@Before
	public void init()
	{
		creditCard = new Card("1234123412341234", "Teste Holder", "09/2016", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
	}
	
	@Test
	public void deveSerializar() 
	{	
		String paymentJson = payment.toJson();
		
		assertTrue("Json gerado", paymentJson.length() > 0);
		
		System.out.println(paymentJson);
	}
	
	@Test
	public void deveDeserializar() 
	{
		CreditPayment creditPayment = new CreditPayment().fromJson(strJsonPayment);
		
		assertFalse("Resposta não null", null == creditPayment);
		
		System.out.println(creditPayment);
		
		
		assertEquals("Type", "CreditCard", creditPayment.getType().toString());
		assertEquals("Amount", 157.00f, creditPayment.getAmount(), 0.0f);
		assertEquals("Installments", 1, creditPayment.getInstallments());
		
		assertEquals("CreditCard Number", "1234123412341234", creditPayment.getCard().getCardNumber());
		assertEquals("CreditCard Holder", "Teste Holder", creditPayment.getCard().getHolder());
		assertEquals("CreditCard ExpirationDate", "09/2016", creditPayment.getCard().getExpirationDate());
		assertEquals("CreditCard Brand", Brand.Visa, creditPayment.getCard().getBrand());
		
		//assertEquals("ProofOfSale", "674532", creditPayment.getProofOfSale());
		//assertEquals("AuthorizationCode", "123456", creditPayment.getAuthorizationCode());
		assertEquals("PaymentId", UUID.fromString("24bc8366-fc31-4d6c-8555-17049a836a07"), creditPayment.getPaymentId());
		assertEquals("Status", "1", creditPayment.getStatus());
		assertEquals("ReturnCode", "4", creditPayment.getReturnCode());
		assertEquals("Tid", "0305023644309", creditPayment.gettId());
		assertEquals("ReturnMessage", "Operation Successful", creditPayment.getReturnMessage());
	}
}
