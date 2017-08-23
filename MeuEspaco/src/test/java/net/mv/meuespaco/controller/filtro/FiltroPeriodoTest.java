package net.mv.meuespaco.controller.filtro;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class FiltroPeriodoTest {

	private FiltroPeriodo filtro;
	
	@Test
	public void deveVerificarSeEstaPreenchido() 
	{
		filtro = new FiltroPeriodo();
		assertFalse("Nao preenchido", filtro.isPreenchido());
		
		filtro.setDataInicial(LocalDate.now());
		assertTrue("Preenchido", filtro.isPreenchido());
		
		filtro.setDataFinal(LocalDate.now());
		assertTrue("Preenchido", filtro.isPreenchido());
		
		filtro.setDataInicial(null);
		assertFalse("Nao preenchido", filtro.isPreenchido());


	}

}
