package net.mv.meuespaco.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.EstoqueException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeUnica;
import net.mv.meuespaco.model.grade.Tamanho;

public class EstoqueTest {

	private Produto produto;
	private Produto produtoSemGrade;
	private Produto produtoGradeUnica;
	
	private Grade gradeAzul20;
	private Grade gradeAzul22;
	private Grade gradeVerde18;
	private Grade gradeVermelho23;
	private Grade gradeUnica;
	
	private Estoque estoque;
	
	@Before
	public void init() throws RegraDeNegocioException {
		
		produto = new Produto(1L, "Anel PR Zirc Navet Lt Vir");
		produtoSemGrade = new Produto(2L, "Brinco Dourado");
		produtoGradeUnica = new Produto(3, "Brinco Banho de Prata");
		
		gradeAzul20 = new GradeCorETamanho(1L, produto, Cor.AMETISTA, Tamanho.TAM_20);
		gradeAzul22 = new GradeCorETamanho(2L, produto, Cor.AMETISTA, Tamanho.TAM_22);
		gradeVerde18 = new GradeCorETamanho(3L, produto, Cor.AMETISTA_VERDE, Tamanho.TAM_18);
		gradeVermelho23 = new GradeCorETamanho(4L, produto, Cor.C_ROSA_LEITOSO, Tamanho.TAM_23);
		gradeUnica = new GradeUnica(4L, produtoGradeUnica);
		
		produto.adicionaGrade(gradeAzul20);
		produto.adicionaGrade(gradeAzul22);
		produto.adicionaGrade(gradeVerde18);
		produtoGradeUnica.adicionaGrade(gradeUnica);
		
		estoque = new Estoque();
		estoque.adicionaMovimentacao(new Movimentacao(produto, gradeAzul20, 10L));
		estoque.adicionaMovimentacao(new Movimentacao(produto, gradeAzul22, 5L));
		estoque.adicionaMovimentacao(new Movimentacao(produto, gradeVerde18, 16L));
		estoque.adicionaMovimentacao(
				new Movimentacao(produtoGradeUnica, gradeUnica, 4L));
	}
	
	@Test
	public void deveInformaAQtdDasGrades() throws EstoqueException {
			
		assertEquals(10L, estoque.qtdDoProdutoPorGrade(produto, gradeAzul20), 0.0);
		assertEquals(5L, estoque.qtdDoProdutoPorGrade(produto, gradeAzul22), 0.0);
		assertEquals(16L, estoque.qtdDoProdutoPorGrade(produto, gradeVerde18), 0.0);
		
		assertEquals(4L, estoque.qtdDoProdutoPorGrade(produtoGradeUnica, gradeUnica), 0.0);
		
		assertEquals(0L, estoque.qtdDoProdutoPorGrade(produto, gradeVermelho23), 0.0);
	}
	
	@Test
	public void deveInformaAQtdDoProduto() {
			
		assertEquals(31L, estoque.qtdDoProduto(produto), 0.0);
	}
	
	@Test(expected=EstoqueException.class)
	public void deveLancarExcecaoDeEstoqueDeProdutoSemGrade() throws EstoqueException {
		
		assertEquals(10L, estoque.qtdDoProdutoPorTamanho(produtoSemGrade, Tamanho.TAM_20), 0.0);
	}

}
