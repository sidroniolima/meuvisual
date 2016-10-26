package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import net.mv.meuespaco.dao.VendaDAO;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.CarrinhoConsignado;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.model.loja.VendaBuilder;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.VendaService;

public class VendaServiceImplTest {

	private VendaDAO daoFalso = Mockito.mock(VendaDAO.class);
	private EstoqueService estoqueFalso = Mockito.mock(EstoqueService.class);
	private Carrinho carrinhoFalso = Mockito.mock(CarrinhoConsignado.class);
	
	private Venda vendaPadrao;
	
	private VendaService vendaSrvc = new VendaServiceImpl(daoFalso, estoqueFalso);
	
	ItemCarrinho item1;
	ItemCarrinho item2;
	
	@Before
	public void init()
	{
		vendaPadrao = new VendaBuilder()
				.comCodigo(1L)
				.noHorario(LocalDateTime.now())
				.doCliente(new Cliente(1L, "Cliente 1"))
				.doProduto(new Produto(1L, "Brinco", "21007656MV09000"), 
				BigDecimal.ONE, 
				new GradeCorETamanho(Cor.APATITA, Tamanho.TAM_19))
				.constroi();
		
		item1 = new ItemCarrinho(new Produto(1L, "Brinco", "21007656MV09000"), 
				BigDecimal.ONE, 
				new GradeCorETamanho(Cor.APATITA, Tamanho.TAM_19));
		
		item2 = new ItemCarrinho(new Produto(2L, "Anel", "20008899MV02000"), 
				BigDecimal.ONE, 
				new GradeTamanho(Tamanho.TAM_22));
		
	}
	
	@Test
	public void deveSalvarUmaVenda() throws RegraDeNegocioException 
	{
		when(carrinhoFalso.getItens()).thenReturn(Arrays.asList(item1, item2));
		when(carrinhoFalso.getDesconto()).thenReturn(BigDecimal.ZERO);
		
		vendaSrvc.criaVendaPeloCarrinho(carrinhoFalso, new Cliente(1L, "Cliente 1"));
		
		ArgumentCaptor<Venda> argumento = ArgumentCaptor.forClass(Venda.class);
		verify(daoFalso).salvar(argumento.capture());
		
		Venda venda = argumento.getValue();
		
		verify(daoFalso, atLeastOnce()).salvar(venda);
		
		assertEquals("Venda gerada", new BigDecimal(110), venda.valorComDesconto());
	}

	@Test(expected=RegraDeNegocioException.class)
	public void naoDeveGerarComCarrinhoVazio() throws RegraDeNegocioException 
	{
		when(carrinhoFalso.getItens()).thenReturn(Arrays.asList());
		
		vendaSrvc.criaVendaPeloCarrinho(carrinhoFalso, new Cliente(1L, "Cliente 1"));
		
		ArgumentCaptor<Venda> argumento = ArgumentCaptor.forClass(Venda.class);
		verify(daoFalso).salvar(argumento.capture());
		
		Venda venda = argumento.getValue();
		
		verify(daoFalso, times(1)).salvar(venda);
	}
	
	@Test
	public void deveMovimentarOEstoque() throws RegraDeNegocioException 
	{
		when(carrinhoFalso.getItens()).thenReturn(Arrays.asList(item1, item2));
		
		vendaSrvc.criaVendaPeloCarrinho(carrinhoFalso, new Cliente(1L, "Cliente 1"));
		
		ArgumentCaptor<Venda> argumento = ArgumentCaptor.forClass(Venda.class);
		verify(daoFalso).salvar(argumento.capture());
		
		Venda venda = argumento.getValue();
		
		verify(estoqueFalso, times(1)).movimentaVenda(venda.getItens());
	}
	
	@Test
	public void deveExcliurEMovimentarOEstoque() throws RegraDeNegocioException, DeleteException 
	{
		when(vendaSrvc.buscaPeloCodigo(1L)).thenReturn(vendaPadrao);
		
		vendaSrvc.exclui(1L);
		
		ArgumentCaptor<Long> argumento = ArgumentCaptor.forClass(Long.class);
		verify(daoFalso).excluir(argumento.capture());
		
		verify(daoFalso, times(1)).excluir(argumento.getValue());
		verify(estoqueFalso, times(1)).estornaVenda(vendaPadrao.getItens());
		
		assertEquals("Código da venda.", 1L, argumento.getValue().longValue());
	}
	
	@Test(expected=RegraDeNegocioException.class)
	public void naoDeveExcliurUmaVendaInexistente() throws RegraDeNegocioException, DeleteException 
	{
		when(vendaSrvc.buscaPeloCodigo(1L)).thenReturn(null);
		
		vendaSrvc.exclui(2L);
		
		ArgumentCaptor<Long> argumento = ArgumentCaptor.forClass(Long.class);
		verify(daoFalso).excluir(argumento.capture());

		verify(daoFalso, never()).excluir(argumento.getValue());
		verify(estoqueFalso, never()).estornaVenda(vendaPadrao.getItens());
		
	}
}
