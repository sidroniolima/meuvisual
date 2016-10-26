package net.mv.meuespaco.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtil {

	public static void addSuccessMessage(String message) 
	{
		if (null == FacesContext.getCurrentInstance())
		{
			return;
		}
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						message, message)); 
	}
	
	public static void addErrorMessage(String message) 
	{
		if (null == FacesContext.getCurrentInstance())
		{
			return;
		}
		
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR,
						message, message)); 
	}
	
}
