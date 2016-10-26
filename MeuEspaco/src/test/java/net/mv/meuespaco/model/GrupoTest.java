package net.mv.meuespaco.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class GrupoTest {

	@Test
	public void deveIndicarSeEhOuNaoDescontavel() {
		
		Grupo grupoDescontavel = new Grupo(1L, "Brinco");
		
		Grupo grupoMatApoio = new Grupo(1L, "Embalagem");
		grupoMatApoio.setDescontavel(false);
		
		assertTrue(grupoDescontavel.isDescontavel());
		assertFalse(grupoMatApoio.isDescontavel());
	}

}
