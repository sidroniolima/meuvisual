package net.mv.meuespaco.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.cielo.Card;

/**
 * Validator do número do cartão de crédito.
 * 
 * @author sidronio
 * @created 25/10/2016
 */
@FacesValidator("creditCardNumberValidator")
public class CreditCardNumberValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException 
	{
		String cardNumber = (String) value;
		cardNumber = cardNumber.replace(".", "");
		
		Card creditCar = new Card(cardNumber);
		
		try 
		{
			creditCar.validateNumber();
		} catch (RegraDeNegocioException e) {
			throw new ValidatorException(new FacesMessage(e.getMessage()));
		}
	}

}
