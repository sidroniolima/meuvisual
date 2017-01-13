package net.mv.meuespaco.integracao;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.model.integracao.Credito;
import net.mv.meuespaco.model.integracao.ListagemCreditos;
import net.mv.meuespaco.model.loja.Cliente;

public class ListagemCreditosTest {

	private ListagemCreditos listagem;
	private Cliente cliente;
	
	@Before
	public void setUp() {
		cliente = new Cliente(1L, "CLIENTE DONA DO CREDITO");
	}
	
	@Test
	public void deveCalcularOTotal()
	{
		listagem = new ListagemCreditos(Arrays.asList(
				new Credito(1L, cliente, "CLI Q GEROU A COM", 10.10d, "COM", "", LocalDate.of(2016, 8, 14)),
				new Credito(2L, cliente, "CLI Q GEROU A COM", 10.10d, "COM", "<20PÇ", LocalDate.of(2016, 8, 14)),
				new Credito(3L, cliente, "CLI Q GEROU A COM", 10.10d, "DBT", "", LocalDate.of(2016, 8, 14)),
				new Credito(4L, cliente, "CLI Q GEROU A COM", 10.10d, "CDT", "", LocalDate.of(2016, 8, 14))));
		
		assertEquals("Saldo", 0.0d, listagem.saldo(), 0.0d);
	}
	
	@Test
	public void deveCalcularOTotalDeListagemZerada()
	{
		listagem = new ListagemCreditos();
		
		assertEquals("Saldo", 0.0d, listagem.saldo(), 0.0d);
	}
	
	@Test
	public void deveCalcularOTotalSohComSomas()
	{
		listagem = new ListagemCreditos(Arrays.asList(
				new Credito(1L, cliente, "CLI Q GEROU A COM", 10.10d, "COM", "", LocalDate.of(2016, 8, 14)),
				new Credito(4L, cliente, "CLI Q GEROU A COM", 10.10d, "CDT", "", LocalDate.of(2016, 8, 14))));
		
		assertEquals("Saldo", 20.20d, listagem.saldo(), 0.0d);
	}

	@Test
	public void deveCalcularOTotalComNaoSomas()
	{
		listagem = new ListagemCreditos(Arrays.asList(
				new Credito(2L, cliente, "CLI Q GEROU A COM", 10.10d, "COM", "<20PÇ", LocalDate.of(2016, 8, 14)),
				new Credito(3L, cliente, "CLI Q GEROU A COM", 10.10d, "DBT", "", LocalDate.of(2016, 8, 14))));
		
		assertEquals("Saldo", -20.20d, listagem.saldo(), 0.0d);
	}
}
