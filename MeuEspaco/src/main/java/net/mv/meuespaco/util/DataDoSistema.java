package net.mv.meuespaco.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Classe para fornecer a data atual do sistema, 
 * utilizada nos testes.
 * 
 * @author sidronio
 * @created 16/10/2016
 */
public class DataDoSistema {

	/**
	 * Retorna a data de hoje do sistema.
	 * 
	 * @return
	 */
	public LocalDate hoje()
	{
		return LocalDate.now();
	}

	/**
	 * Retorna o momento atual.
	 * 
	 * @return
	 */
	public LocalDateTime agora() {
		return LocalDateTime.now();
	}
}
