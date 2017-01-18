package net.mv.meuespaco.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.CarrinhoVenda;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.model.loja.VendaBuilder;
import net.mv.meuespaco.service.impl.VendaServiceImpl;

public class PagamentoBeanTest {

	private VendaServiceImpl vendaSrvcFalso = Mockito.mock(VendaServiceImpl.class);
	private CarrinhoAbstractBean carrinhoBeanFalso = Mockito.mock(CarrinhoVendaBean.class);
	private CarrinhoVenda carrinhoFalso = Mockito.mock(CarrinhoVenda.class);
	
	private Cliente clienteLogadoFalso;
	private Venda vendaPadrao;
	private PagamentoBean pgBean;
	
	@Before
	public void setUp() throws RegraDeNegocioException
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
		pgBean = new PagamentoBean(vendaSrvcFalso, clienteLogadoFalso, carrinhoBeanFalso);
		
	}
	
	@Test
	public void deveCriarOPagamento() throws IOException, RegraDeNegocioException 
	{			
		when(vendaSrvcFalso.criaVendaPeloCarrinho(any(Carrinho.class), any(Cliente.class))).thenReturn(vendaPadrao);
		
		pgBean.init();
		
		verify(vendaSrvcFalso, times(1)).criaVendaPeloCarrinho(any(Carrinho.class), any(Cliente.class));
		
		assertEquals("Pagamento criado...", pgBean.getPagamento().getCustomer().getName(), clienteLogadoFalso.getNome());
		assertEquals("Pagamento criado...", pgBean.getPagamento().getPayment().getAmount(), 1099);
	}

}
