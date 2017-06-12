package net.mv.meuespaco.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class EncryptorTest {

	@Test
	public void deveEncriptar() 
	{
		String text = "sidronio";
		
		String encriptada = Encryptor.encrypt(text);
		
		assertFalse(encriptada == null);
		System.out.println(encriptada);
	}
	
	
	@Test
	public void deveDecriptar() 
	{
		String text = "c2lkcm9uaW8";
		
		String decriptada = Encryptor.decrypt(text);
		
		assertEquals(decriptada, "sidronio");
		System.out.println(decriptada);
	}

}
