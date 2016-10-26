package net.mv.meuespaco.converter;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor da classe LocalDate dao Java 8 para a Classe Date 
 * @author Sidronio
 *
 */
@FacesConverter("localDateFacesConverter")
public class LocalDateFacesConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String stringValue) {
		
		if (null == stringValue || stringValue.isEmpty()) {
			return null;
		}
		
		LocalDate localDate = null; 
		
		try {
			localDate = LocalDate.parse(
						stringValue.trim(), 
						DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception e) {
			localDate = LocalDate.parse(
					stringValue.trim(), 
					DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		
		return localDate;
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object localDateValue) {
		
		if (null == localDateValue) {

			return "";
		}
		
		return ((LocalDate) localDateValue).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}


}
