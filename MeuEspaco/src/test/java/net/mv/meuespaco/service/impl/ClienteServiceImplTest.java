package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.util.DataDoSistema;

public class ClienteServiceImplTest {

	private Cliente cliente;
	private Semana semana;
	private Regiao regiao;
	
	private EscolhaService escolhaSrvcFalso = Mockito.mock(EscolhaService.class);
	private DataDoSistema dataFalsa = Mockito.mock(DataDoSistema.class);
	
	@Before
	public void init()
	{
		regiao = new Regiao(1L);
		semana = new Semana(1L, LocalDate.of(2016, 10, 17), LocalDate.of(2016, 10, 23), dataFalsa);
		regiao.setSemana(semana);
		
		cliente = new Cliente(1L, "Não Pode Escolher", "015308");
		cliente.setQtdDePecasParaEscolha(40f);
		cliente.setValorParaEscolha(2500f);
		cliente.setRegiao(regiao);
	}
	
	@Test
	public void naoDevePermitirEscolhaDeClienteComSaldoUltrapassado() throws RegraDeNegocioException 
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(escolhaSrvcFalso, cliente);
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 17));
		
		when(escolhaSrvcFalso
				.qtdDePecasEscolhidasDoCicloAtual(cliente))
					.thenReturn(new BigDecimal(40));
		
		when(escolhaSrvcFalso
				.valorDePecasEscolhidasDescontaveisDoCicloAtual(cliente))
				.thenReturn(new BigDecimal(2500));
		
		try
		{
			clienteSrvc.verificaSeOUsuarioLogadoPodeEscolher();
			fail("Deve lançar exceção de QUANTIDADE excedida.");
		} catch (RegraDeNegocioException ex)
		{
			assertTrue("Exceção lançada de QUANTIDADE", 
					ex.getMessage().contains("Você já escolheu todas as peças disponíveis para este ciclo.") );
		}
		
	}

	@Test
	public void naoDevePermitirEscolhaDeClienteComValorUltrapassado() throws RegraDeNegocioException 
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(escolhaSrvcFalso, cliente);
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 17));
		
		when(escolhaSrvcFalso
				.qtdDePecasEscolhidasDoCicloAtual(cliente))
					.thenReturn(new BigDecimal(30));
		
		when(escolhaSrvcFalso
				.valorDePecasEscolhidasDescontaveisDoCicloAtual(cliente))
				.thenReturn(new BigDecimal(2500));
		try
		{
			clienteSrvc.verificaSeOUsuarioLogadoPodeEscolher();
			fail("Deve lançar exceção de VALOR excedida.");
		} catch (RegraDeNegocioException ex)
		{
			assertTrue("Exceção lançada de VALOR", 
					ex.getMessage().contains("Você já escolheu o valor permitido para este ciclo.") );
		}
	}
	
	@Test
	public void devePermitirAEscolhaDoCliente() throws RegraDeNegocioException
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(escolhaSrvcFalso, cliente);
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 17));

		when(escolhaSrvcFalso
				.qtdDePecasEscolhidasDoCicloAtual(cliente))
					.thenReturn(new BigDecimal(30));
		
		when(escolhaSrvcFalso
				.valorDePecasEscolhidasDescontaveisDoCicloAtual(cliente))
				.thenReturn(new BigDecimal(1900));
		try
		{
			clienteSrvc.verificaSeOUsuarioLogadoPodeEscolher();
		} catch (RegraDeNegocioException ex)
		{
			fail("NÃO DEVE lançar exceção de VALOR ou QUANTIDADE excedida.");
		}
	}
	
	@Test
	public void deveCalcularAQtdDisponivelParaEscolha() throws RegraDeNegocioException
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(escolhaSrvcFalso, cliente);
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 17));

		when(escolhaSrvcFalso
				.qtdDePecasEscolhidasDoCicloAtual(cliente))
					.thenReturn(new BigDecimal(30));
		
		when(escolhaSrvcFalso
				.valorDePecasEscolhidasDescontaveisDoCicloAtual(cliente))
				.thenReturn(new BigDecimal(1900));

		clienteSrvc.verificaSeOUsuarioLogadoPodeEscolher();
		
		assertEquals("QUANTIDADE disponível.", 10, clienteSrvc.qtdDisponivelParaEscolha());
		
	}
	
	@Test
	public void deveCalcularOValorDisponivelParaEscolha() throws RegraDeNegocioException
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(escolhaSrvcFalso, cliente);
		
		when(dataFalsa.hoje()).thenReturn(LocalDate.of(2016, 10, 17));

		when(escolhaSrvcFalso
				.qtdDePecasEscolhidasDoCicloAtual(cliente))
					.thenReturn(new BigDecimal(30));
		
		when(escolhaSrvcFalso
				.valorDePecasEscolhidasDescontaveisDoCicloAtual(cliente))
				.thenReturn(new BigDecimal(1789.90).setScale(2, RoundingMode.HALF_UP));

		clienteSrvc.verificaSeOUsuarioLogadoPodeEscolher();
		
		assertEquals("VALOR disponível.", new BigDecimal(710.10).setScale(2, RoundingMode.HALF_UP), 
				clienteSrvc.valorDisponivelParaEscolha());
	}
}
