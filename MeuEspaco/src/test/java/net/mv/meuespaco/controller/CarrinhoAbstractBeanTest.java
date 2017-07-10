package net.mv.meuespaco.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.service.CarrinhoService;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.ReservaProdutoService;
import net.mv.meuespaco.service.impl.CarrinhoServiceImpl;
import net.mv.meuespaco.service.impl.ClienteServiceImpl;
import net.mv.meuespaco.service.impl.EstoqueServiceImpl;

public class CarrinhoAbstractBeanTest {

	private ReservaProdutoService reservaProdutoSrvcFalso = Mockito.mock(ReservaProdutoService.class);
	private EstoqueService estoqueSrvcFalso = Mockito.mock(EstoqueServiceImpl.class);
	private ClienteService clienteSrvcFalso = Mockito.mock(ClienteServiceImpl.class);
	
	private CarrinhoService carrinhoService = Mockito.mock(CarrinhoServiceImpl.class);
	
	private CarrinhoAbstractBean carrinhoBean = 
			new CarrinhoConsignadoBean(reservaProdutoSrvcFalso, estoqueSrvcFalso, carrinhoService, clienteSrvcFalso);
	
	private Produto produto;
	
	@Before
	public void init()
	{
		Grupo grupo = new Grupo(1L, "Anéis");
		Subgrupo sub = new Subgrupo(1L, "Anéis de magistério");
		sub.setGrupo(grupo);
		
		produto = new Produto(1L, "Teste", "21457812MV01990");
		produto.setValor(new BigDecimal(19.90));
		produto.setSubgrupo(sub);
	}
	
	@Test
	public void deveCriarCarrinho() throws RegraDeNegocioException 
	{
		when(clienteSrvcFalso.qtdDisponivelParaEscolha()).thenReturn(40);
		when(clienteSrvcFalso.valorDisponivelParaEscolha()).thenReturn(new BigDecimal(1800));
		
		carrinhoBean.criaCarrinho();
		
		assertTrue("Carrinho criado", null != carrinhoBean.getCarrinho());
		
		carrinhoBean.getCarrinho().adicionaItem(
				new ItemCarrinho(produto, BigDecimal.ONE,  new BigDecimal(10), new GradeCor(1L, Cor.AMETISTA_ROSA)));
		
		assertTrue("Item do carrinho", produto.equals(carrinhoBean.getCarrinho().getItens().get(0).getProduto()));
	}

	@Test
	public void deveRetornarAQtdDeItens() throws RegraDeNegocioException
	{
		when(clienteSrvcFalso.qtdDisponivelParaEscolha()).thenReturn(40);
		when(clienteSrvcFalso.valorDisponivelParaEscolha()).thenReturn(new BigDecimal(1800));
		
		carrinhoBean.criaCarrinho();
		
		assertTrue("Carrinho criado", null != carrinhoBean.getCarrinho());
		
		assertEquals("Qtd de itens", BigDecimal.ZERO, carrinhoBean.getQtdDeItens());
		
		carrinhoBean.getCarrinho().adicionaItem(
				new ItemCarrinho(produto, BigDecimal.ONE,  new BigDecimal(10), new GradeCor(1L, Cor.AMETISTA_ROSA)));
		
		assertEquals("Qtd de itens", BigDecimal.ONE, carrinhoBean.getQtdDeItens());
	}
	
}