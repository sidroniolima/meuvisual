package net.mv.meuespaco.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Transforma de Valor em Real para centavos.
 * 
 * @author sidronio
 * @created 23/11/2016
 */
public class ValorEmCentavos {

	/**
	 * Transforma do valor em Real para Centavos.
	 * 
	 * @param valorEmReal
	 * @return
	 */
	public static BigDecimal toCentavos(BigDecimal valorEmReal)
	{
		return valorEmReal.multiply(new BigDecimal(100))
				.setScale(0, RoundingMode.HALF_UP);
	}
	
	/**
	 * Transforma do valor em Centavos para Real.
	 * 
	 * @param valorEmCentavos
	 * @return
	 */
	public static BigDecimal toReal(float valorEmCentavos)
	{
		return new BigDecimal(valorEmCentavos / 100).setScale(2, RoundingMode.HALF_UP);
	}
}
