package net.mv.meuespaco.controller.filtro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FiltroListaProdutoTest 
{
	private FiltroListaProduto filtro;
	
	@Before
	public void setup()
	{
		filtro = new FiltroListaProdutoConsignado();
	}
	
	@Test
	public void deveVerificarAOrdenacao() 
	{
		assertEquals("Ordenacao", "ASC", FiltroListaProduto.verificaOrdem(filtro.getOrdens().get("Menor preço")));
		assertEquals("Ordenacao", "DESC", FiltroListaProduto.verificaOrdem(filtro.getOrdens().get("Maior preço")));
	}
}
