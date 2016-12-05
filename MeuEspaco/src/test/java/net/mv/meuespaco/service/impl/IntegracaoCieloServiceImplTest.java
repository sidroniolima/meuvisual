package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.cielo.Brand;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.CreditCard;
import net.mv.meuespaco.model.cielo.Customer;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.cielo.Payment;
import net.mv.meuespaco.model.cielo.PaymentType;
import net.mv.meuespaco.service.IntegracaoCieloService;

public class IntegracaoCieloServiceImplTest {

	private IntegracaoCieloService integracaoSrvc = new IntegracaoCieloServiceImpl();
	private Pagamento pagamento;
	private Customer customer;
	private CreditCard creditCard;
	private Payment payment;
	
	@Before
	public void setup()
	{
		customer = new Customer("Sidronio");

		integracaoSrvc.init();
	}
	
	@Test
	public void deveAprovarVenda() throws CieloException, IntegracaoException 
	{
		creditCard = new CreditCard("0000000000000001", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Pagamento Confirmado - Status 2 ", 2, resposta.getPayment().getStatus());
		assertTrue("Transação Autorizada", resposta.isAutorizado());
	}
	
	@Test
	public void naoDeveAprovarVenda() throws CieloException, IntegracaoException 
	{
		creditCard = new CreditCard("0000000000000002", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 2", new String("2"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test
	public void naoDeveAprovarVenda_TimeOut() throws CieloException, IntegracaoException 
	{
		creditCard = new CreditCard("000000000000009", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertTrue("Transação ora não ora autorizada - Cód. 4/99", 
				resposta.getPayment().getReturnCode().contains("4") || 
				resposta.getPayment().getReturnCode().contains("99"));
	}
	
	@Test
	public void naoDeveAprovarVenda_CartaoCancelado() throws CieloException, IntegracaoException 
	{
		creditCard = new CreditCard("0000000000000007", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 77", new String("77"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test
	public void naoDeveAprovarVenda_Problemas_Com_O_Cartao() throws CieloException, IntegracaoException 
	{
		creditCard = new CreditCard("0000000000000008", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 70", new String("70"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test
	public void naoDeveAprovarVenda_Cartao_Bloqueado() throws CieloException, IntegracaoException 
	{
		creditCard = new CreditCard("0000000000000005", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 78", new String("78"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test
	public void naoDeveAprovarVenda_Cartao_Expirado() throws CieloException, IntegracaoException 
	{
		creditCard = new CreditCard("0000000000000003", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 57", new String("57"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test
	public void deveGerarError_Numero_Cartao() throws IntegracaoException
	{
		creditCard = new CreditCard("000000000000000000000000003", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta;
		
		try 
		{
			resposta = integracaoSrvc.efetuaPagamento(pagamento);
			fail("Deveria ter lançado a exceção.");
		} catch (CieloException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void deveConsultarUmPagamento() throws CieloException, IntegracaoException
	{
		creditCard = new CreditCard("0000000000000001", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 1390, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		Pagamento consulta = integracaoSrvc.consultaPagamento(resposta.paymentId());

		assertEquals("Deve verificar a Consulta", resposta.getPayment().getStatus(), consulta.getPayment().getStatus()); 
		assertEquals("Deve verificar a Consulta", resposta.getPayment().getProofOfSale(), consulta.getPayment().getProofOfSale());
		assertEquals("Deve verificar a Consulta", resposta.getPayment().gettId(), consulta.getPayment().gettId());
		assertEquals("Deve verificar a Consulta", resposta.getPayment().getAuthorizationCode(), consulta.getPayment().getAuthorizationCode());
		assertEquals("Deve verificar a Consulta", resposta.getPayment().getCreditCard().getCardNumber(), consulta.getPayment().getCreditCard().getCardNumber());
		assertEquals("Deve verificar a Consulta", resposta.getCustomer().getName(), consulta.getCustomer().getName());
	}
	
	@Test
	public void deveFornecerADataDeRecebimentoDoPagamento() throws IntegracaoException
	{
		creditCard = new CreditCard("0000000000000001", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta;
		
		try 
		{
			resposta = integracaoSrvc.efetuaPagamento(pagamento);

			assertFalse("Data de recebimento.", resposta.getPayment().getRecievedDate().isEmpty());
			assertFalse("Data de recebimento.", null == resposta.horarioDoPagamento());
			
		} catch (CieloException e) {
			fail("Compra OK.");
		}
	}
	
	@Test
	public void deveCancelarUmaVenda() throws IntegracaoException, CieloException, RegraDeNegocioException
	{
		creditCard = new CreditCard("0000000000000001", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new Payment(PaymentType.CreditCard, 15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		Payment cancelamento = integracaoSrvc.cancelaCompra(resposta.paymentId(), new BigDecimal(150));
		assertTrue("Cancelamento Ok", cancelamento.isCancelamentoEfetuado());
	}
	
}
