package net.mv.meuespaco.model.cielo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class PagamentoTest {

	private final String respostaOk = 
			"{"
			+ "\"MerchantOrderId\":\"365547\","
			+ "\"Customer\":"
			+ "	{"
			+ "		\"Name\":\"Sidronio\""
			+ "	},"
			+ "\"Payment\":"
			+ "	{"
			+ "		\"ServiceTaxAmount\":0,"
			+ "		\"Installments\":1,"
			+ "		\"Interest\":0,"
			+ "		\"Capture\":false,"
			+ "		\"Authenticate\":false,"
			+ "		\"Recurrent\":false,"
			+ "		\"CreditCard\":"
			+ "			{"
			+ "				\"CardNumber\":\"000000******0001\","
			+ "				\"Holder\":\"Teste Holder\","
			+ "				\"ExpirationDate\":\"09/2017\","
			+ "				\"SaveCard\":false,"
			+ "				\"Brand\":\"Visa\""
			+ "			},"
			+ "		\"Tid\":\"1014082150250\","
			+ "		\"ProofOfSale\":\"2150250\","
			+ "		\"AuthorizationCode\":\"332571\","
			+ "		\"Provider\":\"Simulado\","
			+ "		\"PaymentId\":\"fbf27534-e289-4f7e-a7bb-f49bb4f4e321\","
			+ "		\"Type\":\"CreditCard\","
			+ "		\"Amount\":15000,"
			+ "		\"ReceivedDate\":\"2016-10-14 08:21:50\","
			+ "		\"Currency\":\"BRL\","
			+ "		\"Country\":\"BRA\","
			+ "		\"ReturnCode\":\"4\","
			+ "		\"ReturnMessage\":\"Operation Successful\","
			+ "		\"Status\":2,"
			+ "		\"Links\":["
			+ "					{\"Method\":\"GET\",\"Rel\":\"self\",\"Href\":\"https://apiquerysandbox.cieloecommerce.cielo.com.br/1/sales/fbf27534-e289-4f7e-a7bb-f49bb4f4e321\"},"
			+ "					{\"Method\":\"PUT\",\"Rel\":\"capture\",\"Href\":\"https://apisandbox.cieloecommerce.cielo.com.br/1/sales/fbf27534-e289-4f7e-a7bb-f49bb4f4e321/capture\"},"
			+ "					{\"Method\":\"PUT\",\"Rel\":\"void\",\"Href\":\"https://apisandbox.cieloecommerce.cielo.com.br/1/sales/fbf27534-e289-4f7e-a7bb-f49bb4f4e321/void\"}"
			+ "				]"
			+ "		}"
			+ "	}";
	
	private Pagamento pagamento;
	private Customer customer;
	private Payment payment;
	private CreditCard creditCard;
	
	@Before
	public void init()
	{

	}
	
	@Test
	public void deveGerarOJsonDoPagamento() 
	{
		customer = new Customer("Sidronio");
		creditCard = new CreditCard("1234123412341234", "Teste Holder", "09/2016", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		String pagamentoJson = pagamento.converterToJson();
		
		assertTrue("Json gerado", pagamentoJson.length() > 0);
		
		System.out.println(pagamentoJson);
	}
	
	@Test
	public void deveConverterDeJsonParaObjetoResposta() 
	{
		Pagamento resposta = new Gson().fromJson(respostaOk, Pagamento.class);
		
		assertFalse("Resposta não null", null == resposta);
		
		System.out.println(resposta);
		
		assertEquals("Status da resposta", 2, resposta.getPayment().getStatus());
	}
	
	@Test
	public void deveFornecerADataDoPagamento()
	{
		Pagamento resposta = new Gson().fromJson(respostaOk, Pagamento.class);
		assertEquals("Data do pagamento", LocalDateTime.parse("2016-10-14T08:21:50"), resposta.horarioDoPagamento());
	}
}
