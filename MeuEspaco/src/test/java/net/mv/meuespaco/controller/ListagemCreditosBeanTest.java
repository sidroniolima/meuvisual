package net.mv.meuespaco.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.integracao.ClasseCredito;
import net.mv.meuespaco.model.integracao.Credito;
import net.mv.meuespaco.model.integracao.ListagemCreditos;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.CreditoService;
import net.mv.meuespaco.util.DataDoSistema;

public class ListagemCreditosBeanTest {
	
	private ListagemCreditosBean creditosBean;
	
	private CreditoService creditoSrvcFalso;
	private Cliente cliente;
	
	private DataDoSistema relogioFalso;
	
	private ListagemCreditos listagemFalsa;

	public ListagemCreditosBeanTest() 
	{
		List<Credito> creditos = new ArrayList<Credito>();
		creditos.add(new Credito(new Cliente(1L, "Sidronio"), 12.65, "MARIA DAS GRAÇAS", ClasseCredito.COM, "", LocalDate.now()));
		creditos.add(new Credito(new Cliente(1L, "Sidronio"), 12.65, "MARIA DAS GRAÇAS", ClasseCredito.COM, "", LocalDate.now()));
		creditos.add(new Credito(new Cliente(1L, "Sidronio"), 12.65, "MARIA DAS GRAÇAS", ClasseCredito.COM, "", LocalDate.now()));
		creditos.add(new Credito(new Cliente(1L, "Sidronio"), 12.65, "MARIA DAS GRAÇAS", ClasseCredito.COM, "", LocalDate.now()));
		
		listagemFalsa = new ListagemCreditos(creditos);
	}
	
	@Before
	public void setUp()
	{
		creditoSrvcFalso = Mockito.mock(CreditoService.class);
		cliente = new Cliente(1L, "Sidronio");
		relogioFalso = Mockito.mock(DataDoSistema.class);
		
		creditosBean = new ListagemCreditosBean(creditoSrvcFalso, cliente, relogioFalso);
	}
	
	@Test
	public void deveIniciarMes() 
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();
		assertEquals("Mês == 1", 1, creditosBean.mesAtual);
		
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 12, 11));
		creditosBean.init();
		assertEquals("Mês == 12", 12, creditosBean.mesAtual);
	}

	@Test
	public void deveIniciarAno() 
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();
		assertEquals("Ano == 2017", 2017, creditosBean.anoAtual);
		
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2016, 01, 11));
		creditosBean.init();
		assertEquals("Ano == 2016", 2016, creditosBean.anoAtual);
		
	}
	
	@Test
	public void deveIniciarOMesSelecionado() 
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();
		assertEquals("Mês selecionado == Janeiro", "Janeiro", creditosBean.getMesSelecionado());
		
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 12, 11));
		creditosBean.init();
		assertEquals("Mês selecionado == Dezembro", "Dezembro", creditosBean.getMesSelecionado());
		
	}
	
	@Test
	public void deveIniciarOsMesesVisiveis() 
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();
		
		assertEquals("Mês de Janeiro", 
				Arrays.asList("Agosto", "Setembro", "Outubro", "Novembro", "Dezembro", "Janeiro"), 
				creditosBean.getMesesVisiveis());
		
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 07, 11));
		creditosBean.init();
		
		assertEquals("Mês de Julho", 
				Arrays.asList("Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho"), 
				creditosBean.getMesesVisiveis());

		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 12, 11));
		creditosBean.init();
		
		assertEquals("Mês de Dezembro", 
				Arrays.asList("Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"), 
				creditosBean.getMesesVisiveis());		
	}
	
	@Test
	public void deveCalcularOPrimeiroDiaDoMesSelecionado() throws RegraDeNegocioException
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();
		
		assertEquals("Primeiro dia.", LocalDate.of(2017, 1, 1), creditosBean.primeiroDiaDoMesSelecionado());
	}
	
	@Test
	public void deveCalcularOPrimeiroDiaDoMesSelecionadoParaMesesDoAnoAnterior() throws RegraDeNegocioException
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();
		
		creditosBean.setMesSelecionado("Dezembro");
		
		assertEquals("Primeiro dia.", LocalDate.of(2016, 12, 1), creditosBean.primeiroDiaDoMesSelecionado());
	}
	
	@Test(expected=RegraDeNegocioException.class)
	public void deveCalcularOPrimeiroDiaQuandoOMesSelecionadoForNull() throws RegraDeNegocioException
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();
		
		creditosBean.setMesSelecionado(null);
		
		assertEquals("Primeiro dia.", LocalDate.of(2017, 1, 1), creditosBean.primeiroDiaDoMesSelecionado());
	}
	
	@Test
	public void deveCalcularOUltimoDia()
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();
		
		LocalDate primeiroDia = LocalDate.of(2017, 01, 1);
		
		assertEquals("Primeiro dia.", LocalDate.of(2017, 1, 31), creditosBean.ultimoDiaDoMesSelecionado(primeiroDia));
		
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 11, 11));
		creditosBean.init();
		
		primeiroDia = LocalDate.of(2017, 11, 1);
		
		assertEquals("Primeiro dia.", LocalDate.of(2017, 11, 30), creditosBean.ultimoDiaDoMesSelecionado(primeiroDia));
	}
	
	@Test
	public void deveListarOsCreditos()
	{
		when(relogioFalso.hoje()).thenReturn(LocalDate.of(2017, 01, 11));
		creditosBean.init();

		creditosBean.setMesSelecionado("Outubro");
		
		when(creditoSrvcFalso.listagemDeCreditoDoClientePorPeriodo(
				cliente, LocalDate.of(2016, 10, 1), LocalDate.of(2016, 10, 31))).thenReturn(listagemFalsa);
		
		ListagemCreditos listagemGerada = creditosBean.listagem();
		
		verify(creditoSrvcFalso, atLeastOnce()).listagemDeCreditoDoClientePorPeriodo(cliente, LocalDate.of(2016, 10, 1), LocalDate.of(2016, 10, 31));
		
		assertEquals("Listagem", listagemFalsa, listagemGerada);
	}
}
