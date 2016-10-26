package net.mv.meuespaco.converter;

import java.time.LocalDate;

import org.junit.Test;

public class LocalDateFacesConverterTest {

	private LocalDateFacesConverter localDateFacesConverter = new LocalDateFacesConverter();
	
	@Test
	public void deveConverterDeStringParaDate() {
		
		String strData = "01/07/2016";
		LocalDate data = (LocalDate) localDateFacesConverter.getAsObject(null, null, strData);
	}
	
	@Test
	public void deveConverterDeDateParaString() {
		
		LocalDate data = LocalDate.now();
		
		String strData = (String) localDateFacesConverter.getAsString(null, null, data);
		
	}

}
