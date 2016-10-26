package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import net.mv.meuespaco.model.Permissao;

/**
 * Conversor para a permissão, necessário na inclusão, 
 * alteração e exclusão no cadastro de Usuario.
 * 
 * @author Sidronio
 * 05/01/2016
 */
@FacesConverter("permissaoConverter")
public class PermissaoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return Permissao.valueOf(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null == value) {
			return "";
		}
		
		return value.toString();
	}

}
