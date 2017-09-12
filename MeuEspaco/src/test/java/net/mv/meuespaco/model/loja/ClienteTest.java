package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Permissao;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.Usuario;

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
		
		List<Permissao> permissoes = Arrays.asList(Permissao.ROLE_ADMIN);
		
		try 
		{
			cliente.efetivaCadastro(permissoes);
			
		} catch (RegraDeNegocioException e) {
			fail("Deve efetivar sem erro.");
		}
	}
	
	@Test
	public void deveVerificarSeEhPreCadastroOuNao() throws RegraDeNegocioException
	{
		Cliente pre = new Cliente();
		
		cliente.efetivaCadastro("015308", new Regiao("000001"), Arrays.asList(Permissao.ROLE_ADMIN));
		
		assertFalse("Pré-cadastro", cliente.isPreCadastro());
		assertTrue("Pré-cadastro", pre.isPreCadastro());
	}
	
	@Test
	public void deveTerPermissao() throws RegraDeNegocioException
	{
		cliente.criaUsuario(Arrays.asList(Permissao.ROLE_ADMIN));
		
		assertTrue("Tem permissão Administrativa.", cliente.temPermissao(Permissao.ROLE_ADMIN));
	}
	
	@Test
	public void naoDeveTerPermissao() throws RegraDeNegocioException
	{
		cliente.criaUsuario(Arrays.asList(Permissao.ROLE_ADMIN));
		assertFalse("Tem permissão Administrativa.", cliente.temPermissao(Permissao.ROLE_VENDA));
	}
	
	@Test
	public void deveAtualizarPermissoes()
	{
		List<Permissao> novasPermissoes = new ArrayList<Permissao>();
		novasPermissoes.addAll(Arrays.asList(Permissao.ROLE_VENDA, Permissao.ROLE_CLIENTE));
		
		cliente.criaUsuario(Arrays.asList(Permissao.ROLE_ADMIN));
		cliente.atualizaPermissoes(novasPermissoes);
		
		assertEquals("Permissões atualizadas.", Arrays.asList(Permissao.ROLE_VENDA, Permissao.ROLE_CLIENTE, Permissao.ROLE_ADMIN), cliente.getUsuario().getPermissoes());
	}
	
	@Test
	public void deveVerificarSeUsuarioEhAdministrativo()
	{
		Usuario usuario = new Usuario();
		assertFalse("Usuario não adm.", usuario.isAdmin());
	}
}
