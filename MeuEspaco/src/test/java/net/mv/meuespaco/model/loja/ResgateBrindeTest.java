package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeUnica;

public class ResgateBrindeTest {

	private ResgateBrinde resgate;
	private Produto brindeFalso = Mockito.mock(Produto.class);
	private Produto outroBrindeFalso = Mockito.mock(Produto.class);
	private Grade gradeFalsa = Mockito.mock(GradeUnica.class);
	
	@Before
	public void init()
	{
		resgate = new ResgateBrinde(new Cliente());
	}
	
	@Test
	public void deveCriarUmResgate() 
	{
		assertFalse("Horário", null == resgate.getHorario());
		assertEquals("Status", resgate.getStatus(), StatusEscolha.NOVA);
		assertFalse("Brindes", null == resgate.getBrindes());
		assertTrue("Sem brindes", resgate.getBrindes().size() == 0);
	}
	
	@Test
	public void deveInValidar()
	{
		resgate = new ResgateBrinde();
		
		try 
		{
			resgate.valida();
			fail("Deve invalidar.");
		
		} catch (RegraDeNegocioException e) 
		{
			assertTrue("Invalidada pelo cliente ser nulo.", e.getMessage().contains("cliente"));
		}
		
		resgate = new ResgateBrinde(new Cliente());
		
		try 
		{
			resgate.valida();
			fail("Deve invalidar.");
		
		} catch (RegraDeNegocioException e) 
		{
			assertTrue("Invalidada por não ter ao menos um item.", e.getMessage().contains("brinde"));
		}
		
		try 
		{
			resgate.setHorario(null);
			resgate.valida();
		
		} catch (RegraDeNegocioException e) 
		{
			assertTrue("Invalidada pelo horário ser nulo.", e.getMessage().contains("horário"));
		}
	}

	@Test
	public void deveValidar()
	{
		resgate.getBrindes().add(new ItemResgate());
		
		try 
		{
			resgate.valida();
			
		} catch (RegraDeNegocioException e) 
		{
			fail("Deve estar válida.");
		}
	}
	
	@Test
	public void deveAdicionarUmBrinde() throws RegraDeNegocioException
	{
		when(brindeFalso.valor()).thenReturn(new BigDecimal(2.50));
		
		resgate.adicionaBrinde(brindeFalso, new BigDecimal(2), new BigDecimal(2), gradeFalsa);
	}
	
	@Test
	public void deveRetornarOValorDoResgate() throws RegraDeNegocioException
	{
		when(brindeFalso.valor()).thenReturn(new BigDecimal(2.50));
		when(outroBrindeFalso.valor()).thenReturn(new BigDecimal(1.75));
		
		resgate.adicionaBrinde(brindeFalso, new BigDecimal(2), brindeFalso.valor(), gradeFalsa);	
		
		assertEquals("Valor do resgate.", resgate.valor().setScale(2), new BigDecimal(5).setScale(2));
		
		resgate.adicionaBrinde(outroBrindeFalso, BigDecimal.ONE, outroBrindeFalso.valor(), gradeFalsa);
		
		assertEquals("Valor do resgate.", resgate.valor().setScale(2), new BigDecimal(6.75).setScale(2));
	}
	
	@Test
	public void deveRetornarAQuantidadeDeItensDoResgate() throws RegraDeNegocioException
	{
		when(brindeFalso.valor()).thenReturn(new BigDecimal(2.50));
		when(outroBrindeFalso.valor()).thenReturn(new BigDecimal(1.75));
		
		resgate.adicionaBrinde(brindeFalso, new BigDecimal(2), brindeFalso.valor(), gradeFalsa);	
		
		assertEquals("Valor do resgate.", resgate.qtdDeItens(), new BigDecimal(2));
		
		resgate.adicionaBrinde(outroBrindeFalso, BigDecimal.ONE, outroBrindeFalso.valor(), gradeFalsa);
		
		assertEquals("Valor do resgate.", resgate.qtdDeItens(), new BigDecimal(3));		
	}
	
	@Test
	public void deveCalcularOSaldoAnterior() throws RegraDeNegocioException
	{
		resgate = new ResgateBrinde(new Cliente(), 1000L);
		
		when(brindeFalso.valor()).thenReturn(new BigDecimal(2.50));
		when(outroBrindeFalso.valor()).thenReturn(new BigDecimal(1.75));
		
		resgate.adicionaBrinde(brindeFalso, new BigDecimal(2), brindeFalso.valor(), gradeFalsa);			
		resgate.adicionaBrinde(outroBrindeFalso, BigDecimal.ONE, outroBrindeFalso.valor(), gradeFalsa);
		
		assertTrue("Saldo Anterior.", resgate.getSaldoAnterior().equals(1000L));
	}
	
	@Test
	public void deveCalcularOSaldoPosterior() throws RegraDeNegocioException
	{
		resgate = new ResgateBrinde(new Cliente(), 1000L);
		
		when(brindeFalso.valor()).thenReturn(new BigDecimal(200));
		when(outroBrindeFalso.valor()).thenReturn(new BigDecimal(330));
		
		resgate.adicionaBrinde(brindeFalso, new BigDecimal(2), brindeFalso.valor(), gradeFalsa);			
		resgate.adicionaBrinde(outroBrindeFalso, BigDecimal.ONE, outroBrindeFalso.valor(), gradeFalsa);
		
		assertEquals("Saldo Posterior.", resgate.getSaldoPosterior().longValue(), 270, 0.00);
	}

}
