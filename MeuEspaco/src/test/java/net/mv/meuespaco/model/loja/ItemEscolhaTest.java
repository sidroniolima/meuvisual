package net.mv.meuespaco.model.loja;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.GradeUnica;

public class ItemEscolhaTest 
{
	List<ItemEscolha> itens;
	Produto brinco = new Produto(1L, "Brinco", "000001", new BigDecimal(20));
	Produto anel = new Produto(2L, "Anel", "000002", new BigDecimal(15));
	Produto corrente = new Produto(3L, "Corrente", "000003", new BigDecimal(50));
	
	@Before
	public void before()
	{
		itens = new ArrayList<ItemEscolha>();
		
		itens.add(new ItemEscolha(brinco, BigDecimal.ONE,  new BigDecimal(20), new GradeUnica()));
		itens.add(new ItemEscolha(anel, new BigDecimal(3),  new BigDecimal(15), new GradeUnica()));
		itens.add(new ItemEscolha(corrente, BigDecimal.ONE,  new BigDecimal(50), new GradeUnica()));
	}
	
	@Test
	public void deveGerarOToString() 
	{
		assertTrue("To string correta", itens.get(0).toString().contains("000001;1;20.00"));
	}

}
