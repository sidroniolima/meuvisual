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
 * Valida na View a data de expiração do cartão.
 * 
 * @author sidronio
 * @created 25/10/2016
 */
@FacesValidator("creditCardExpirationDateValidator")
public class CreditCardExpirationDateValidator implements Validator 
{
	private Card creditCard;

	public CreditCardExpirationDateValidator() {	}
	
	public CreditCardExpirationDateValidator(Card creditCard) 
	{
		this();
		this.creditCard = creditCard;
	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException 
	{
	
		String expDate = (String) value;
		
		try 
		{
			this.getCreditCard(expDate).validateExpirationDate();
		} catch (RegraDeNegocioException e) 
		{
			throw new ValidatorException(new FacesMessage(e.getMessage()));
		}
	}
	
	/**
	 * Cria um cartão de crédito para validar.
	 * 
	 * @param expirationDate
	 * @return
	 */
	protected Card getCreditCard(String expirationDate)
	{
		if (null == creditCard)
		{
			return new Card("", expirationDate);
		}
		
		return this.creditCard;
	}

}
