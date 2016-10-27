package net.mv.meuespaco.validator;


import java.time.LocalDate;

import javax.faces.validator.ValidatorException;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import net.mv.meuespaco.model.cielo.Card;
import net.mv.meuespaco.util.DataDoSistema;

public class CreditCardExpirationDateValidatorTest {

	private String dataInvalida = "13/2045";
	private String dataVencida = "09/2016";
	private String dataValida = "10/2016";
	
	private DataDoSistema dataFalsa = Mockito.mock(DataDoSistema.class);

	@Test
	public void deveInvalidarAData() 
	{
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 30));
		
		CreditCardExpirationDateValidator validator = 
				new CreditCardExpirationDateValidator(new Card("", dataInvalida, dataFalsa));
		
		try
		{
			validator.validate(null, null, dataInvalida);
			fail("Data inválida");
		} catch (ValidatorException e) {
			assertEquals("Mensagem correta de data inválida.", "O mês ou ano de vencimento é inválido.", e.getMessage());
		}
		
	}
	
	@Test
	public void deveValidarAData() 
	{
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 30));
		
		CreditCardExpirationDateValidator validator = 
				new CreditCardExpirationDateValidator(new Card("", dataValida, dataFalsa));
		
		try
		{
			validator.validate(null, null, dataValida);
		} catch (ValidatorException e) {
			fail("Data válida");
		}
		
	}
	
	@Test
	public void deveInvalidarADataVencida() 
	{
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 30));
		
		CreditCardExpirationDateValidator validator = 
				new CreditCardExpirationDateValidator(new Card("", dataVencida, dataFalsa));
		
		try
		{
			validator.validate(null, null, dataVencida);
			fail("Data inválida");
		} catch (ValidatorException e) {
			assertEquals("O cartão está com data de expiração vencida.", e.getMessage());
		}
		
	}

}
