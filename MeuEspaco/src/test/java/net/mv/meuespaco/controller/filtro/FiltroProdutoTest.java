package net.mv.meuespaco.controller.filtro;

import static org.junit.Assert.*;

import org.junit.Test;

import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Subgrupo;

public class FiltroProdutoTest {

	private FiltroProduto filtro;
	
	@Test
	public void deveVerificarSeEstaPreenchido() 
	{
		filtro = new FiltroProduto();
		
		assertFalse("Nao preenchido.", filtro.isPreenchido());
		
		filtro.setDescricao("MV");
		filtro.setComposicao(Composicao.BP);
		assertTrue("Preenchido.", filtro.isPreenchido());
		
		filtro.setSubgrupo(new Subgrupo());
		assertTrue("Preenchido.", filtro.isPreenchido());
	}

}
