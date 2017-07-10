package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.Tamanho;

public class CarrinhoParaVendaTest {

	private Carrinho carrinho;
	
	private Produto anel;
	private Produto brinco;
	
	private Grupo grupoAnel;
	private Grupo grupoBrinco;
	
	private Grade corETamanho = new GradeCorETamanho(1L, Cor.AMARELO, Tamanho.TAM_23);
	private Grade cor = new GradeCor(2L, Cor.CITRINO_CHAMPAGNE);
	
	@Before
	public void init() throws RegraDeNegocioException
	{
		carrinho = new CarrinhoVenda();
		
		grupoAnel = new Grupo(1L, "Anéis");
		grupoBrinco = new Grupo(1L, "Brincos");
		
		anel = new Produto(1L, "Anel diverso");
		anel.setCodigoInterno("21497698SF06890");
		anel.setValor(new BigDecimal(68.90));
		anel.adicionaGrade(corETamanho);
		Subgrupo subAnel = new Subgrupo();
		subAnel.setGrupo(grupoAnel);
		anel.setSubgrupo(subAnel);
		
		brinco = new Produto(2L, "Brinco diverso");
		brinco.setCodigoInterno("21629419MV01490");
		brinco.setValor(new BigDecimal(14.90));
		brinco.adicionaGrade(cor);
		Subgrupo subBrinco = new Subgrupo();
		subBrinco.setGrupo(grupoBrinco);
		brinco.setSubgrupo(subBrinco);
	}
	
	private void adicionaItensBasicos() throws RegraDeNegocioException {
		carrinho.adicionaItem(new ItemCarrinho(anel, BigDecimal.ONE, anel.getValor(), corETamanho));
		carrinho.adicionaItem(new ItemCarrinho(brinco, BigDecimal.ONE, brinco.getValor(), cor));
	}
	
	@Test
	public void deveCriarUmCarrinhoComItensEResumo() 
	{
		assertTrue(carrinho.getItens().isEmpty());
		assertTrue(carrinho.getResumo().isEmpty());
	}
	
	@Test
	public void deveAdicionarItensAoCarrinho() throws RegraDeNegocioException 
	{
		adicionaItensBasicos();
		assertFalse(carrinho.getItens().isEmpty());
	}
	
	@Test
	public void deveVerificarSeExisteUmItem() throws RegraDeNegocioException 
	{
		adicionaItensBasicos();
		
		assertTrue(carrinho.jaExisteOItem(new ItemCarrinho(brinco, BigDecimal.ONE, brinco.getValor(), cor)).isPresent());
		assertFalse(carrinho.jaExisteOItem(new ItemCarrinho(brinco, BigDecimal.ONE, brinco.getValor(), new GradeCor(3L, Cor.APATITA))).isPresent());
		
		assertTrue(carrinho.jaExisteOItem(new ItemCarrinho(anel, BigDecimal.ONE, anel.getValor(), corETamanho)).isPresent());
		assertFalse(carrinho.jaExisteOItem(new ItemCarrinho(anel, BigDecimal.ONE, anel.getValor(), new GradeCorETamanho(4L, Cor.APATITA, Tamanho.TAM_6))).isPresent());
	}

	@Test
	public void deveRemoverUmItem() throws RegraDeNegocioException
	{
		adicionaItensBasicos();
		
		try
		{
			carrinho.removeItem(new ItemCarrinho(brinco, BigDecimal.ONE, brinco.getValor(), new GradeCor(3L, Cor.APATITA)));
			fail("Deve ser gerada uma exceção pois não existe o item.");
		} catch (RegraDeNegocioException ex)
		{		
			
		}
		
		try
		{
			carrinho.removeItem(new ItemCarrinho(brinco, BigDecimal.ONE, brinco.getValor(), cor));
			
		} catch (RegraDeNegocioException ex)
		{		
			fail("NÃO deve ser gerada uma exceção pois o item existe.");
		}

	}
	
	@Test
	public void deveCalcularOValorDosItens() throws RegraDeNegocioException
	{
		adicionaItensBasicos();
		
		assertEquals("Valor dos itens.", new BigDecimal("83.80"), carrinho.valorDosItens());
		
		carrinho.add(new ItemCarrinho(anel, new BigDecimal(2), anel.getValor(), new GradeCorETamanho(Cor.BRONZE, Tamanho.TAM_13)));
		assertEquals("Valor dos itens.", new BigDecimal("221.60"), carrinho.valorDosItens());
		
		carrinho.removeItem(new ItemCarrinho(anel, new BigDecimal(2), anel.getValor(), new GradeCorETamanho(Cor.BRONZE, Tamanho.TAM_13)));
		assertEquals("Valor dos itens.", new BigDecimal("83.80"), carrinho.valorDosItens());
	}
	
	@Test
	public void deveCalcularAQtdDeItens() throws RegraDeNegocioException
	{
		adicionaItensBasicos();
		assertEquals("Quantidade de itens.", new BigDecimal("2"), carrinho.qtdDeItens());
		
		carrinho.add(new ItemCarrinho(anel, new BigDecimal(2), anel.getValor(), new GradeCorETamanho(Cor.BRONZE, Tamanho.TAM_13)));
		assertEquals("Quandidate de itens.", new BigDecimal("4"), carrinho.qtdDeItens());
		
		carrinho.removeItem(new ItemCarrinho(anel, new BigDecimal(2), anel.getValor(), new GradeCorETamanho(Cor.BRONZE, Tamanho.TAM_13)));
		assertEquals("Quantidade de itens.", new BigDecimal("2"), carrinho.qtdDeItens());

	}
	
	
	@Test
	public void deveEsvaziarUmCarrinh() throws RegraDeNegocioException
	{
		adicionaItensBasicos();
		
		carrinho.esvazia();
		
		assertTrue(carrinho.getItens().isEmpty());
		assertTrue(carrinho.getResumo().isEmpty());
	}
}
