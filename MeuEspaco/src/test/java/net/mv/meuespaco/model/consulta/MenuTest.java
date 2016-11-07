package net.mv.meuespaco.model.consulta;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;

public class MenuTest {

	private Menu menu = new Menu();
	Grupo anel;
	Grupo brinco;
	Grupo corrente;
	
	@Before
	public void configure() {
		
		anel = new Grupo(1L, "Anéis");
		brinco = new Grupo(2L, "Brincos");
		corrente = new Grupo(3L, "Correntes");
		
		Subgrupo argola = new Subgrupo(1L, "Argola");
		Subgrupo anelSolitario = new Subgrupo(2L, "Solitário/Falange/Berloque");
		Subgrupo anelFino = new Subgrupo(4L, "Fino/Médio/Aparador");
		Subgrupo veneziana = new Subgrupo(3L, "Veneziana");
		
		anel.getSubgrupos().add(anelFino);	
		anel.getSubgrupos().add(anelSolitario);	
		brinco.getSubgrupos().add(argola);
		corrente.getSubgrupos().add(veneziana);
	}
	
	@Test
	public void deveMontarOMenu() {
		
		menu.montaMenu(Departamento.FEMININO, anel);
		menu.montaMenu(Departamento.FEMININO, brinco);
		menu.montaMenu(Departamento.MASCULINO, corrente);
		System.out.println(menu.getMenus());
		//assertEquals("Tamanho do menu", 8, menu.getMenus().size());
	}

}
