package net.mv.meuespaco.service.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.service.ProdutoService;

public class ProdutoServiceImplTest {

	private Produto produto1;
	private Produto produto2;
	private Produto produto3;
	
	private Grade gradeCor1;
	private Grade gradeCor2;
	private Grade gradeTamanho1;
	private Grade gradeTamanho2;
	private Grade gradeTamanhoECor1;
	private Grade gradeTamanhoECor2;
	private Grade gradeTamanhoECor3;
	
	private ProdutoService produtoService;
	
	
	@Before
	public void init() throws RegraDeNegocioException {
		
		produtoService = new ProdutoServiceImpl();
		
		produto1 = new Produto(1L, "Brinco com Cor");
		produto2 = new Produto(1L, "Anel com Tamanho");
		produto3 = new Produto(1L, "Anel com Tamanho e Cor");
		
		gradeCor1 = new GradeCor(Cor.AMETISTA);
		gradeCor2 = new GradeCor(Cor.APATITA);
		
		produto1.adicionaGrade(gradeCor1);
		produto1.adicionaGrade(gradeCor2);
		
		gradeTamanho1 = new GradeTamanho(Tamanho.TAM_18);
		gradeTamanho2 = new GradeTamanho(Tamanho.TAM_22);
		
		produto2.adicionaGrade(gradeTamanho1);
		produto2.adicionaGrade(gradeTamanho2);
		
		gradeTamanhoECor1 = new GradeCorETamanho(Cor.AMETISTA, Tamanho.TAM_22);
		gradeTamanhoECor2 = new GradeCorETamanho(Cor.AMETISTA_VERDE, Tamanho.TAM_18);
		gradeTamanhoECor3 = new GradeCorETamanho(Cor.AMETISTA_VERDE, Tamanho.TAM_26);
		
		produto3.adicionaGrade(gradeTamanhoECor1);
		produto3.adicionaGrade(gradeTamanhoECor2);
		produto3.adicionaGrade(gradeTamanhoECor3);
	}
	
	
	public void deveInformarOsTamanhosDisponiveisDosProdutos() {
		
		assertTrue("Check se não há tamanho para o produto1", 
				produtoService.tamanhosDisponiveisDoProduto(produto1) == null);
		
		assertEquals("Check se há 2 tamanhos para o produto2", 
				produtoService.tamanhosDisponiveisDoProduto(produto2).size(), 2);
		
		assertEquals("Check se há 3 tamanhos para o produto3", 
				produtoService.tamanhosDisponiveisDoProduto(produto3).size(), 3);
		
	}
	
	public void deveInformarAsCoresDisponiveisDosProdutos() {
		
		assertEquals("Check que há 2 cores para o produto1", 
				produtoService.coresDisponiveisDoProduto(produto1).size(), 2);
		
		assertTrue("Check se há 2 cores para o produto2", 
				produtoService.coresDisponiveisDoProduto(produto2) == null);
		
		assertEquals("Check se há 2 cores sem repetição para o produto3", 
				produtoService.coresDisponiveisDoProduto(produto3).size(), 2);
	}
	
	public void deveFazerOFiltroDoTamanhoPelaCor() {
		
		assertEquals("Check que há 1 cor para o tamanho 22", 
				produtoService.filtraGradesPeloTamanho(produto3, Tamanho.TAM_22).size(), 1);
		
		assertEquals("Check que não há cor para o tamanho 20", 
				produtoService.filtraGradesPeloTamanho(produto3, Tamanho.TAM_20).size(), 0);
		
		assertTrue("Check que não há tamanho para o produto1", 
				produtoService.filtraGradesPeloTamanho(produto1, Tamanho.TAM_20) == null);
	}
	
	public void deveFazerOFiltroDaCorPeloTamanho() {
		
		assertEquals("Check que há 1 tamanho para a cor Azul", 
				produtoService.filtraGradesPorCor(produto3, Cor.AMETISTA).size(), 1);
		
		assertEquals("Check que há 2 tamanhos para a cor Verde", 
				produtoService.filtraGradesPorCor(produto3, Cor.AMETISTA_VERDE).size(), 2);
		
		assertTrue("Check que não há cor para o produto2",
				produtoService.filtraGradesPorCor(produto2, Cor.AMETISTA) == null);
	}


}
