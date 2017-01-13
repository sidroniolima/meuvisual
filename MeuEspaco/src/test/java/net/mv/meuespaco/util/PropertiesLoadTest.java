package net.mv.meuespaco.util;

import static org.junit.Assert.*;

import org.junit.Test;

import net.mv.meuespaco.service.impl.PropertiesLoad;

public class PropertiesLoadTest {

	PropertiesLoad props = new PropertiesLoad();
	
	@Test
	public void deveCarregarOArquivo() 
	{
		assertFalse("Carregou property.", props.getProperty("merchant_id").isEmpty());
	}

	
	@Test
	public void deveCarregarUmaPropriedade() 
	{
		assertEquals("Carregou property environment.", "local", props.getProperty("environment"));
	}
}
