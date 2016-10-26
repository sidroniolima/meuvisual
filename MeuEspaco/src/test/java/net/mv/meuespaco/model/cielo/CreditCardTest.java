package net.mv.meuespaco.model.cielo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.util.DataDoSistema;

public class CreditCardTest {

	private CreditCard creditCard_2;
	private CreditCard creditCard_1;
	private CreditCard creditCard_3;
	private CreditCard creditCard_4;
	private CreditCard creditCard_5;
	private CreditCard creditCard_6;
	private CreditCard creditCard_7;
	
	private DataDoSistema dataFalsa = Mockito.mock(DataDoSistema.class);
	private CreditCard cartaoMaster;
	private CreditCard cartaoVisa;
	private CreditCard cartaoDiners;
	private CreditCard cartaoDiscover;
	private CreditCard cartaoJCB;
	private CreditCard cartaoAura;
	
	@Before
	public void init()
	{
		creditCard_1 = new CreditCard("5162445578415641", "12/2000", dataFalsa);
		creditCard_2 = new CreditCard("1111444455551112", "13/2016", dataFalsa);
		creditCard_3 = new CreditCard("7451234871","11/2017", dataFalsa);
		creditCard_4 = new CreditCard("5162203955588846","11/2016", dataFalsa);
		creditCard_5 = new CreditCard("51622039555888469","10/2016", dataFalsa);
		creditCard_6 = new CreditCard("51622039","07/2016", dataFalsa);
		creditCard_7 = new CreditCard("5162203955588846","09/2016", dataFalsa);
		
		cartaoMaster = new CreditCard("5162203955588846","09/2016", dataFalsa);
		cartaoVisa = new CreditCard("4162203955588846","09/2016", dataFalsa);
		cartaoDiners = new CreditCard("38022039555888","09/2016", dataFalsa);
		cartaoDiscover = new CreditCard("6011203955588846","09/2016", dataFalsa);
		cartaoJCB = new CreditCard("3562203955588846","09/2016", dataFalsa);
		cartaoAura = new CreditCard("5062203955588846","09/2016", dataFalsa);
	}
	
	@Test
	public void deveRetornarODigito() 
	{	
		assertEquals("Dígito verificador.", 1, creditCard_1.getDigit());
		assertEquals("Dígito verificador.", 2, creditCard_2.getDigit());
		assertEquals("Dígito verificador.", 1, creditCard_3.getDigit());
		assertEquals("Dígito verificador.", 6, creditCard_4.getDigit());
	}
	
	@Test 
	public void deveVerificarSeONumeroEhValido()
	{
		try 
		{
			assertTrue("Válido", creditCard_4.validateNumber());
		} catch (RegraDeNegocioException e) {
			fail("Cartão válido.");
		}
		
		try 
		{
			assertFalse("Inválido", creditCard_5.validateNumber());
			fail("Cartão inválido.");
		} catch (RegraDeNegocioException e) {
			
		}
	}

	@Test 
	public void deveVerificarQueEhInvalido()
	{
		try 
		{
			assertFalse("Válido", creditCard_1.validateNumber());
			assertFalse("Válido", creditCard_2.validateNumber());
			assertFalse("Válido", creditCard_3.validateNumber());
			assertFalse("Válido", creditCard_5.validateNumber());
			assertFalse("Válido", creditCard_6.validateNumber());
			
			fail("Cartão inválido.");
		} catch (RegraDeNegocioException e) {
			
		}
	}
	
	@Test
	public void deveVerificarADataDeExpiracao()
	{
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 30));
		
		try 
		{
			assertTrue("Válido", creditCard_3.validateExpirationDate());
			assertTrue("Válido", creditCard_4.validateExpirationDate());
			assertTrue("Válido", creditCard_5.validateExpirationDate());
	
		} catch (RegraDeNegocioException e) {
			System.out.println(e.getMessage());
			fail("Datas válidas.");
		}
		
		try 
		{
			assertFalse("Inválido", creditCard_1.validateExpirationDate());
			assertFalse("Inválido", creditCard_2.validateExpirationDate());
			assertFalse("Inválido", creditCard_6.validateExpirationDate());
			
			fail("Datas inválidas.");
		} catch (RegraDeNegocioException e) {
			
		}
	}
	
	@Test
	public void deveValidarOCartao()
	{
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 30));
		
		try 
		{
			assertTrue("Válido", creditCard_4.isValid());
			
		} catch (RegraDeNegocioException e) {
			fail("Cartão válido.");
		}
		
		try 
		{
			assertFalse("Inválido", creditCard_7.isValid());
			
			fail("Cartão inválido.");
		} catch (RegraDeNegocioException e) {
			
		}		
	}
	
	@Test
	public void deveTestarOPadrao()
	{
		assertEquals("Teste Visa.", Brand.Visa, cartaoVisa.getBrand());
		assertEquals("Teste Master.", Brand.Master, cartaoMaster.getBrand());
		assertEquals("Teste Diners.", Brand.Diners, cartaoDiners.getBrand());
		assertEquals("Teste Discover.", Brand.Discover, cartaoDiscover.getBrand());
		assertEquals("Teste JCB.", Brand.JCB, cartaoJCB.getBrand());
		assertEquals("Teste Aura.", Brand.Aura, cartaoAura.getBrand());
	}
	
}
