package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.cielo.Brand;
import net.mv.meuespaco.model.cielo.Card;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.CreditPayment;
import net.mv.meuespaco.model.cielo.Customer;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.cielo.Payment;
import net.mv.meuespaco.model.cielo.PaymentType;
import net.mv.meuespaco.service.IntegracaoCieloService;

public class IntegracaoCieloServiceImplTest {

	private IntegracaoCieloService integracaoSrvc = new IntegracaoCieloServiceImpl();
	private Pagamento pagamento;
	private Customer customer;
	private Card creditCard;
	private Payment payment;
	
	@Before
	public void setup()
	{
		customer = new Customer("Sidronio");

		integracaoSrvc.init();
	}
	
	@Test
	public void deveAprovarVendaDeCredito() throws CieloException, IntegracaoException 
	{
		creditCard = new Card("0000000000000001", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Aceita - Cód. 4", new String("4"), resposta.getPayment().getReturnCode());
		assertTrue("Transação Autorizada", resposta.isAutorizado());
	}
	
	@Test(expected=CieloException.class)
	public void naoDeveAprovarVendaDeCredito() throws CieloException, IntegracaoException 
	{
		creditCard = new Card("0000000000000002", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 2", new String("2"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test
	public void naoDeveAprovarVendaDeCredito_TimeOut() throws CieloException, IntegracaoException 
	{
		creditCard = new Card("0000000000000009", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertTrue("Transação ora não ora autorizada - Cód. 4/99", 
				resposta.getPayment().getReturnCode().contains("4") || 
				resposta.getPayment().getReturnCode().contains("99"));
	}
	
	@Test(expected=CieloException.class)
	public void naoDeveAprovarVendaDeCredito_CartaoCancelado() throws CieloException, IntegracaoException 
	{
		creditCard = new Card("0000000000000007", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 77", new String("77"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test(expected=CieloException.class)
	public void naoDeveAprovarVendaDeCredito_Problemas_Com_O_Cartao() throws CieloException, IntegracaoException 
	{
		creditCard = new Card("0000000000000008", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 70", new String("70"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test(expected=CieloException.class)
	public void naoDeveAprovarVendaDeCredito_Cartao_Bloqueado() throws IntegracaoException, CieloException 
	{
		creditCard = new Card("0000000000000005", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 78", new String("78"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test(expected=CieloException.class)
	public void naoDeveAprovarVendaDeCredito_Cartao_Expirado() throws CieloException, IntegracaoException 
	{
		creditCard = new Card("0000000000000003", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta = integracaoSrvc.efetuaPagamento(pagamento);
		
		assertEquals("Transação Não autorizada - Cód. 57", new String("57"), resposta.getPayment().getReturnCode());
		assertTrue("Transação NÃO Autorizada", !resposta.isAutorizado());
	}
	
	@Test
	public void deveGerarErrorNoCredito_Numero_Cartao() throws IntegracaoException
	{
		creditCard = new Card("000000000000000000000000003", "Teste Holder", "09/2017", "123", Brand.Visa);
		payment = new CreditPayment(15000, 1, creditCard);
		pagamento = new Pagamento("365547", customer, payment);
		
		Pagamento resposta;
		
		try 
		{
			resposta = integracaoSrvc.efetuaPagamento(pagamento);
			fail("Deveria ter lançado a exceção.");
		} catch (CieloException e) {
			
		}
		
	}
}
