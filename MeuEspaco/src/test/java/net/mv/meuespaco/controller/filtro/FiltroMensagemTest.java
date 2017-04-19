package net.mv.meuespaco.controller.filtro;

import static org.junit.Assert.*;

import org.junit.Test;

public class FiltroMensagemTest {

	private FiltroMensagem filtro;
	
	@Test
	public void deveVerificarSeEstaPreenchido() 
	{
		filtro = new FiltroMensagem();
		
		assertFalse("Filtro não preenchido", filtro.isPreenchido());
		
		filtro.setCodigoCliente("000042");
		assertTrue("Filtro não preenchido", filtro.isPreenchido());
	}

}
