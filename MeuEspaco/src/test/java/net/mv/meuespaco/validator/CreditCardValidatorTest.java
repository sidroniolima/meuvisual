package net.mv.meuespaco.validator;

import static org.junit.Assert.*;

import javax.faces.validator.ValidatorException;

import org.junit.Test;

public class CreditCardValidatorTest {

	private String value_1;
	private String value_2;
	private String value_3;
	
	private CreditCardNumberValidator validator = new CreditCardNumberValidator();
	private String value_4;
	
	@Test
	public void deveValidarOsValores() {
		
		value_1 = "5162445578415641";
		value_2 = "51622039";
		value_3 = "5162203955588846";
		value_4 = "5162.2039.5558.8846";
		
		try
		{
			validator.validate(null, null, value_1);
			fail("Cartão 1 válido");
		} catch (ValidatorException e) {

		}
		

		try
		{
			validator.validate(null, null, value_2);
			fail("Cartão 2 inválido");
		} catch (ValidatorException e) {
			
		}

		try
		{
			validator.validate(null, null, value_3);
			
		} catch (ValidatorException e) {
			fail("Cartão 3 inválido");
			
		}
		
		try
		{
			validator.validate(null, null, value_4);
			
		} catch (ValidatorException e) {
			fail("Cartão 4 válido");
		}
		
	}

}
