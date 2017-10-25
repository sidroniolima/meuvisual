package net.mv.meuespaco.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeComLetra;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeLetra;
import net.mv.meuespaco.model.grade.GradeUnica;
import net.mv.meuespaco.model.grade.Tamanho;

public class ProdutoTest {
	
	private Produto produto;
	
	private Grade grade1;
	private Grade grade2;
	private Grade grade3;
	
	private Grade gradeLetra;
	
	@Before
	public void init() throws RegraDeNegocioException {
		produto = new Produto(1L, "Anel Contorno Zircs Red O");
		
		grade1 = new GradeCorETamanho(Cor.CITRINO_LIMAO, Tamanho.TAM_20);
		grade2 = new GradeCorETamanho(Cor.CITRINO_LIMAO, Tamanho.TAM_22);
		grade3 = new GradeCorETamanho(Cor.AMETISTA_VERDE, Tamanho.TAM_18);
		
		produto.adicionaGrade(grade1);
		produto.adicionaGrade(grade2);
		produto.adicionaGrade(grade3);
	}

	@Test
	public void deveImprimirOProdutoComGrade() {
		
		System.out.println(produto.toString());
		
		assertThat("Checa tamanho da grade.", produto.tamanhoDaGrade(), is(3));
	}
	
	@Test
	public void deveInformarQueProdutoNaoTemGrade() {
		
		Produto produto = new Produto(2L, "Brinco");
		assertThat("Check se produto tem grade.", !produto.temGrade());
	}
	
	@Test
	public void deveRemoverUmaGrade() {
		
		produto.removeGrade(grade2);
		
		assertThat("Check de tamanho de grade.", produto.tamanhoDaGrade(), is(2));
		System.out.println(produto.toString());
		
	}
	
	@Test
	public void deveAdicionarUmaFoto() {
		
		produto.adicionaFoto("foto_de_destaque.png");
		
		assertThat("Check de adição de foto.", produto.getFotos().size(), is(1));
	}
	
	@Test(expected=RegraDeNegocioException.class)
	public void deveLançarExcecaoAoRemoverUmaFoto() throws RegraDeNegocioException {
		
		produto.adicionaFoto("foto_de_destaque.png");
		
		produto.removeFoto("foto_que_nao_pertence.png");
		
		assertThat("Check de remoção de foto.", produto.getFotos().size(), is(1));
	}
	
	@Test
	public void deveRemoverUmaFoto() throws RegraDeNegocioException {
		
		produto.adicionaFoto("foto_de_destaque.png");
		
		produto.removeFoto("foto_de_destaque.png");
		
		assertThat("Check de remoção de foto.", produto.getFotos().size(), is(0));
	}

	@Test
	public void deveRetornarAFotoPeloIndice() {
		
		produto.adicionaFoto("foto_de_destaque.png");
		produto.adicionaFoto("foto_numero_2.png");
		
		assertThat("Check que retorna a foto do índice.", produto.buscaFotoPeloIndice(1), is("foto_de_destaque.png"));
		assertThat("Check que retorna a foto do índice.", produto.buscaFotoPeloIndice(2), is("foto_numero_2.png"));
		
		assertThat("Check que não há este índice.", produto.buscaFotoPeloIndice(3), is("foto_de_destaque.png"));
	}
	
	@Test
	public void deveRetornarAGradeLetraDoProduto() throws RegraDeNegocioException {
		
		gradeLetra = new GradeLetra('A');

		produto.adicionaGrade(gradeLetra);
		
		assertThat("Check da letra da grade", ((GradeComLetra) produto.getGrades().get(3)).getLetra(), is('A'));
		
		System.out.println(produto);
		
	}
	
	@Test(expected=RegraDeNegocioException.class)
	public void naoDevePermitirAdicionarMaisQueUmaGradeUnica() throws RegraDeNegocioException {
		
		Grade gradeUnica1 = new GradeUnica();
		Grade gradeUnica2 = new GradeUnica();

		produto.adicionaGrade(gradeUnica1);
		produto.adicionaGrade(gradeUnica2);
	}
	
	@Test
	public void deveVerificarSePodeOuNaoAlterarAGrade() throws RegraDeNegocioException {
		
		assertFalse(produto.podeAlterarGrade());
		
		Produto produto2 = new Produto();
		produto2.adicionaGrade(new GradeUnica());
		
		assertTrue(produto2.podeAlterarGrade());
	}
	
	@Test
	public void deveCriarProdutoComData() {
		
		Produto produto = new Produto();
		assertTrue("Check da data.", produto.getDataDeCadastro() != null);
		assertThat("Check da data.", produto.getDataDeCadastro(), is(LocalDate.now()));
	}
	
	@Test
	public void deveInformarOValor() {
		Produto produtoComValor_1 = new Produto(1L, "Produto 1");
		produtoComValor_1.setCodigoInterno("20874654MV023090");
		
		Produto produtoComValor_2 = new Produto(2L, "Produto 2");
		produtoComValor_2.setCodigoInterno("20874654MV00390");
		
		Produto produtoComValor_3 = new Produto(3L, "Produto 3");
		produtoComValor_3.setCodigoInterno("21693571BL12190");
		
		assertTrue("Check do valor", produtoComValor_1.valor().compareTo(new BigDecimal(230.90).setScale(2, RoundingMode.HALF_UP)) == 0);
		assertTrue("Check do valor", produtoComValor_2.valor().compareTo(new BigDecimal(3.90).setScale(2, RoundingMode.HALF_UP)) == 0);
		assertTrue("Check do valor", produtoComValor_3.valor().compareTo(new BigDecimal(121.90).setScale(2, RoundingMode.HALF_UP)) == 0);
	}
	
	@Test
	public void deveSerOneClick() throws RegraDeNegocioException
	{
		Produto produto = new Produto(4L, "21776454MV13390");
		produto.setTipoGrade(TipoGrade.UNICA);
		produto.adicionaGrade(new GradeUnica());
				
		assertTrue("Produto eh onclick", produto.isOneClick());
		
		Produto produtoCor = new Produto(5L, "21883421MV09990");
		produtoCor.setTipoGrade(TipoGrade.COR);
		produtoCor.adicionaGrade(new GradeCor(Cor.AMARELO));
				
		assertFalse("Produto eh onclick", produtoCor.isOneClick());
	}

	@Test
	public void deveRetornarGradeUnicaDoProduto() throws RegraDeNegocioException
	{
		Produto produto = new Produto(4L, "21776454MV13390");
		produto.setTipoGrade(TipoGrade.UNICA);
		GradeUnica grade = new GradeUnica(1L, produto);
		produto.adicionaGrade(grade);
				
		assertEquals("Grade única do produto", grade, produto.gradeUnica());
		
		Produto produtoCor = new Produto(5L, "21883421MV09990");
		produtoCor.setTipoGrade(TipoGrade.COR);
		GradeCor gradeCor = new GradeCor(Cor.AZUL_CLARO);
		produtoCor.adicionaGrade(gradeCor);
				
		try
		{
			Grade gradeNaoUnica = produtoCor.gradeUnica();
			fail("Deve lançar exceção por não ser grade unica");
		}catch (RegraDeNegocioException e) {
		}
	}
}
