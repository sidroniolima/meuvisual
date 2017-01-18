package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cupom;
import net.mv.meuespaco.service.CupomService;

/**
 * Teste unitário da camada Service da entidade Cupom.
 * 
 * @author Sidronio
 * @create 10/09/2016
 */
public class CupomServiceImplTest {

	CupomService cupomSrvc;
	
	@Before
	public void init()
	{
		Cliente clienteLogado = new Cliente("1980");
		clienteLogado.setSenha("1980");
		
		cupomSrvc = new CupomServiceImpl(clienteLogado);
	}
	
	@Test
	public void verificaSeOCupomEhGenerico() {
		String codigoCupom = "1980";
		
		assertTrue(cupomSrvc.ehCupomGenerico(codigoCupom));
	}

	@Test
	public void deveCriarUmCupomGenerico()
	{
		String codigoCupom = "1980";
		
		Cupom cupom = cupomSrvc.buscaCupom(codigoCupom);
		
		assertTrue(null != cupom);
	}
	
	@Test
	public void naoDeveCriarUmCupomQueNaoExista()
	{
		String codigoCupom = "CUPOM";
		
		Cupom cupom = cupomSrvc.buscaCupom(codigoCupom);
		
		assertTrue(null == cupom);
	}
	
	@Test
	public void deveRetornarODescontoDoCupomGenerico() throws RegraDeNegocioException
	{
		String codigoCupom = "1980";
		
		BigDecimal desconto = cupomSrvc.descontoDoCupom(codigoCupom);
		assertEquals("Desconto", new BigDecimal(42), desconto);
	}
	
	@Test(expected=RegraDeNegocioException.class)
	public void naoDeveRetornarODescontoDeCupomQueNaoExista() throws RegraDeNegocioException
	{
		String codigoCupom = "CUPOM";
		
		cupomSrvc.descontoDoCupom(codigoCupom);
	}
}
