package net.mv.meuespaco.service.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.service.CarrinhoService;

public class CarrinhoServiceImplTest {

	private Produto prodBrinco;
	private Produto prodAnel;
	
	private Grade gradeBrinco;
	private Grade gradeAnel;
	
	private CarrinhoService carrinhoSrvc;
	
	@Before
	public void init() throws RegraDeNegocioException {
		
		prodBrinco = new Produto(1L, "Brinco");
		gradeBrinco = new GradeCorETamanho(Cor.CRISTAL, Tamanho.TAM_18);
		prodBrinco.adicionaGrade(gradeBrinco);
		
		prodAnel = new Produto(2L, "Anel");
		gradeAnel = new GradeTamanho(Tamanho.TAM_24);
		prodAnel.adicionaGrade(gradeAnel);
		
		prodBrinco.setSubgrupo(new Subgrupo(2L, "Brinco"));
		prodAnel.setSubgrupo(new Subgrupo(1L, "Anel"));
		
		carrinhoSrvc = new CarrinhoServiceImpl();
	}
	
	public void deveAdicionarUmProdutoAoCarrinho() throws RegraDeNegocioException {
		
		assertTrue("Check de carrinho vazio.", carrinhoSrvc.getCarrinho().isEmpty());
		
		carrinhoSrvc.adicionaProduto(prodBrinco, new BigDecimal(1), new BigDecimal(10), gradeBrinco);
		
		assertTrue("Check de carrinho com item.", carrinhoSrvc.qtdDeItens() == 1);
	}
	
	public void deveRemoverOProduto() throws RegraDeNegocioException {
		Grade gradeNova = new GradeCorETamanho(Cor.AMETISTA_VERDE, Tamanho.TAM_23);
		prodBrinco.adicionaGrade(gradeNova);
		
		carrinhoSrvc.adicionaProduto(prodBrinco, new BigDecimal(1), new BigDecimal(10), gradeBrinco);
		carrinhoSrvc.adicionaProduto(prodBrinco, BigDecimal.ONE,  new BigDecimal(10), gradeNova);
		carrinhoSrvc.adicionaProduto(prodAnel, BigDecimal.ONE,  new BigDecimal(10), gradeAnel);
		
		assertTrue("Check de carrinho com item.", carrinhoSrvc.qtdDeItens() == 3);
		
		ItemCarrinho itemSelecionado = carrinhoSrvc.getCarrinho().get(0);
		carrinhoSrvc.removeItem(itemSelecionado);
		
		assertTrue("Check de carrinho com item.", carrinhoSrvc.qtdDeItens() == 2);
		assertTrue("Check se o item é o correto.", 
				carrinhoSrvc.getCarrinho().get(0).getProduto().equals(prodBrinco));
		
	}
	
	public void deveAlterarAQtdDoItem() throws RegraDeNegocioException {
		
		carrinhoSrvc.adicionaProduto(prodBrinco, new BigDecimal(1),  new BigDecimal(10), gradeBrinco);
		carrinhoSrvc.adicionaProduto(prodAnel, BigDecimal.ONE,  new BigDecimal(10), gradeAnel);
		
		ItemCarrinho itemSelecionado = carrinhoSrvc.getCarrinho().get(1);
		carrinhoSrvc.alteraQtd(itemSelecionado, itemSelecionado.getQtd().add(BigDecimal.ONE));
		
		assertTrue("Check de qtd de itens.", carrinhoSrvc.qtdDeItens() == 3);
	}

	public void deveAtualizarQtdSeForMesmoProduto() throws RegraDeNegocioException {
		
		carrinhoSrvc.adicionaProduto(prodBrinco, new BigDecimal(1),  new BigDecimal(10), gradeBrinco);
		carrinhoSrvc.adicionaProduto(prodAnel, BigDecimal.ONE,  new BigDecimal(10), gradeAnel);
		
		carrinhoSrvc.adicionaProduto(prodBrinco, new BigDecimal(1),  new BigDecimal(10), gradeBrinco);
		
		assertTrue("Check tamanho do carrinho.", carrinhoSrvc.getCarrinho().size() == 2);
		assertEquals("Check de qtd de itens.", carrinhoSrvc.qtdDeItens(), new BigDecimal(3).doubleValue(), 0.00);
	}
	
	public void deveCriarResumoDoCarrinho() throws RegraDeNegocioException {
		
		carrinhoSrvc.adicionaProduto(prodBrinco, new BigDecimal(1),  new BigDecimal(10), gradeBrinco);
		carrinhoSrvc.adicionaProduto(prodAnel, BigDecimal.ONE,  new BigDecimal(10), gradeAnel);
		
		carrinhoSrvc.adicionaProduto(prodBrinco, new BigDecimal(1),  new BigDecimal(10), gradeBrinco);
		
		assertTrue("Check da criação do resumo.", carrinhoSrvc.getResumo().size() == 3);
		
	}
}
