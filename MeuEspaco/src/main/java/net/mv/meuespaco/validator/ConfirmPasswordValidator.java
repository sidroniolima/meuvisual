package net.mv.meuespaco.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("confirmPasswordValidator")
public class ConfirmPasswordValidator implements Validator{

	@Override
	public void validate(FacesContext context, UIComponent component, Object value)
			throws ValidatorException {
		
		UIInput passwordComponent = (UIInput) component.getAttributes().get("passwordComponent");

		String password = (String) passwordComponent.getValue();
		
		String confirm = (String) value;
		
		if (!password.equals(confirm)) {
            throw new ValidatorException(new FacesMessage("As senhas não coincidem."));
        }
		
	}
	
}
