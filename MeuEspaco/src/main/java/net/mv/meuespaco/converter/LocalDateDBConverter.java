
package net.mv.meuespaco.converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Conversor da classe LocalDate dao Java 8 para a Classe Date 
 * do pacote java.util.Date para ser persistido no banco.
 */
@Converter
public class LocalDateDBConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate attribute) {

		if (null == attribute) {
			
			return null;
		}
		
		Instant instant = attribute.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		
		return Date.from(instant);
	}

	@Override
	public LocalDate convertToEntityAttribute(Date dbData) {
		
		if (null == dbData) {
			
			return null;
		}
		
		Instant instant = Instant.ofEpochMilli(dbData.getTime());
		
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}

}
