package net.mv.meuespaco.integracao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.model.loja.Cliente;

public class CreditoTest {

	private Credito creditoPago;
	private Credito creditoDbt;
	private Credito creditoNaoPago;
	private Credito creditoCdt;
	private Cliente cliente;
	
	@Before
	public void setUp() 
	{
		cliente = new Cliente(1L, "CLIENTE DONA DO CREDITO");
		
		creditoPago = new Credito(1L, cliente, "DANIELA SILVA GODOY", 33.9d, "COM", "", LocalDate.of(2016, 8, 14));
		creditoNaoPago = new Credito(2L, cliente, "DANIELA SILVA GODOY", 33.9d, "COM", "<20PÇ", LocalDate.of(2016, 8, 14));
		creditoDbt = new Credito(3L, cliente, "DANIELA SILVA GODOY", 33.9d, "DBT", "", LocalDate.of(2016, 8, 14));
		creditoCdt = new Credito(4L, cliente, "DANIELA SILVA GODOY", 33.9d, "CDT", "", LocalDate.of(2016, 8, 14));
	}
	
	@Test
	public void deveVerificarSeEhOuNaoSoma()
	{
		assertTrue("Eh soma", creditoPago.isSoma());
		assertTrue("Eh soma", creditoCdt.isSoma());
		assertFalse("NÃO eh soma", creditoNaoPago.isSoma());
		assertFalse("NÃO eh soma", creditoDbt.isSoma());
	}
	
	@Test
	public void verificaSeFoiPagoOuNao()
	{
		assertTrue("Foi pago", creditoPago.isPago());
		assertFalse("NÃO foi pago", creditoNaoPago.isPago());
		assertTrue("Foi pago", creditoCdt.isPago());
		assertTrue("Foi pago", creditoDbt.isPago());
	}

}
