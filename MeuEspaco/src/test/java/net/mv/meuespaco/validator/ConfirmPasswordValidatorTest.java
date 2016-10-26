package net.mv.meuespaco.validator;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConfirmPasswordValidatorTest {

	private FacesContext facesContext = Mockito.mock(FacesContext.class);
	private UIComponent uiComponent = Mockito.mock(UIComponent.class);
	
	ConfirmPasswordValidator confirmPasswordValidator = new ConfirmPasswordValidator();
	Map<String, Object> arvoreDeComponentes = new HashMap<String, Object>();
	
	UIInput inputPassowrd = new UIInput();
	
	@Before
	public void init()
	{
		arvoreDeComponentes.put("passwordComponent", inputPassowrd);
	}
	
	@Test(expected=ValidatorException.class)
	public void deveInvalidar() 
	{
		inputPassowrd.setValue("senha-diferente");
		
		when(uiComponent.getAttributes()).thenReturn(arvoreDeComponentes);
		confirmPasswordValidator.validate(facesContext, uiComponent, "senha");
	}

	@Test
	public void deveValidar() 
	{
		inputPassowrd.setValue("senha");
		
		when(uiComponent.getAttributes()).thenReturn(arvoreDeComponentes);
		confirmPasswordValidator.validate(facesContext, uiComponent, "senha");
	}
}
