package net.mv.meuespaco.controller;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.Tamanho;

public class ListaProdutosAbstractBeanTest 
{
	private ListaProdutosAbstractBean listaBean;
	
	private Produto prodBrinco;
	private Produto prodAnel;
	private Produto prodAnelCaro;
	
	private Grade gradeBrinco;
	private Grade gradeAnel;
	private Grade gradeAnelCaro;
	
	@Before
	public void init() throws RegraDeNegocioException 
	{
		prodBrinco = new Produto(1L, "Brinco");
		gradeBrinco = new GradeCor(Cor.CRISTAL);
		prodBrinco.adicionaGrade(gradeBrinco);
		
		prodAnel = new Produto(2L, "Anel");
		gradeAnel = new GradeTamanho(Tamanho.TAM_24);
		prodAnel.adicionaGrade(gradeAnel);
		
		prodAnelCaro = new Produto(3L, "Anel Caro");
		gradeAnelCaro = new GradeCorETamanho(Cor.AMARELO, Tamanho.TAM_24);
		prodAnelCaro.adicionaGrade(gradeAnelCaro);
		
		prodBrinco.setSubgrupo(new Subgrupo(2L, "Brinco"));
		prodAnel.setSubgrupo(new Subgrupo(1L, "Anel"));
		prodAnelCaro.setSubgrupo(new Subgrupo(1L, "Anel"));
		
		listaBean = new ListaProdutosConsignadosBean();
	}
	
	@Test
	public void deveVerificarSeMostraTamanhos() 
	{
		List<Produto> produtos = Arrays.asList(prodBrinco, prodAnel, prodAnelCaro);
		
		listaBean.verificaSeMostraTamanhos(produtos);
		assertTrue("Deve mostrar", listaBean.isMostraTamanhos());

		produtos = Arrays.asList(prodBrinco);
		
		listaBean.verificaSeMostraTamanhos(produtos);
		assertFalse("NÃO deve mostrar", listaBean.isMostraTamanhos());
	}

}