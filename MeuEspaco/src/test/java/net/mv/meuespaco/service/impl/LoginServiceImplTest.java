package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.model.Usuario;

public class LoginServiceImplTest {

	private Usuario usuario;

	@Before
	public void setup()
	{
		usuario = new Usuario(1L, "00001");
	}
	
	@Test
	public void deveDiferenciarLogins() {
		assertTrue(new Usuario(1L, "000002").equals(usuario));
		assertFalse(new Usuario(2L, "000002").equals(usuario));
	}

}
