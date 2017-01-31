package net.mv.meuespaco.model.grade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;

public class GradeTest {

	Grade gradeCor1;
	Grade gradeCor2;
	
	Grade gradeTamanho1;
	Grade gradeTamanho2;
	
	Grade gradeCorETamanho1;
	Grade gradeCorETamanho2;
	
	Grade gradeUnica1;
	Grade gradeUnica2;
	
	Grade gradeLetra1;
	Grade gradeLetra2;
	
	Grade gradeSol;
	Grade gradeColcheia;
	
	Produto produto;
	
	@Before
	public void init() {
		gradeCor1 = new GradeCor(1L, null, Cor.AMETISTA);
		gradeCor2 = new GradeCor(2L, null, Cor.APATITA);
		
		gradeTamanho1 = new GradeTamanho(3L, null, Tamanho.TAM_10);
		gradeTamanho2 = new GradeTamanho(4L, null, Tamanho.TAM_11);
		
		gradeCorETamanho1 = new GradeCorETamanho(5L, null, Cor.C_ROSA_LEITOSO, Tamanho.TAM_20);
		gradeCorETamanho2 = new GradeCorETamanho(6L, null, Cor.CITRINO_CONHAQUE, Tamanho.TAM_22);
		
		gradeUnica1 = new GradeUnica(7L, null);
		gradeUnica2 = new GradeUnica(8L, null);
		
		gradeLetra1 = new GradeLetra(9L, null, 'A');
		gradeLetra2 = new GradeLetra(10L, null, 'Z');
		
		gradeSol = new GradeDeMusica(11L, null, "G");
		gradeColcheia = new GradeDeMusica(12L, null, "Colcheia");
	}
	
	@Test
	public void deveSelecionarAGradeDeCor() throws RegraDeNegocioException {
		
		produto = new Produto(1L, "Teste");
		
		produto.adicionaGrade(gradeCor1);
		produto.adicionaGrade(gradeCor2);
		
		Grade gradeSelecionada = new GradeCor(Cor.ESMERALDA);
		Grade gradeLocalizada = null;
		
		boolean achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertFalse("Check que não achou a peça.", achou);
		
		gradeSelecionada = new GradeCor(Cor.APATITA);
		achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertTrue("Check que achou a peça.", achou);
		assertEquals("Check do codigo da grade", (int) 2L, gradeLocalizada.getCodigo().intValue());
	}
	
	@Test
	public void deveSelecionarAGradeDeTamanho() throws RegraDeNegocioException{
		
		produto = new Produto(1L, "Teste");
		
		produto.adicionaGrade(gradeTamanho1);
		produto.adicionaGrade(gradeTamanho2);
		
		Grade gradeSelecionada = new GradeTamanho(Tamanho.TAM_16);
		Grade gradeLocalizada = null;
		
		boolean achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertFalse("Check que não achou a peça.", achou);
		
		gradeSelecionada = new GradeTamanho(Tamanho.TAM_10);
		achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertTrue("Check que achou a peça.", achou);
		assertEquals("Check do codigo da grade", (int) 3L, gradeLocalizada.getCodigo().intValue());
	}
	
	@Test
	public void deveSelecionarAGradeDeCorETamanho() throws RegraDeNegocioException{
		
		produto = new Produto(1L, "Teste");
		
		produto.adicionaGrade(gradeCorETamanho1);
		produto.adicionaGrade(gradeCorETamanho2);
		
		Grade gradeSelecionada = new GradeCorETamanho(Cor.CITRINO_CONHAQUE, Tamanho.TAM_33);
		Grade gradeLocalizada = null;
		
		boolean achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertFalse("Check que não achou a peça.", achou);
		
		gradeSelecionada = new GradeCorETamanho(Cor.CITRINO_CONHAQUE, Tamanho.TAM_33);
		achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertFalse("Check que achou a peça.", achou);
		
		gradeSelecionada = new GradeCorETamanho(Cor.ESMERALDA, Tamanho.TAM_22);
		achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}		
		
		assertFalse("Check que achou a peça.", achou);
		
		gradeSelecionada = new GradeCorETamanho(Cor.CITRINO_CONHAQUE, Tamanho.TAM_22);
		achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}	
		
		assertTrue("Check que achou a peça.", achou);
		assertEquals("Check do codigo da grade", (int) 6L, gradeLocalizada.getCodigo().intValue());
	}
	
	@Test
	public void deveSelecionarAGradeLetra() throws RegraDeNegocioException{
		
		produto = new Produto(1L, "Teste");
		
		produto.adicionaGrade(gradeLetra1);
		produto.adicionaGrade(gradeLetra2);
		
		Grade gradeSelecionada = new GradeLetra('T');
		Grade gradeLocalizada = null;
		
		boolean achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertFalse("Check que não achou a peça.", achou);
		
		gradeSelecionada = new GradeLetra('Z');
		achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertTrue("Check que achou a peça.", achou);
		assertEquals("Check do codigo da grade", (int) 10L, gradeLocalizada.getCodigo().intValue());
	}
	
	@Test
	public void deveSelecionarAGradeUnica() throws RegraDeNegocioException{
		
		produto = new Produto(1L, "Teste");
		
		produto.adicionaGrade(gradeUnica1);
		
		Grade gradeSelecionada = new GradeTamanho();
		Grade gradeLocalizada = null;
		
		boolean achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertFalse("Check que não achou a peça.", achou);
		
		gradeSelecionada = new GradeUnica();
		achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertTrue("Check que achou a peça.", achou);
		assertEquals("Check do codigo da grade", (int) 7L, gradeLocalizada.getCodigo().intValue());
	}
	
	@Test
	public void deveTerAsMesmasCaracteristicas() {
		
		Grade gradeUnica = new GradeUnica(100L, null);
		assertTrue("Grade tem as mesmas caracteristicas", gradeUnica1.temAsMesmasCaracteristicas(gradeUnica));
		Grade gradeUnicaErrada = new GradeCor(200L, null, Cor.AMETISTA_ROSA);
		assertFalse("Grade tem as mesmas caracteristicas", gradeUnica1.temAsMesmasCaracteristicas(gradeUnicaErrada));
		
		Grade gradeCorCerta = new GradeCor(101L, null, Cor.AMETISTA);
		assertTrue("Grade tem as mesmas caracteristicas", gradeCor1.temAsMesmasCaracteristicas(gradeCorCerta));
		Grade gradeCorErrada = new GradeCor(201L, null, Cor.AMETISTA_ROSA);
		assertFalse("Grade tem as mesmas caracteristicas", gradeCor1.temAsMesmasCaracteristicas(gradeCorErrada));
		
		Grade gradeCorETamanhoCerta = new GradeCorETamanho(102L, null, Cor.C_ROSA_LEITOSO, Tamanho.TAM_20);
		assertTrue("Grade tem as mesmas caracteristicas", gradeCorETamanho1.temAsMesmasCaracteristicas(gradeCorETamanhoCerta));
		Grade gradeCorETamanhoErrada = new GradeCorETamanho(202L, null, Cor.C_ROSA_LEITOSO, Tamanho.TAM_22);
		assertFalse("Grade tem as mesmas caracteristicas", gradeCor1.temAsMesmasCaracteristicas(gradeCorETamanhoErrada));
		
		Grade gradeTamanhoCerta = new GradeTamanho(103L, null, Tamanho.TAM_10);
		assertTrue("Grade tem as mesmas caracteristicas", gradeTamanho1.temAsMesmasCaracteristicas(gradeTamanhoCerta));
		Grade gradeTamanhoErrada = new GradeTamanho(203L, null, Tamanho.TAM_22);
		assertFalse("Grade tem as mesmas caracteristicas", gradeTamanho1.temAsMesmasCaracteristicas(gradeTamanhoErrada));
		
		Grade gradeLetraCerta = new GradeLetra(104L, null, 'A');
		assertTrue("Grade tem as mesmas caracteristicas", gradeLetra1.temAsMesmasCaracteristicas(gradeLetraCerta));
		Grade gradeLetraErrada = new GradeLetra(205L, null, 'a');
		assertFalse("Grade tem as mesmas caracteristicas", gradeLetra1.temAsMesmasCaracteristicas(gradeLetraErrada));
		
		Grade outraGradeColcheia = new GradeDeMusica("Colcheia");
		assertTrue("Grade tem as mesmas caracteristicas", gradeColcheia.temAsMesmasCaracteristicas(outraGradeColcheia));
		assertFalse("Grade tem as mesmas caracteristicas", gradeColcheia.temAsMesmasCaracteristicas(gradeSol));
	}
	
	@Test
	public void deveSelecionarAGrade() throws RegraDeNegocioException {
		
		produto = new Produto(1L, "Teste");
		
		produto.adicionaGrade(gradeColcheia);
		
		Grade gradeSelecionada = new GradeCor(Cor.ESMERALDA);
		Grade gradeLocalizada = null;
		
		boolean achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertFalse("Check que não achou a peça.", achou);
		
		gradeSelecionada = new GradeDeMusica("Colcheia");
		achou = false;
		
		for (Grade grade : produto.getGrades()) {
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				achou = !achou;
				gradeLocalizada = grade;
			}
		}
		
		assertTrue("Check que achou a peça.", achou);
		assertEquals("Check do codigo da grade", (int) 12L, gradeLocalizada.getCodigo().intValue());
	}
	
	@Test
	public void deveValidar()
	{
		Grade gradeFa = new GradeDeMusica();
		
		try 
		{
			gradeFa.valida();
			fail("Não deve validar.");
		} catch (RegraDeNegocioException e) 
		{
			
		}
		
		gradeFa = new GradeDeMusica("");
		
		try 
		{
			gradeFa.valida();
			fail("Não deve validar.");
		} catch (RegraDeNegocioException e) 
		{
			
		}
		
		gradeFa = new GradeDeMusica("Fa");
		
		try 
		{
			gradeFa.valida();
		} catch (RegraDeNegocioException e) 
		{
			fail("Deve validar.");
		}
	}
}
