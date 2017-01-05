package net.mv.meuespaco.integracao;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ListagemCreditosTest {

	private ListagemCreditos listagem;
	
	@Before
	public void setUp() {

	}
	
	@Test
	public void deveCalcularOTotal()
	{
		listagem = new ListagemCreditos(Arrays.asList(
				new Credito(1L, "000004", "DANIELA SILVA GODOY", 10.10d, "COM", "", LocalDate.of(2016, 8, 14)),
				new Credito(2L, "000004", "DANIELA SILVA GODOY", 10.10d, "COM", "<20PÇ", LocalDate.of(2016, 8, 14)),
				new Credito(3L, "000004", "DANIELA SILVA GODOY", 10.10d, "DBT", "", LocalDate.of(2016, 8, 14)),
				new Credito(4L, "000004", "DANIELA SILVA GODOY", 10.10d, "CDT", "", LocalDate.of(2016, 8, 14))));
		
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
				new Credito(1L, "000004", "DANIELA SILVA GODOY", 10.10d, "COM", "", LocalDate.of(2016, 8, 14)),
				new Credito(4L, "000004", "DANIELA SILVA GODOY", 10.10d, "CDT", "", LocalDate.of(2016, 8, 14))));
		
		assertEquals("Saldo", 20.20d, listagem.saldo(), 0.0d);
	}

	@Test
	public void deveCalcularOTotalComNaoSomas()
	{
		listagem = new ListagemCreditos(Arrays.asList(
				new Credito(2L, "000004", "DANIELA SILVA GODOY", 10.10d, "COM", "<20PÇ", LocalDate.of(2016, 8, 14)),
				new Credito(3L, "000004", "DANIELA SILVA GODOY", 10.10d, "DBT", "", LocalDate.of(2016, 8, 14))));
		
		assertEquals("Saldo", -20.20d, listagem.saldo(), 0.0d);
	}
}
