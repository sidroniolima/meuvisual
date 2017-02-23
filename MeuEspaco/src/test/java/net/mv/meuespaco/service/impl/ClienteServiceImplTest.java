package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.dao.ClienteDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.integracao.ClientesDoErp;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cpf;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.service.IntegracaoService;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.util.DataDoSistema;

public class ClienteServiceImplTest {

	private Cliente cliente;
	private Semana semana;
	private Regiao regiao;

	private IntegracaoService integracaoSrvcFalso = Mockito.mock(IntegracaoService.class);
	private RegiaoService regiaoSrvcFalso = Mockito.mock(RegiaoService.class);
	private EscolhaService escolhaSrvcFalso = Mockito.mock(EscolhaService.class);
	private DataDoSistema dataFalsa = Mockito.mock(DataDoSistema.class);
	
	private ClienteDAO clienteDAOFalso = Mockito.mock(ClienteDAO.class);

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
	
	@Test
	public void deveImportarClientesDoErp_TodosOk() throws MalformedURLException, IOException, RegraDeNegocioException
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(
				clienteDAOFalso, integracaoSrvcFalso, regiaoSrvcFalso, escolhaSrvcFalso, cliente);

		Cpf cpfCamila = new Cpf("38393593840");
		Cpf cpfGrabriel = new Cpf("47273263885");
		Cpf cpfAlda = new Cpf("45653894877");
		
		Cliente camila = new Cliente("CAMILA FRANCINE DE OLIVEIRA", cpfCamila);
		Cliente gabriel = new Cliente("GABRIEL DA SILVA", cpfGrabriel);
		Cliente aldaCristina = new Cliente("ALDA CRISTINA DA SILVA SANTOS", cpfAlda);		
		
		Regiao regiao = new Regiao("000023");
		
		assertTrue("Pré-cadastro", camila.isPreCadastro());
		assertTrue("Pré-cadastro", gabriel.isPreCadastro());
		assertTrue("Pré-cadastro", aldaCristina.isPreCadastro());
		
		assertTrue("Sem codigo Siga", null == camila.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == gabriel.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == aldaCristina.getCodigoSiga());
		
		assertTrue("Sem região", null == camila.getRegiao());
		assertTrue("Sem região", null == gabriel.getRegiao());
		assertTrue("Sem região", null == aldaCristina.getRegiao());		
		
		when(clienteDAOFalso.buscarPeloCpf(cpfCamila)).thenReturn(camila);
		when(clienteDAOFalso.buscarPeloCpf(cpfGrabriel)).thenReturn(gabriel);
		when(clienteDAOFalso.buscarPeloCpf(cpfAlda)).thenReturn(aldaCristina);
			
		when(integracaoSrvcFalso.listaClientesDoErp()).thenReturn(Arrays.asList(
				new ClientesDoErp("094619", "CAMILA FRANCINE DE OLIVEIRA", "38393593840", 40, 1800, "000023"),
				new ClientesDoErp("094620", "GABRIEL DA SILVA", "47273263885", 40, 1800, "000023"),
				new ClientesDoErp("094621", "ALDA CRISTINA DA SILVA SANTOS", "45653894877", 40, 2400, "000023")				
				));
		
		when(clienteSrvc.buscarClientePeloCpf(cpfCamila)).thenReturn(camila);
		when(clienteSrvc.buscarClientePeloCpf(cpfGrabriel)).thenReturn(gabriel);
		when(clienteSrvc.buscarClientePeloCpf(cpfAlda)).thenReturn(aldaCristina);
		
		when(regiaoSrvcFalso.buscaPeloCodigoInterno("000023")).thenReturn(regiao);

		clienteSrvc.atualizaInformacoesVindasDoErp();
		
		assertFalse("Efetivado", camila.isPreCadastro());
		assertFalse("Efetivado", gabriel.isPreCadastro());
		assertFalse("Efetivado", aldaCristina.isPreCadastro());
		
		assertEquals("Codigo Siga", "094619", camila.getCodigoSiga());
		assertEquals("Codigo Siga", "094620", gabriel.getCodigoSiga());
		assertEquals("Codigo Siga", "094621", aldaCristina.getCodigoSiga());
		
		assertEquals("Região", regiao, camila.getRegiao());
		assertEquals("Região", regiao, gabriel.getRegiao());
		assertEquals("Região", regiao, aldaCristina.getRegiao());
	}
	
	@Test
	public void deveImportarClientesDoErp_SemCamila() throws MalformedURLException, IOException, RegraDeNegocioException
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(
				clienteDAOFalso, integracaoSrvcFalso, regiaoSrvcFalso, escolhaSrvcFalso, cliente);

		Cpf cpfCamila = new Cpf("38393593840");
		Cpf cpfGrabriel = new Cpf("47273263885");
		Cpf cpfAlda = new Cpf("45653894877");
		
		Cliente camila = new Cliente("CAMILA FRANCINE DE OLIVEIRA", cpfCamila);
		Cliente gabriel = new Cliente("GABRIEL DA SILVA", cpfGrabriel);
		Cliente aldaCristina = new Cliente("ALDA CRISTINA DA SILVA SANTOS", cpfAlda);		
		
		Regiao regiao = new Regiao("000023");
		
		assertTrue("Pré-cadastro", camila.isPreCadastro());
		assertTrue("Pré-cadastro", gabriel.isPreCadastro());
		assertTrue("Pré-cadastro", aldaCristina.isPreCadastro());
		
		assertTrue("Sem codigo Siga", null == camila.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == gabriel.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == aldaCristina.getCodigoSiga());
		
		assertTrue("Sem região", null == camila.getRegiao());
		assertTrue("Sem região", null == gabriel.getRegiao());
		assertTrue("Sem região", null == aldaCristina.getRegiao());		
		
		when(clienteDAOFalso.buscarPeloCpf(cpfCamila)).thenReturn(null);
		when(clienteDAOFalso.buscarPeloCpf(cpfGrabriel)).thenReturn(gabriel);
		when(clienteDAOFalso.buscarPeloCpf(cpfAlda)).thenReturn(aldaCristina);
			
		when(integracaoSrvcFalso.listaClientesDoErp()).thenReturn(Arrays.asList(
				new ClientesDoErp("094620", "GABRIEL DA SILVA", "47273263885", 40, 1800, "000023"),
				new ClientesDoErp("094621", "ALDA CRISTINA DA SILVA SANTOS", "45653894877", 40, 2400, "000023")				
				));
		
		when(regiaoSrvcFalso.buscaPeloCodigoInterno("000023")).thenReturn(regiao);

		clienteSrvc.atualizaInformacoesVindasDoErp();
		
		assertTrue("NÃO FOI Efetivado", camila.isPreCadastro());
		
		assertFalse("Efetivado", gabriel.isPreCadastro());
		assertFalse("Efetivado", aldaCristina.isPreCadastro());
		
		assertTrue("Codigo Siga Ausente", null == camila.getCodigoSiga());
		
		assertEquals("Codigo Siga", "094620", gabriel.getCodigoSiga());
		assertEquals("Codigo Siga", "094621", aldaCristina.getCodigoSiga());
		
		assertTrue("Região Ausente", null == camila.getRegiao());
		
		assertEquals("Região", regiao, gabriel.getRegiao());
		assertEquals("Região", regiao, aldaCristina.getRegiao());
	}

	@Test
	public void deveImportarClientesDoErp_SemRegiao() throws MalformedURLException, IOException, RegraDeNegocioException
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(
				clienteDAOFalso, integracaoSrvcFalso, regiaoSrvcFalso, escolhaSrvcFalso, cliente);
	
		Cpf cpfCamila = new Cpf("38393593840");
		Cpf cpfGrabriel = new Cpf("47273263885");
		Cpf cpfAlda = new Cpf("45653894877");
		
		Cliente camila = new Cliente("CAMILA FRANCINE DE OLIVEIRA", cpfCamila);
		Cliente gabriel = new Cliente("GABRIEL DA SILVA", cpfGrabriel);
		Cliente aldaCristina = new Cliente("ALDA CRISTINA DA SILVA SANTOS", cpfAlda);		
		
		assertTrue("Pré-cadastro", camila.isPreCadastro());
		assertTrue("Pré-cadastro", gabriel.isPreCadastro());
		assertTrue("Pré-cadastro", aldaCristina.isPreCadastro());
		
		assertTrue("Sem codigo Siga", null == camila.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == gabriel.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == aldaCristina.getCodigoSiga());
		
		assertTrue("Sem região", null == camila.getRegiao());
		assertTrue("Sem região", null == gabriel.getRegiao());
		assertTrue("Sem região", null == aldaCristina.getRegiao());		
		
		when(clienteDAOFalso.buscarPeloCpf(cpfCamila)).thenReturn(null);
		when(clienteDAOFalso.buscarPeloCpf(cpfGrabriel)).thenReturn(gabriel);
		when(clienteDAOFalso.buscarPeloCpf(cpfAlda)).thenReturn(aldaCristina);
			
		when(integracaoSrvcFalso.listaClientesDoErp()).thenReturn(Arrays.asList(
				new ClientesDoErp("094620", "GABRIEL DA SILVA", "47273263885", 40, 1800, "000023"),
				new ClientesDoErp("094621", "ALDA CRISTINA DA SILVA SANTOS", "45653894877", 40, 2400, "000023")				
				));
		
		when(regiaoSrvcFalso.buscaPeloCodigoInterno("000023")).thenReturn(null);
	
		clienteSrvc.atualizaInformacoesVindasDoErp();
		
		assertTrue("Pré-cadastro", camila.isPreCadastro());
		assertTrue("Pré-cadastro", gabriel.isPreCadastro());
		assertTrue("Pré-cadastro", aldaCristina.isPreCadastro());
		
		assertTrue("Sem codigo Siga", null == camila.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == gabriel.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == aldaCristina.getCodigoSiga());
		
		assertTrue("Sem região", null == camila.getRegiao());
		assertTrue("Sem região", null == gabriel.getRegiao());
		assertTrue("Sem região", null == aldaCristina.getRegiao());	
	}
	
	@Test
	public void deveImportarClientesDoErp_TrocandoARegiao_Gabriel() throws MalformedURLException, IOException, RegraDeNegocioException
	{
		ClienteService clienteSrvc = new ClienteServiceImpl(
				clienteDAOFalso, integracaoSrvcFalso, regiaoSrvcFalso, escolhaSrvcFalso, cliente);

		Cpf cpfCamila = new Cpf("38393593840");
		Cpf cpfGrabriel = new Cpf("47273263885");
		Cpf cpfAlda = new Cpf("45653894877");
		
		Cliente camila = new Cliente("CAMILA FRANCINE DE OLIVEIRA", cpfCamila);
		Cliente gabriel = new Cliente("GABRIEL DA SILVA", cpfGrabriel);
		Cliente aldaCristina = new Cliente("ALDA CRISTINA DA SILVA SANTOS", cpfAlda);		
		
		Regiao macae = new Regiao(1L, "000023");
		Regiao miracema = new Regiao(2L, "000045");
		
		assertTrue("Pré-cadastro", camila.isPreCadastro());
		assertTrue("Pré-cadastro", gabriel.isPreCadastro());
		assertTrue("Pré-cadastro", aldaCristina.isPreCadastro());
		
		assertTrue("Sem codigo Siga", null == camila.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == gabriel.getCodigoSiga());
		assertTrue("Sem codigo Siga", null == aldaCristina.getCodigoSiga());
		
		assertTrue("Sem região", null == camila.getRegiao());
		assertTrue("Sem região", null == gabriel.getRegiao());
		assertTrue("Sem região", null == aldaCristina.getRegiao());		
		
		when(clienteDAOFalso.buscarPeloCpf(cpfCamila)).thenReturn(camila);
		when(clienteDAOFalso.buscarPeloCpf(cpfGrabriel)).thenReturn(gabriel);
		when(clienteDAOFalso.buscarPeloCpf(cpfAlda)).thenReturn(aldaCristina);
			
		when(integracaoSrvcFalso.listaClientesDoErp()).thenReturn(Arrays.asList(
				new ClientesDoErp("094619", "CAMILA FRANCINE DE OLIVEIRA", "38393593840", 40, 1800, "000023"),
				new ClientesDoErp("094620", "GABRIEL DA SILVA", "47273263885", 40, 1800, "000045"),
				new ClientesDoErp("094621", "ALDA CRISTINA DA SILVA SANTOS", "45653894877", 40, 2400, "000023")				
				));
		
		when(clienteSrvc.buscarClientePeloCpf(cpfCamila)).thenReturn(camila);
		when(clienteSrvc.buscarClientePeloCpf(cpfGrabriel)).thenReturn(gabriel);
		when(clienteSrvc.buscarClientePeloCpf(cpfAlda)).thenReturn(aldaCristina);
		
		when(regiaoSrvcFalso.buscaPeloCodigoInterno("000023")).thenReturn(macae);
		when(regiaoSrvcFalso.buscaPeloCodigoInterno("000045")).thenReturn(miracema);

		clienteSrvc.atualizaInformacoesVindasDoErp();
		
		assertFalse("Efetivado", camila.isPreCadastro());
		assertFalse("Efetivado", gabriel.isPreCadastro());
		assertFalse("Efetivado", aldaCristina.isPreCadastro());
		
		assertEquals("Codigo Siga", "094619", camila.getCodigoSiga());
		assertEquals("Codigo Siga", "094620", gabriel.getCodigoSiga());
		assertEquals("Codigo Siga", "094621", aldaCristina.getCodigoSiga());
		
		assertEquals("Região", macae, camila.getRegiao());
		assertEquals("Região", miracema, gabriel.getRegiao());
		assertEquals("Região", macae, aldaCristina.getRegiao());
	}	
}