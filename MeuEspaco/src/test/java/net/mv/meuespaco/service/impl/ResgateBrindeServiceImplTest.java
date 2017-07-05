package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import net.mv.meuespaco.dao.ResgateBrindeDAO;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.model.loja.ResgateBrinde;

public class ResgateBrindeServiceImplTest {

	private ResgateBrindeServiceImpl resgateSrvc;
	private ResgateBrindeDAO resgateDaoFalso = Mockito.mock(ResgateBrindeDAO.class);
	private EstoqueServiceImpl estoqueSrvcFalso = Mockito.mock(EstoqueServiceImpl.class);
	private Cliente clienteFalso = Mockito.mock(Cliente.class);
	
	private Produto brincoFalso = Mockito.mock(Produto.class);
	private Produto anelFalso = Mockito.mock(Produto.class);
	
	private Grade gradeUnicaFalsa = Mockito.mock(Grade.class);
	private Grade gradeCorFalsa = Mockito.mock(Grade.class);
	
	@Before
	public void init()
	{
		this.resgateSrvc = new ResgateBrindeServiceImpl(resgateDaoFalso, clienteFalso, estoqueSrvcFalso);
	}
	
	@Test
	public void deveCriarDoCarrinho() 
	{
		List<? extends IMovimentavel> itens = Arrays.asList(
				new ItemCarrinho(brincoFalso, new BigDecimal(1), new BigDecimal(10), gradeUnicaFalsa),
				new ItemCarrinho(anelFalso, new BigDecimal(2), new BigDecimal(2.5), gradeCorFalsa));
		
		ResgateBrinde resgateCriado = resgateSrvc.criaResgateDeCarrinho(itens, clienteFalso, 1000L);
		
		assertFalse("Resgate recém criado.", null == resgateCriado);
		
		try
		{
			resgateCriado.valida();
		} catch (Exception e) 
		{
			fail("Deve criar um resgate válido");
		}
	}

	@Test
	public void deveFinalizarCarrinho() throws RegraDeNegocioException
	{
		List<? extends IMovimentavel> itens = Arrays.asList(
				new ItemCarrinho(brincoFalso, new BigDecimal(1), new BigDecimal(10), gradeUnicaFalsa),
				new ItemCarrinho(anelFalso, new BigDecimal(2), new BigDecimal(2.5), gradeCorFalsa));
		
		resgateSrvc.finalizaCarrinho(itens, clienteFalso, 1000L);
		
		verify(estoqueSrvcFalso, times(1)).movimenta(itens, OrigemMovimento.RESGATE_BRINDE);
	}
	
	@Test
	public void deveExcluirEMovimentarOEstoque() throws RegraDeNegocioException, DeleteException
	{
		ResgateBrinde resgate = new ResgateBrinde();
		resgate.adicionaBrinde(brincoFalso, new BigDecimal(1), new BigDecimal(2.5), gradeCorFalsa);
				
		when(resgateSrvc.buscarComItensPeloCodigo(1L)).thenReturn(resgate);
		
		resgateSrvc.exclui(1L);
		
		ArgumentCaptor<Long> argumento = ArgumentCaptor.forClass(Long.class);
		verify(resgateDaoFalso).excluir(argumento.capture());
		
		verify(resgateDaoFalso, times(1)).excluir(argumento.getValue());
		verify(estoqueSrvcFalso, times(1)).estorna(resgate.getBrindes(), OrigemMovimento.ESTORNO_RESGATE_BRINDE);		
	}
}
