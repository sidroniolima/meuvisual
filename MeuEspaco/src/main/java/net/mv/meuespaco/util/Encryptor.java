package net.mv.meuespaco.util;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Classe utilitária de encriptografar e criptografar utilizando 
 * o algoritmo MD5.
 * 
 * @author sidronio
 * @created 07/07/2017
 */
public class Encryptor 
{

	/**
	 * Encode a text.
	 * 
	 * @param text
	 * @return
	 */
	public static String encrypt(String text)
	{
		byte[] encoded = Base64.getEncoder().encode(text.getBytes());
		return new String(encoded, Charset.forName("UTF-8"));
	}
	
	/**
	 * Decode a text.
	 * 
	 * @param text
	 * @return
	 */
	public static String decrypt(String token)
	{
		byte [] decoded = Base64.getDecoder().decode(token); 
		return new String(decoded, Charset.forName("UTF-8"));
	}
}
