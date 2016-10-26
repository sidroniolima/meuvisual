package net.mv.meuespaco.model;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;

public class DepartamentoTest {

	@Test
	public void valueOfTest() {
		
		Departamento dep1 = Departamento.valueOf("INFANTIL");
		Departamento dep2 = Departamento.valueOf("FEMININO");
		Departamento dep3 = Departamento.valueOf("MASCULINO");
		Departamento dep4 = Departamento.valueOf("DIA_DAS_MAES");
		Departamento dep5 = Departamento.valueOf("DIA_DOS_NAMORADOS");
		
		assertEquals("Infantil", dep1.getDescricao());
		assertEquals("Feminino", dep2.getDescricao());
		assertEquals("Masculino", dep3.getDescricao());
		assertEquals("Dia das mães", dep4.getDescricao());
		assertEquals("Dia dos namorados", dep5.getDescricao());
	}
	
	@Test(expected=RegraDeNegocioException.class)
	public void deveLancarExcecaoSeNaoTiverOValor() throws RegraDeNegocioException
	{
		try {
			Departamento dep = Departamento.valueOf("INFANTILSSSS");
		} catch(IllegalArgumentException ex)
		{
			throw new RegraDeNegocioException("Não foi possível listar produtos para o departamento selecionado.");
		}
	}

}
