package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.GradeUnica;

public class ItemResgateTest {

	private ItemResgate item;
	
	@Test
	public void deveValidarOItem() 
	{
		item = new ItemResgate(1L, new ResgateBrinde(), new Produto(), BigDecimal.ONE, BigDecimal.ONE, new GradeUnica());
		
		try 
		{
			item.valida();
		} catch (RegraDeNegocioException e) 
		{
			fail("O item deveria estar válido.");
		}
	}
	
	@Test
	public void deveInvalidarOItem() 
	{
		item = new ItemResgate(1L, null, new Produto(), BigDecimal.ONE, BigDecimal.ONE, new GradeUnica());
		
		try 
		{
			item.valida();
			fail("O item deveria estar INválido.");
			
		} catch (RegraDeNegocioException e) 
		{
			assertTrue(e.getMessage().contains("resgate"));
		}
		
		item = new ItemResgate(1L, new ResgateBrinde(), null, BigDecimal.ONE, BigDecimal.ONE, new GradeUnica());
		
		try 
		{
			item.valida();
			fail("O item deveria estar INválido.");
			
		} catch (RegraDeNegocioException e) 
		{
			assertTrue(e.getMessage().contains("brinde"));
		}
		
		item = new ItemResgate(1L, new ResgateBrinde(), new Produto(), BigDecimal.ZERO, BigDecimal.ONE, new GradeUnica());
		
		try 
		{
			item.valida();
			fail("O item deveria estar INválido.");
			
		} catch (RegraDeNegocioException e) 
		{
			assertTrue(e.getMessage().contains("zero"));
		}
		
		item = new ItemResgate(1L, new ResgateBrinde(), new Produto(), BigDecimal.ONE, BigDecimal.ONE, null);
		
		try 
		{
			item.valida();
			fail("O item deveria estar INválido.");
			
		} catch (RegraDeNegocioException e) 
		{
			assertTrue(e.getMessage().contains("grade"));
		}
	}
	
	@Test
	public void deveCalcularOTotal()
	{
		item = new ItemResgate(1L, new ResgateBrinde(), new Produto(), BigDecimal.ONE, BigDecimal.TEN, new GradeUnica());
		assertTrue("Valor", item.valorTotal().equals(BigDecimal.TEN));
	}

}
