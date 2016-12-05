package net.mv.meuespaco.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.model.loja.VendaBuilder;
import net.mv.meuespaco.service.VendaService;

public class PagamentoBeanTest {

	private VendaService vendaSrvcFalso = Mockito.mock(VendaService.class);
	private PrePagamentoBean prePagamentoFalso = Mockito.mock(PrePagamentoBean.class);
	
	private Cliente clienteLogadoFalso;
	private Venda vendaPadrao;
	private PagamentoBean pgBean;
	
	@Before
	public void setUp()
	{
		vendaPadrao = new VendaBuilder()
				.comCodigo(1L)
				.noHorario(LocalDateTime.now())
				.doCliente(new Cliente(1L, "Cliente 1"))
				.doProduto(new Produto(1L, "Brinco", "21007656MV01099"), 
				BigDecimal.ONE, 
				new GradeCorETamanho(Cor.APATITA, Tamanho.TAM_19))
				.constroi();

		clienteLogadoFalso = new Cliente(1L, "Sidronio");
		pgBean = new PagamentoBean(vendaSrvcFalso, clienteLogadoFalso, prePagamentoFalso);
	}
	
	
	@Test
	public void deveCriarOPagamento() throws IOException 
	{
		when(prePagamentoFalso.vendaParaPagamento()).thenReturn(vendaPadrao);
		when(vendaSrvcFalso.buscaCompletaPeloCodigo(1L)).thenReturn(vendaPadrao);
		
		pgBean.init();
		
		assertEquals("Pagamento criado...", pgBean.getPagamento().getCustomer().getName(), clienteLogadoFalso.getNome());
		assertEquals("Pagamento criado...", pgBean.getPagamento().getPayment().getAmount(), 1099);
	}

}
