package net.mv.meuespaco.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Classe utilitária para conversão de datas nos foramtos LocalDate e LocalDateTime para
 * Date do pacote java.util.
 * 
 * @author Sidronio
 * @created 14/07/2016
 */
public class UtilDateTimeConverter {

	/**
	 * Converte de LocalDate para Date (java.util.datetime)
	 * @param data
	 * @return
	 */
	public static Date toDate(LocalDate data)
	{
		Instant instant = data.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}
	
	/**
	 * Converte de LocalDateTime para Date (java.util.date)
	 * 
	 * @param dataEHora
	 * @return
	 */
	public static Date toDateTime(LocalDateTime dataEHora)
	{
		Instant instant = dataEHora.atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}
	
}
