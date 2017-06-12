package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;

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

public class CarrinhoTest {
	
	private Produto anelDourado;
	private Produto brincoPendurado;
	private Produto correnteMasculina;
	
	private Produto embalagem;
	
	private Grupo grupoDescontavel = new Grupo(1L, "Descontavel");
	private Grupo grupoNaoDescontavel = new Grupo(2L, "Não Descontavel");
	
	private Subgrupo subDescontavel = new Subgrupo(1L, "Descontavel");
	private Subgrupo subNaoDescontavel = new Subgrupo(2L, "Não Descontavel");
	
	private CarrinhoConsignado carrinho;
	
	ItemCarrinho itemAnel;
	ItemCarrinho itemBrinco;
	ItemCarrinho itemCorrente;
	ItemCarrinho itemEmbalagem;

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
		
		carrinho = new CarrinhoConsignado(new BigDecimal(1800), 45);
		
		itemAnel = new ItemCarrinho(anelDourado, BigDecimal.ONE, new GradeCorETamanho(Cor.CITRINO_CONHAQUE, Tamanho.TAM_20));
		itemBrinco = new ItemCarrinho(brincoPendurado, new BigDecimal(2), new GradeCor(Cor.AMETISTA_VERDE));
		itemCorrente = new ItemCarrinho(correnteMasculina, BigDecimal.ONE, new GradeUnica());
		itemEmbalagem =  new ItemCarrinho(embalagem, BigDecimal.TEN, new GradeUnica());
	}

	@Test
	public void deveRetornarAQtdDosItensDescontaveis() throws RegraDeNegocioException {
		carrinho.getItens().addAll(Arrays.asList(itemAnel, itemBrinco, itemCorrente, itemEmbalagem));
		
		assertEquals("Check do valor dos itens descontáveis.", 
				new BigDecimal(4).setScale(0, RoundingMode.HALF_UP), 
				carrinho.calculaQtdDescontavel());
	}
	
	@Test
	public void deveVerificarSeExisteOuNaoOItem()
	{
		carrinho.getItens().addAll(Arrays.asList(itemAnel, itemBrinco, itemCorrente, itemEmbalagem));
		itemCorrente = new ItemCarrinho(correnteMasculina, BigDecimal.ONE, new GradeUnica());
		
		Optional<ItemCarrinho> itemExistente1 = carrinho.jaExisteOItem(itemCorrente);
		assertTrue("Deve achar o item.", itemExistente1.isPresent());
		
		Produto produtoNovo = new Produto(10L, "Produto novo");
		produtoNovo.setCodigoInterno("2033456700050");
		
		ItemCarrinho itemNovo = new ItemCarrinho(produtoNovo, BigDecimal.ONE, new GradeUnica());
		
		Optional<ItemCarrinho> itemExistente2 = carrinho.jaExisteOItem(itemNovo);
		assertFalse("Deve achar o item.", itemExistente2.isPresent());
	}
	
	@Test
	public void deveCalcularOSaldo() throws RegraDeNegocioException
	{
		carrinho.getItens().addAll(Arrays.asList(itemAnel, itemBrinco, itemCorrente, itemEmbalagem));

		assertEquals("Deve informar o saldo.", new BigDecimal(41).intValue(), carrinho.saldoDeQuantidade());
	}
	
	@Test
	public void deveVerificarSeTemSaldoOuNaoParaAdicao() throws RegraDeNegocioException
	{
		carrinho.getItens().addAll(Arrays.asList(itemAnel, itemBrinco, itemCorrente, itemEmbalagem));
		
		assertTrue("Deve permitir a adição de item.", carrinho.haSaldoDeQtdParaAdicaoDoItem(new BigDecimal(30)));
		
		assertTrue("Deve permitir a adição de item.", carrinho.haSaldoDeQtdParaAdicaoDoItem(new BigDecimal(41)));
		
		assertFalse("Deve negar a adição de item.", carrinho.haSaldoDeQtdParaAdicaoDoItem(new BigDecimal(42)));
	}

	@Test
	public void deveAdicionarOItem() throws RegraDeNegocioException
	{
		carrinho.getItens().addAll(Arrays.asList(itemAnel, itemBrinco, itemCorrente, itemEmbalagem));
		
		Produto novaCorrenteMasculina = new Produto(3L, "Corrente Masculina");
		novaCorrenteMasculina.setCodigoInterno("21345622MV09090");
		novaCorrenteMasculina.setValor(new BigDecimal(90.90));
		
		Produto novaEmbalagem = new Produto(4L, "Embalagem");
		novaEmbalagem.setCodigoInterno("22994567MV00060");
		novaEmbalagem.setValor(new BigDecimal(.6));
		
		novaCorrenteMasculina.setSubgrupo(subDescontavel);
		novaEmbalagem.setSubgrupo(subNaoDescontavel);
		
		ItemCarrinho novoItemCorrente = new ItemCarrinho(novaCorrenteMasculina, new BigDecimal(2), new GradeUnica());
		ItemCarrinho novoItemEmbalagem =  new ItemCarrinho(novaEmbalagem, BigDecimal.TEN, new GradeUnica());
		
		carrinho.adicionaItem(novoItemCorrente);
		carrinho.adicionaItem(novoItemEmbalagem);
		
		assertEquals("Check da quantidade.", new BigDecimal(26), carrinho.qtdDeItens());
		
		ItemCarrinho maisEmbalagem =  new ItemCarrinho(novaEmbalagem, BigDecimal.TEN, new GradeUnica());
		carrinho.adicionaItem(maisEmbalagem);
		
		ItemCarrinho maisCorrente = new ItemCarrinho(novaCorrenteMasculina, new BigDecimal(41), new GradeUnica());
		
		try {
			carrinho.adicionaItem(maisCorrente);
			fail("Deveria ter lançado a exceção.");
		} catch (RegraDeNegocioException ex) {
			
		}
	}
	
	@Test
	public void deveRemoverUmItem() throws RegraDeNegocioException
	{
		carrinho.getItens().addAll(Arrays.asList(itemAnel, itemBrinco, itemCorrente, itemEmbalagem));
		
		carrinho.removeItem(itemBrinco);
		assertEquals("Check da quantidade.", new BigDecimal(12), carrinho.qtdDeItens());
		
		Produto novaCorrenteMasculina = new Produto(3L, "Corrente Masculina");
		novaCorrenteMasculina.setCodigoInterno("21345622MV99090");
		novaCorrenteMasculina.setSubgrupo(subDescontavel);
		novaCorrenteMasculina.setValor(new BigDecimal(990.9));
		
		ItemCarrinho novoItemCorrente = new ItemCarrinho(novaCorrenteMasculina, new BigDecimal(41), new GradeCor(Cor.AMARELO));
		
		try {
			carrinho.removeItem(novoItemCorrente);
			fail("Deveria ter lançado a exceção.");
		} catch (RegraDeNegocioException ex) {
			
		}
		
	}
	
	@Test
	public void deveInformarOResumo() throws RegraDeNegocioException 
	{
		carrinho.adicionaItem(itemAnel);
		carrinho.adicionaItem(itemBrinco);
		carrinho.adicionaItem(itemCorrente);
		carrinho.adicionaItem(itemEmbalagem);
		
		assertEquals("Deve criar o resumo.", 2, carrinho.getResumo().size());
		assertEquals("Deve criar o resumo.", new BigDecimal(4), carrinho.getResumo().get(grupoDescontavel));
		assertEquals("Deve criar o resumo.", new BigDecimal(10), carrinho.getResumo().get(grupoNaoDescontavel));
		
		Produto novaCorrenteMasculina = new Produto(3L, "Corrente Masculina");
		novaCorrenteMasculina.setCodigoInterno("21345622MV10090");
		novaCorrenteMasculina.setValor(new BigDecimal(100.9));
		novaCorrenteMasculina.setSubgrupo(subDescontavel);
		
		ItemCarrinho novoItemCorrente = new ItemCarrinho(novaCorrenteMasculina, new BigDecimal(1), new GradeCor(Cor.AMARELO));
		carrinho.adicionaItem(novoItemCorrente);
		
		assertEquals("Deve criar o resumo.", new BigDecimal(5), carrinho.getResumo().get(grupoDescontavel));
		
		carrinho.removeItem(novoItemCorrente);
		assertEquals("Deve atualizar o resumo.", new BigDecimal(4), carrinho.getResumo().get(grupoDescontavel));
		
		carrinho.esvazia();
		assertTrue("Check do resumo vazio.", carrinho.getResumo().isEmpty());
	}
	
	@Test
	public void deveCalcularOValorDescontavel() throws RegraDeNegocioException
	{
		carrinho.adicionaItem(itemAnel);
		carrinho.adicionaItem(itemBrinco);
		carrinho.adicionaItem(itemCorrente);
		carrinho.adicionaItem(itemEmbalagem);
		
		assertEquals("Deve calcular o valor descontável.", 
				new BigDecimal(1227.60).setScale(2, RoundingMode.HALF_UP), carrinho.calculaValorDescontavel());
	}
	
	@Test
	public void deveCalcularSaldoDeValor() throws RegraDeNegocioException
	{
		carrinho.adicionaItem(itemAnel);
		carrinho.adicionaItem(itemBrinco);
		carrinho.adicionaItem(itemCorrente);
		carrinho.adicionaItem(itemEmbalagem);
		
		assertEquals("Deve calcular o saldo do valor.", 
				new BigDecimal(572.40).setScale(2, RoundingMode.HALF_UP), carrinho.calculaSaldoDeValor());
	}
	
	@Test
	public void deveVerificarSePodeOuNaoInserirItemPeloValor() throws RegraDeNegocioException
	{
		carrinho.adicionaItem(itemAnel);
		carrinho.adicionaItem(itemBrinco);
		carrinho.adicionaItem(itemCorrente);
		carrinho.adicionaItem(itemEmbalagem);
		
		Produto novaCorrenteMasculina = new Produto(6L, "Corrente Masculina");
		novaCorrenteMasculina.setCodigoInterno("21345622MV99090");
		novaCorrenteMasculina.setSubgrupo(subDescontavel);
		novaCorrenteMasculina.setValor(new BigDecimal(990.9));
		
		ItemCarrinho novoItemCorrente = new ItemCarrinho(novaCorrenteMasculina, new BigDecimal(41), new GradeCor(Cor.AMARELO));
		
		try
		{
			carrinho.adicionaItem(novoItemCorrente);
			fail("Deve ser lançada uma exceção impedindo a adição do item pelo saldo do valor.");

		} catch (RegraDeNegocioException ex) 
		{
			
		}
		
		Produto novoAnel = new Produto(7L, "Anel Masculina");
		novoAnel.setCodigoInterno("21345622MV57240");
		novoAnel.setSubgrupo(subDescontavel);
		novoAnel.setValor(new BigDecimal(572.40));
		
		ItemCarrinho novoItemAnel = new ItemCarrinho(novoAnel, new BigDecimal(1), new GradeCor(Cor.AMARELO));
		
		try
		{
			carrinho.adicionaItem(novoItemAnel);
		} catch (RegraDeNegocioException ex) 
		{
			fail("Deve ser adicionado o item pois há saldo suficiente para tal.");
		}
	}
	
	@Test
	public void deveCalcularQtdDeItensDoCarrinhoVazio()
	{
		assertEquals("Check da quantidade.", new BigDecimal(0), carrinho.qtdDeItens());
	}
	
	@Test
	public void deveVerificarSeOCarrinhoEstaOuNaoVazio() throws RegraDeNegocioException
	{
		assertTrue("Carrinho vazio", carrinho.isVazio());
		carrinho.add(itemAnel);
		assertFalse("Carrinho não vazio", carrinho.isVazio());
	}
	
	@Test
	public void deveCalcularOValorDoDesconto() throws RegraDeNegocioException
	{
		carrinho.add(itemAnel);
		
		carrinho.setDesconto(new BigDecimal(10));
		
		assertEquals("Valor do Desconto.", new BigDecimal(3.49).setScale(2,RoundingMode.HALF_UP), 
				carrinho.valorDoDesconto().setScale(2,RoundingMode.HALF_UP));
	}
	
	@Test
	public void deveCalcularOValorComDesconto() throws RegraDeNegocioException
	{
		carrinho.add(itemAnel);
		
		carrinho.setDesconto(new BigDecimal(10));
		
		assertEquals("Valor com Desconto.", new BigDecimal(31.41).setScale(2,RoundingMode.HALF_UP), 
				carrinho.valorComDesconto().setScale(2,RoundingMode.HALF_UP));
	}
}
