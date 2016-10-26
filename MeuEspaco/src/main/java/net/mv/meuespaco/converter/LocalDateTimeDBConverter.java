package net.mv.meuespaco.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Conversor da classe LocalDate dao Java 8 para a Classe Date 
 * do pacote java.util.Date para ser persistido no banco.
 * 
 * @author Sidronio
 *
 */
@Converter
public class LocalDateTimeDBConverter implements AttributeConverter<LocalDateTime, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDateTime attribute) {

		if (null == attribute) {
			
			return null;
		}
		
		Instant instant = attribute.atZone(ZoneId.systemDefault()).toInstant();
		
		return Date.from(instant);
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Date dbData) {
		
		if (null == dbData) {
			
			return null;
		}
		
		Instant instant = Instant.ofEpochMilli(dbData.getTime());
		
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
	}

}
