package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import net.mv.meuespaco.model.loja.Cpf;
import net.mv.meuespaco.model.loja.Documento;

/**
 * Conveter da entidade Cpf.
 * 
 * @author Sidronio
 * 21/12/2015
 */
@FacesConverter("cpfFacesConverter")
public class CpfFacesConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return new Cpf(value.toString());
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null != value) {
			
			return (value == null ? "" : ((Documento)value).getValor());
		}
		
		return "";
	}

}
