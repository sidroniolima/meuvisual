package net.mv.meuespaco.model.consulta;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.loja.Departamento;

public class MenuPorDepartamentoTest 
{
	@Test
	public void test() 
	{
		List<MenuPorDepartamento> menus = Arrays.asList(
			new MenuPorDepartamento(new BigInteger("1"), "Infantil", new BigInteger("1"), "Aneis", new BigInteger("1"), "Tradicional"),
			new MenuPorDepartamento(new BigInteger("1"), "Infantil", new BigInteger("2"), "Brincos", new BigInteger("2"), "2 Furo"),
			new MenuPorDepartamento(new BigInteger("1"), "Infantil", new BigInteger("3"), "Pingentes", new BigInteger("3"), "Fashion"),
			new MenuPorDepartamento(new BigInteger("1"), "Infantil", new BigInteger("3"), "Pingentes", new BigInteger("8"), "Tradicional"),
			new MenuPorDepartamento(new BigInteger("1"), "Infantil", new BigInteger("3"), "Pingentes", new BigInteger("9"), "Moderno"),
			new MenuPorDepartamento(new BigInteger("2"), "Masculino",new BigInteger("1"), "Aneis", new BigInteger("4"), "Dourado"),
			new MenuPorDepartamento(new BigInteger("2"), "Masculino",new BigInteger("5"), "Correntes", new BigInteger("5"), "Prata"),
			new MenuPorDepartamento(new BigInteger("3"), "Feminino", new BigInteger("1"), "Aneis", new BigInteger("6"), "Novela"),
			new MenuPorDepartamento(new BigInteger("3"), "Feminino", new BigInteger("1"), "Aneis", new BigInteger("7"), "Aço")
		);
		
		Map<Departamento, Map<Grupo, Map<Subgrupo, List<MenuPorDepartamento>>>> menuConstruido = 
				MenuPorDepartamento.constroiMenu(menus);

		menuConstruido.entrySet().stream().forEach(
				a -> {
					System.out.println(a.getKey().toString());
					
					a.getValue().entrySet().stream().forEach(b -> {
						System.out.println("\t" + b.getKey().toString());
						
						b.getValue().entrySet().stream().forEach(c -> {
							System.out.println("\t\t" + c.getKey().toString());
						});
					});
				}
			);
		
		menuConstruido.get(
				new Departamento(1L, "Infantil")).get(
						new Grupo(3L, "Pingente")).entrySet().stream().forEach(x -> System.out.println(x.getKey()));

		Set<Subgrupo> verificacao = new HashSet<Subgrupo>();
		verificacao.addAll(Arrays.asList(
				new Subgrupo(3L, "Fashion"), 
				new Subgrupo(8L, "Tradicional"), 
				new Subgrupo(9L, "Moderno")));
				
		assertTrue("Itens Ok", 
				menuConstruido.get(
						new Departamento(1L, "Infantil"))
						.get(new Grupo(3L, "Pingente"))
						.keySet()
						.equals(verificacao));
	}

}
