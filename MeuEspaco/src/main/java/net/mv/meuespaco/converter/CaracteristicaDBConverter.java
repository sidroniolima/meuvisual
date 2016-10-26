package net.mv.meuespaco.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import net.mv.meuespaco.model.Caracteristica;

@Converter
public class CaracteristicaDBConverter implements AttributeConverter<Caracteristica, String>{

	@Override
	public String convertToDatabaseColumn(Caracteristica attribute) {
		return attribute.toString();
	}

	@Override
	public Caracteristica convertToEntityAttribute(String dbData) {
		return Caracteristica.valueOf(dbData);
	}

}
