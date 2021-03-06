package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeUnica;
import net.mv.meuespaco.model.grade.Tamanho;

public class ItemCarrinhoTest {
	
	private Produto anelDourado;
	private Produto brincoPendurado;
	private Produto correnteMasculina;
	
	private Produto embalagem;
	
	private Grupo grupoDescontavel = new Grupo(1L, "Descontavel");
	private Grupo grupoNaoDescontavel = new Grupo(1L, "Não Descontavel");
	
	private Subgrupo subDescontavel = new Subgrupo(1L, "Descontavel");
	private Subgrupo subNaoDescontavel = new Subgrupo(1L, "Não Descontavel");
	
	private List<ItemCarrinho> itensCarrinho = new ArrayList<ItemCarrinho>();

	@Before
	public void init() {
		
		grupoNaoDescontavel.setDescontavel(false);
		
		subDescontavel.setGrupo(grupoDescontavel);
		subNaoDescontavel.setGrupo(grupoNaoDescontavel);
		
		anelDourado = new Produto(1L, "Anel Dourado");
		anelDourado.setCodigoInterno("20884456MV03490");
		anelDourado.setValor(new BigDecimal(34.9));
		
		brincoPendurado = new Produto(2L, "Brinco Pendurado");
		brincoPendurado.setCodigoInterno("20884499MV10090");
		brincoPendurado.setValor(new BigDecimal(100.9));
		
		correnteMasculina = new Produto(3L, "Corrente Masculina");
		correnteMasculina.setCodigoInterno("21345622MV99090");
		correnteMasculina.setValor(new BigDecimal(990.9));
		
		embalagem = new Produto(4L, "Embalagem");
		embalagem.setCodigoInterno("22994567MV00060");
		embalagem.setValor(new BigDecimal(.6));
		
		anelDourado.setSubgrupo(subDescontavel);
		brincoPendurado.setSubgrupo(subDescontavel);
		correnteMasculina.setSubgrupo(subDescontavel);
		embalagem.setSubgrupo(subNaoDescontavel);
		
		itensCarrinho.add(new ItemCarrinho(anelDourado, BigDecimal.ONE, anelDourado.getValor(), new GradeCorETamanho(Cor.CITRINO_CONHAQUE, Tamanho.TAM_20)));
		itensCarrinho.add(new ItemCarrinho(brincoPendurado, new BigDecimal(2), brincoPendurado.getValor(), new GradeCor(Cor.AMETISTA_VERDE)));
		itensCarrinho.add(new ItemCarrinho(correnteMasculina, BigDecimal.ONE, correnteMasculina.getValor(), new GradeUnica()));
	}
	
	@Test
	public void deveRetornarOValorUnitario() {
		
		assertEquals("Check do valor do anel.", 
				new BigDecimal(34.90).setScale(2, RoundingMode.HALF_UP), itensCarrinho.get(0).getValorUnitario().setScale(2, RoundingMode.HALF_UP));
		
		assertEquals("Check do valor do brinco.", 
				new BigDecimal(100.90).setScale(2, RoundingMode.HALF_UP), itensCarrinho.get(1).getValorUnitario().setScale(2, RoundingMode.HALF_UP));
		
		assertEquals("Check do valor da corrente", 
				new BigDecimal(990.90).setScale(2, RoundingMode.HALF_UP), itensCarrinho.get(2).getValorUnitario().setScale(2, RoundingMode.HALF_UP));
		
		itensCarrinho.add(new ItemCarrinho(embalagem, BigDecimal.ONE, embalagem.getValor(), new GradeUnica()));
		assertEquals("Check do valor da embalagem", 
				new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP), itensCarrinho.get(3).getValorUnitario().setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test
	public void deveRetornarOValorTotal() {
		
		assertEquals("Check do valor do anel.", 
				new BigDecimal(34.90).setScale(2, RoundingMode.HALF_UP), itensCarrinho.get(0).valorTotal());
		
		assertEquals("Check do valor do brinco.", 
				new BigDecimal(201.80).setScale(2, RoundingMode.HALF_UP), itensCarrinho.get(1).valorTotal());
		
		assertEquals("Check do valor da corrente", 
				new BigDecimal(990.90).setScale(2, RoundingMode.HALF_UP), itensCarrinho.get(2).valorTotal());
		
		itensCarrinho.add(new ItemCarrinho(embalagem, BigDecimal.TEN, embalagem.getValor(), new GradeUnica()));
		assertEquals("Check do valor da embalagem", 
				new BigDecimal(6.00).setScale(2, RoundingMode.HALF_UP), itensCarrinho.get(3).valorTotal());
	}
	
	@Test(expected=RegraDeNegocioException.class)
	public void deveInvalidarItem() throws RegraDeNegocioException
	{
		
		ItemCarrinho itemInvalido1 = new ItemCarrinho(null, BigDecimal.ONE, null, new GradeUnica());
		itemInvalido1.valida();
		
		ItemCarrinho itemInvalido2 = new ItemCarrinho(anelDourado, BigDecimal.ZERO, anelDourado.getValor(), new GradeUnica());
		itemInvalido2.valida();
		
		ItemCarrinho itemInvalido3 = new ItemCarrinho(anelDourado, BigDecimal.ONE,  anelDourado.getValor(), null);
		itemInvalido3.valida();
		
		Produto anelDouradoSemValor = new Produto(51L, "Anel Dourado");
		
		ItemCarrinho itemInvalido4 = new ItemCarrinho(anelDouradoSemValor, BigDecimal.ONE, anelDouradoSemValor.getValor(), new GradeUnica());
		itemInvalido4.valida();		
	}

}
