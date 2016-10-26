package net.mv.meuespaco.converter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Conversor da classe LocalDate dao Java 8 para a Classe Date 
 * do pacote java.util.Date para ser persistido no banco.

 * @author Sidronio
 *
 */
@FacesConverter("localDateTimeFacesConverter")
public class LocalDateTimeFacesConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String stringValue) {
		
		if (null == stringValue || stringValue.isEmpty()) {
			return null;
		}
		
		LocalDateTime localDateTime = null; 
		
		localDateTime = LocalDateTime.parse(
					stringValue.trim(), 
					DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.systemDefault()));
		
		return localDateTime;
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object localDateTimeValue) {
		
		if (null == localDateTimeValue) {

			return "";
		}
		
		return ((LocalDateTime) localDateTimeValue)
				.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
						.withZone(ZoneId.systemDefault()));
	}


}
