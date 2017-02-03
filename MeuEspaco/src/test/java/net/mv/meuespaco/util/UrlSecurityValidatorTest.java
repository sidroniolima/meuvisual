package net.mv.meuespaco.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import net.mv.meuespaco.filter.UrlSecurityFilter;
import net.mv.meuespaco.service.impl.PropertiesLoad;

public class UrlSecurityValidatorTest {

	private URL url;
	private URL urlComQuery;
	private URL urlComQuerySegura;
	private URL urlSegura;
	
	private UrlSecurityFilter urlValidator;
	
	private PropertiesLoad propsFalsas = Mockito.mock(PropertiesLoad.class);

	@Before
	public void setup() throws MalformedURLException
	{
		url = new URL("http://meuvisualsemijoias.com/private/site/pesquisa-produto.xhtml?paramCod=2");
		urlSegura = new URL("https://meuvisualsemijoias.com/private/site/pesquisa-produto.xhtml?paramCod=2");
		
		urlComQuery = new URL("http://meuvisualsemijoias.com/private/site/lista-produtos.xhtml?paramDep=MASCULINO&paramSubgrupo=22");
		urlComQuerySegura = new URL("https://meuvisualsemijoias.com/private/site/lista-produtos.xhtml?paramDep=MASCULINO&paramSubgrupo=22");
		
		urlValidator = new UrlSecurityFilter(propsFalsas);
	}

	@Test
	public void deveVerificarSeAUrlEhSegura_SemQuery() throws MalformedURLException 
	{
		assertFalse("URL segura", urlValidator.isSecure(url));
		
		URL urlSegura = new URL("https://meuvisualsemijoias.com/private/site/pesquisa-produto.xhtml?paramCod=2");
		assertTrue("URL segura", urlValidator.isSecure(urlSegura));
	}

	@Test
	public void deveTornarUmaURLSegura_SemQuery() throws MalformedURLException, URISyntaxException
	{
		when(propsFalsas.getProperty("server")).thenReturn("https://meuvisualsemijoias.com");
		
		URL urlSeguraCriada = new URL(urlValidator.securedUrl(url));
		
		assertTrue("URL segura", urlValidator.isSecure(urlSeguraCriada));
		
		System.out.println(urlSeguraCriada + " - " + this.urlSegura);
		
		assertTrue("URLs iguais", urlSeguraCriada.equals(this.urlSegura));
	}
	
	@Test
	public void deveTornarUmaURLSegura_ComQuery() throws MalformedURLException, URISyntaxException
	{
		when(propsFalsas.getProperty("server")).thenReturn("https://meuvisualsemijoias.com");
		
		URL urlSeguraCriada = new URL(urlValidator.securedUrl(urlComQuery));
		
		System.out.println(urlSeguraCriada + " - " + this.urlComQuerySegura);
		
		assertTrue("URL segura", urlValidator.isSecure(urlSeguraCriada));
		assertEquals("URLs iguais", this.urlComQuerySegura,urlSeguraCriada);	
	}
}
