package net.mv.meuespaco.model.loja;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Permissao;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;

public class ClienteTest {

	private Cliente cliente;
	private Regiao regiao;
	private Semana semana;
	
	@Before
	public void init() 
	{
		cliente = new Cliente(1L, "Sidronio Lima");
		regiao = new Regiao(1L);
		semana = new Semana(1L, LocalDate.of(2016, 10, 10), LocalDate.of(2016, 10, 16));
		regiao.setSemana(semana);
		cliente.setRegiao(regiao);
	}
	
	@Test
	public void deveRetornarOPrimeiroNome() {
		
		String primeiroNome = cliente.primeiroNome();
		
		if (primeiroNome.isEmpty()) {
			fail("Not yet implemented");
		}
		
		System.out.println(primeiroNome);
	}
	
	@Test
	public void deveValidarOCliente()
	{
		cliente.setCpf(new Cpf("1234566"));
		cliente.setCodigoSiga("000111");
		cliente.setRegiao(new Regiao());
		
		try
		{
			cliente.valida();
			
		} catch (RegraDeNegocioException ex)
		{
			fail("Cliente está válido.");
			System.out.println(ex.getMessage());
		}
	}
	
	@Test
	public void deveInformarASemanaAtual() throws RegraDeNegocioException
	{
		Semana semanaQueVem = new Semana(2L, LocalDate.of(2016, 10, 17), LocalDate.of(2016, 10, 23));
		assertFalse("Semanas diferentes", semanaQueVem.equals(cliente.semanaAtualDoCliente()));
		assertEquals("Semana atual", semana, cliente.semanaAtualDoCliente());
	}

	@Test
	public void deveInformarADataInicialDoCicloAtual() throws RegraDeNegocioException
	{
		assertEquals("Data inicial", LocalDate.of(2016, 10, 10), cliente.dataInicialDoCicloAtual());
	}
	
	@Test
	public void deveInformarADataFinalDoCicloAtual() throws RegraDeNegocioException
	{
		assertEquals("Data final", LocalDate.of(2016, 10, 16), cliente.dataFinalDoCicloAtual());
	}

	@Test
	public void deveEfetivarOCadastro()
	{
		cliente.setCpf(new Cpf("1234566"));
		cliente.setCodigoSiga("000111");
		
		try 
		{
			cliente.efetivaCadastro();
			
			assertTrue("Permissões padrão: VENDA e CLIENTE", cliente.temPermissao(Permissao.ROLE_VENDA));
			
		} catch (RegraDeNegocioException e) {
			fail("Deve efetivar sem erro.");
		}
	}
	
	@Test
	public void deveVerificarSeEhPreCadastroOuNao() throws RegraDeNegocioException
	{
		Cliente pre = new Cliente();
		
		cliente.efetivaCadastro("015308", new Regiao("000001"));
		
		assertFalse("Pré-cadastro", cliente.isPreCadastro());
		assertTrue("Pré-cadastro", pre.isPreCadastro());
	}
}
