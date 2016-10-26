package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import net.mv.meuespaco.model.Caracteristica;

@FacesConverter("caracteristicaConverter")
public class CaracteristicaConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return Caracteristica.valueOf(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null == value) {
			return "";
		}
		
		String valor = value.toString();
		
		return valor == null ? "" : valor;
	}

}
