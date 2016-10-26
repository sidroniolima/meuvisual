package net.mv.meuespaco.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.util.DataDoSistema;

public class SemanaTest {

	private Semana semana;
	private DataDoSistema dataFalsa = Mockito.mock(DataDoSistema.class);
	
	@Test
	public void deveVerificarSeOCicloEstaAberto() 
	{
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 16));
		semana = new Semana(1L, LocalDate.of(2016, 10, 10), LocalDate.of(2016, 10, 16), dataFalsa);
		assertEquals("Check que o ciclo está aberto.", semana.isCicloAberto(), true);
	}
	
	@Test
	public void deveVerificarQueOCicloEstaFechado() 
	{
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 17));
		semana = new Semana(1L, LocalDate.of(2016, 10, 10), LocalDate.of(2016, 10, 16), dataFalsa);
		assertEquals("Check que o ciclo está fechado.", semana.isCicloAberto(), false);
	}

	@Test
	public void deveVerificarQuantosDiasAtehOProximoCiclo() 
	{
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 05));
		
		semana = new Semana(1L, LocalDate.of(2016, 10, 10), LocalDate.of(2016, 10, 16), dataFalsa);
		semana.setDiasEntreCiclos(35);
		
		assertEquals("Check dos dias até o próximo ciclo.", semana.diasAteOProximoCiclo(), 05);
		
	}
	
	@Test
	public void deveCalcularADataDoProximoCiclo_ComAtualFechado() 
	{
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 05));
		
		semana = new Semana(1L, LocalDate.of(2016, 10, 10), LocalDate.of(2016, 10, 16), dataFalsa);
		semana.setDiasEntreCiclos(35);
		
		assertFalse("Check que o ciclo está fechado.", semana.isCicloAberto());
		
		assertEquals("Check dos dias até o próximo ciclo.", semana.dataDoProximoCiclo(), LocalDate.of(2016, 10, 10));
	}
	
	@Test
	public void deveCalcularADataDoProximoCiclo_ComAtualAberto() 
	{
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 15));
		
		semana = new Semana(1L, LocalDate.of(2016, 10, 10), LocalDate.of(2016, 10, 16), dataFalsa);
		semana.setDiasEntreCiclos(35);
		
		assertTrue("Check que o ciclo está fechado.", semana.isCicloAberto());
		
		assertEquals("Check dos dias até o próximo ciclo.", semana.dataDoProximoCiclo(), LocalDate.of(2016, 11, 20));
	}
}
