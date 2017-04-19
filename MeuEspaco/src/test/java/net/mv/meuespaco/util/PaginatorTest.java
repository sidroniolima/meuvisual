package net.mv.meuespaco.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class PaginatorTest {
	
	private Paginator paginator;

	@Test
	public void deveRetornarAQtdePartes() {
		paginator = new Paginator(21, 331);
		assertEquals("Check de qtd de partes.", 16, paginator.getPartes());
		
		paginator = new Paginator(21, 21);
		assertEquals("Check de qtd de partes.", 1, paginator.getPartes());		
		
		paginator = new Paginator(21, 8);
		assertEquals("Check de qtd de partes.", 1, paginator.getPartes());				
	}
	
	@Test
	public void deveRetornarPartesVisiveis() {
		paginator = new Paginator(21, 1000);
		assertEquals("Check de qtd de partes.", 15, paginator.partesVisiveis());
		
		paginator = new Paginator(21, 21);
		assertEquals("Check de qtd de partes.", 1, paginator.partesVisiveis());	
	}
	
	@Test
	public void deveFazerONext() {
		paginator = new Paginator(10, 100);
		
		assertEquals("Check do firs result", 0, paginator.getFirstResult());
		assertEquals("Check do firs last", 9, paginator.getLastResult());
		
		paginator.next();
		
		assertEquals("Check do firs result", 10, paginator.getFirstResult());
		assertEquals("Check do firs last", 19, paginator.getLastResult());
		
		paginator.next();
		
		assertEquals("Check do firs result", 20, paginator.getFirstResult());
		assertEquals("Check do firs last", 29, paginator.getLastResult());
		
		paginator.next();
		
		assertEquals("Check do firs result", 30, paginator.getFirstResult());
		assertEquals("Check do firs resulastlt", 39, paginator.getLastResult());
	}
	
	@Test
	public void deveFazerOGoTo() {
		paginator = new Paginator(10, 100);
		
		assertEquals("Check do firs result", 0, paginator.getFirstResult());
		assertEquals("Check do firs last", 9, paginator.getLastResult());
		
		paginator.goTo(0);
	
		assertEquals("Check do firs result", 0, paginator.getFirstResult());
		assertEquals("Check do firs last", 9, paginator.getLastResult());

		paginator.goTo(3);
		
		assertEquals("Check do firs result", 30, paginator.getFirstResult());
		assertEquals("Check do firs last", 39, paginator.getLastResult());
		
		paginator.goTo(5);
		
		assertEquals("Check do firs result", 50, paginator.getFirstResult());
		assertEquals("Check do firs last", 59, paginator.getLastResult());	
		
		paginator.goTo(1);
		
		assertEquals("Check do firs result", 10, paginator.getFirstResult());
		assertEquals("Check do firs last", 19, paginator.getLastResult());
			
	}
	
	@Test
	public void deveExibirTodosOsRegistrosEmOrdemCrescente() {
		paginator = new Paginator(21, 21);
		
		Map<Integer, Integer> registros = new HashMap<Integer, Integer>();
		
		int i = 0;
		
		for (i = 0; i < 21; i++) 
		{
			registros.put(i,0);
		}
		
		i = 0;
		
		while (i < 21) 
		{
			System.out.println(String.format("Percorrendo... primeiro registro %s / último registro %s", 
					paginator.getFirstResult(), paginator.getLastResult()));
			
			for (int j = paginator.getFirstResult(); j <= paginator.getLastResult(); j++) 
			{
				registros.replace(j, 1);
				i++;
			}
			
			paginator.next();
		}

		for (i = 0; i < 21; i++) 
		{
			assertTrue("Check do indíce " + i + " como percorrido.", 1 == registros.get(i));
		}
		
	}
	
	@Test
	public void deveExibirTodosOsRegistrosEmOrdemDecrescente() {
		paginator = new Paginator(10, 100);
		
		Map<Integer, Integer> registros = new HashMap<Integer, Integer>();
		int i;
		
		for (i = 0; i < 100; i++) 
		{
			registros.put(i,0);
		}
		
		paginator.goTo(9);
		i = 99;
		
		while (i > 0) 
		{
			System.out.println(String.format("Percorrendo... primeiro registro %s / último registro %s", 
					paginator.getFirstResult(), paginator.getLastResult()));
			
			for (int j = paginator.getFirstResult(); j <= paginator.getLastResult(); j++) {
				registros.replace(j, 1);
				i--;
			}
			
			paginator.previous();
		}

		for (i = 0; i < 100; i++) 
		{
			assertTrue("Check do indíce " + i + " como percorrido.", 1 == registros.get(i));
		}
		
	}

	@Test
	public void devePercorrerUmaListaInteiraEmOrdeDecrescente() {
		
		paginator = new Paginator(10, 100);
		
		List<Integer> lista = new ArrayList<Integer>();
		
		for (int i = 0; i < 100; i++) 
		{
			lista.add(i);
		}
		
		List<Integer> subLista = new ArrayList<Integer>();
		List<Integer> provaReal = new ArrayList<Integer>();
		
		paginator.goTo(9);
		
		while (paginator.getFirstResult() > 0) 
		{
			subLista = lista.subList(paginator.getFirstResult(), paginator.getLastResult()+1);
			
			provaReal.addAll(subLista);
			
			paginator.previous();
		}

		assertTrue("Check da prova real.", lista.containsAll(provaReal));
		
		provaReal.forEach(System.out::println);
	}
	
	@Test
	public void devePercorrerUmaListaInteiraEmOrdeCrescente() {
		
		paginator = new Paginator(10, 100);
		
		List<Integer> lista = new ArrayList<Integer>();
		
		for (int i = 0; i < 100; i++) 
		{
			lista.add(i);
		}
		
		List<Integer> subLista = new ArrayList<Integer>();
		List<Integer> provaReal = new ArrayList<Integer>();
		
		while (paginator.getLastResult() < 99) 
		{
			subLista = lista.subList(paginator.getFirstResult(), paginator.getLastResult()+1);
			
			provaReal.addAll(subLista);
			
			paginator.next();
		}

		assertTrue("Check da prova real.", lista.containsAll(provaReal));
		
		provaReal.forEach(System.out::println);
	}
	
	@Test
	public void deveIrParaAUltimaParte(){
		
		paginator = new Paginator(10, 100);
		paginator.goToLast();
		
		assertEquals("Check do firs result", 90, paginator.getFirstResult());
		assertEquals("Check do firs last", 99, paginator.getLastResult());
		
		paginator = new Paginator(21, 331);
		paginator.goToLast();
		
		assertEquals("Check do firs result", 315, paginator.getFirstResult());
		assertEquals("Check do firs last", 330, paginator.getLastResult());
		
		paginator = new Paginator(21, 8);
		paginator.goToLast();
		
		assertEquals("Check do firs result", 0, paginator.getFirstResult());
		assertEquals("Check do firs last", 7, paginator.getLastResult());
	}
	
	@Test
	public void deveInformarSeEhParticionado() {
		
		paginator = new Paginator(10, 100);
		assertFalse(paginator.isParticionado());
		
		paginator = new Paginator(21, 800);
		assertTrue(paginator.isParticionado());
		
		paginator = new Paginator(21, 8);
		assertFalse(paginator.isParticionado());
		
		paginator = new Paginator(21, 378);
		assertTrue(paginator.isParticionado());
	}
	
	@Test
	public void deveResetar(){
		paginator = new Paginator(21, 331);
		paginator.goToLast();
		
		assertEquals("Check do firs result", 315, paginator.getFirstResult());
		assertEquals("Check do firs last", 330, paginator.getLastResult());
		
		paginator.reset();
		
		assertEquals("Check do firs result", 0, paginator.getFirstResult());
		assertEquals("Check do firs last", 20, paginator.getLastResult());
	}
	
	@Test
	public void deveCriarComDadosDoPage()
	{
		paginator = new Paginator(10);
		
		assertEquals("Page.", 0, paginator.getPage());
		assertEquals("Total de Pages.", 0, paginator.getTotalPages());
	}
	
	@Test
	public void deveGoToPage()
	{
		paginator = new Paginator(10);
		
		paginator.setTotalDeRegistros(100);
		paginator.setTotalPages(10);
		
		assertEquals("Page.", 0, paginator.getPage());
		assertEquals("Total de Pages.", 10, paginator.getTotalPages());
		
		paginator.goToPage(10);
		
		assertEquals("Page.", 10, paginator.getPage());
		assertEquals("Total de Pages.", 10, paginator.getTotalPages());		
	}
}
