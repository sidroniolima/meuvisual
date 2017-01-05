package net.mv.meuespaco.integracao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

public class CreditoTest {

	private Credito creditoPago;
	private Credito creditoDbt;
	private Credito creditoNaoPago;
	private Credito creditoCdt;
	
	@Before
	public void setUp() {
		creditoPago = new Credito(1L, "000004", "DANIELA SILVA GODOY", 33.9d, "COM", "", LocalDate.of(2016, 8, 14));
		creditoNaoPago = new Credito(2L, "000004", "DANIELA SILVA GODOY", 33.9d, "COM", "<20PÇ", LocalDate.of(2016, 8, 14));
		creditoDbt = new Credito(3L, "000004", "DANIELA SILVA GODOY", 33.9d, "DBT", "", LocalDate.of(2016, 8, 14));
		creditoCdt = new Credito(4L, "000004", "DANIELA SILVA GODOY", 33.9d, "CDT", "", LocalDate.of(2016, 8, 14));
	}
	
	@Test
	public void deveVerificarSeEhOuNaoSoma()
	{
		assertTrue("Eh soma", creditoPago.isSoma());
		assertTrue("Eh soma", creditoCdt.isSoma());
		assertFalse("NÃO eh soma", creditoNaoPago.isSoma());
		assertFalse("NÃO eh soma", creditoDbt.isSoma());
	}

}
