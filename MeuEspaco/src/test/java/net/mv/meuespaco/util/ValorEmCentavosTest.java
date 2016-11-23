package net.mv.meuespaco.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class ValorEmCentavosTest {

	private BigDecimal valorEmReal = new BigDecimal("15.99");
	private float valorEmCentavos = 1599;
	
	@Test
	public void deveConverterParaCentavos() 
	{
		BigDecimal valorConvertido = ValorEmCentavos.toCentavos(valorEmReal);
		assertEquals("Valor em centavos.", "1599", valorConvertido.toString());
	}
	
	@Test
	public void deveConverterParaReal() 
	{
		BigDecimal valorConvertido = ValorEmCentavos.toReal(valorEmCentavos);
		
		assertTrue("Valor em Real.", valorEmReal.compareTo(valorConvertido) == 0);
	}

}
