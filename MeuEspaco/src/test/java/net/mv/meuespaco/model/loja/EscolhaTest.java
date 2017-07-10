package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.factory.EscolhaFactory;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeUnica;
import net.mv.meuespaco.model.grade.Tamanho;

/**
 * Teste do escolha com inclusão de itens do carrinho e  
 * quantidade de itens.
 * 
 * @author Sidronio
 * 08/01/16
 */
public class EscolhaTest {
	
	private Produto anelDourado;
	private Produto brincoPendurado;
	private Produto correnteMasculina;
	
	private Produto embalagem;
	
	private Grupo grupoDescontavel = new Grupo(1L, "Descontavel");
	private Grupo grupoNaoDescontavel = new Grupo(1L, "Não Descontavel");
	
	private Subgrupo subDescontavel = new Subgrupo(1L, "Descontavel");
	private Subgrupo subNaoDescontavel = new Subgrupo(1L, "Não Descontavel");
	
	private List<ItemCarrinho> itensCarrinho = new ArrayList<ItemCarrinho>();
	
	private Escolha escolha;
	
	@Before
	public void init() {
		
		grupoNaoDescontavel.setDescontavel(false);
		
		subDescontavel.setGrupo(grupoDescontavel);
		subNaoDescontavel.setGrupo(grupoNaoDescontavel);
		
		anelDourado = new Produto(1L, "Anel Dourado");
		anelDourado.setCodigoInterno("20884456MV03490");
		
		brincoPendurado = new Produto(2L, "Brinco Pendurado");
		brincoPendurado.setCodigoInterno("20884499MV10090");
		
		correnteMasculina = new Produto(3L, "Corrente Masculina");
		correnteMasculina.setCodigoInterno("21345622MV99090");
		
		embalagem = new Produto(4L, "Embalagem");
		embalagem.setCodigoInterno("22994567MV00060");
		
		anelDourado.setSubgrupo(subDescontavel);
		brincoPendurado.setSubgrupo(subDescontavel);
		correnteMasculina.setSubgrupo(subDescontavel);
		embalagem.setSubgrupo(subNaoDescontavel);
		
		itensCarrinho.add(new ItemCarrinho(anelDourado, BigDecimal.ONE,  anelDourado.getValor(), new GradeCorETamanho(Cor.CITRINO_CONHAQUE,Tamanho.TAM_20)));
		itensCarrinho.add(new ItemCarrinho(brincoPendurado, new BigDecimal(2), brincoPendurado.getValor(), new GradeCor(Cor.AMETISTA_VERDE)));
		itensCarrinho.add(new ItemCarrinho(correnteMasculina, BigDecimal.ONE, correnteMasculina.getValor(), new GradeUnica()));
	}
	
	@Test
	public void deveCriarCarrinhoComItens() throws RegraDeNegocioException {
		
		escolha = new EscolhaFactory()
				.doCliente(new Cliente(1L, "Sidronio"))
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(5)
				.comOsItens(itensCarrinho)
				.cria();
		
		assertTrue("Pedio foi criado.", null != escolha);
		assertTrue("Escolha tem itens.", !escolha.getItens().isEmpty());
	}
	
	@Test
	public void deveInformarAQtdDeItens() throws RegraDeNegocioException {
		
		escolha = new EscolhaFactory()
				.doCliente(new Cliente(1L, "Sidronio"))
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(5)
				.comOsItens(itensCarrinho)
				.cria();
		
		assertEquals("Quantidade de itens.", new BigDecimal(4), escolha.qtdDeItens());
		
	}
	
	@Test
	public void deveAtualizarAQuantidadeDeUmItem() throws RegraDeNegocioException {
		
		escolha = new EscolhaFactory()
				.doCliente(new Cliente(1L, "Sidronio"))
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(50)
				.comOsItens(itensCarrinho)
				.cria();
		
		List<ItemCarrinho> novoCarrinho = new ArrayList<ItemCarrinho>();
		
		novoCarrinho.add(new ItemCarrinho(correnteMasculina, new BigDecimal(22), correnteMasculina.getValor(), new GradeUnica()));
		novoCarrinho.add(new ItemCarrinho(brincoPendurado, new BigDecimal(2), brincoPendurado.getValor(), new GradeCor(Cor.CITRINO_CONHAQUE)));
		
		escolha.adicionaItensDoCarrinho(novoCarrinho);
		
		assertEquals("Número de itens.", new BigDecimal(28), escolha.qtdDeItens());
		assertEquals("Item atualizado.", new BigDecimal(23), escolha.getItens().get(2).getQtd());
		
		escolha.getItens().forEach(i -> System.out.println(i));
	}

	@Test
	public void deveRetornarOStatusCorreto() throws RegraDeNegocioException {
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		cliente.setQtdDePecasParaEscolha(100f);
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comEnivoPara(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
				.comQtdMaximaDeItens(5)
				.comOsItens(itensCarrinho)
				.cria();
		
		assertEquals("O status deve ser inicial (Parcial).", StatusEscolha.PARCIAL, escolha.getStatus());
		
		escolha.setDataEnvio(LocalDateTime.now().minusDays(2));
		
		assertEquals("O status deve ser Enviado.", StatusEscolha.PARCIAL, escolha.getStatus());
	}
	
	@Test
	public void deveAtenderUmaEscolha() throws RegraDeNegocioException {
		
		escolha = new EscolhaFactory()
				.doCliente(new Cliente(1L, "Sidronio"))
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(5)
				.comOsItens(itensCarrinho)
				.cria();
		
		ItemEscolha item1 = escolha.getItens().get(0);
		ItemEscolha item2 = escolha.getItens().get(1);
		ItemEscolha item3 = escolha.getItens().get(2);
		
		escolha.setDataEnvio(LocalDateTime.now().minusDays(2));
		
		item1.setQtdAtendido(BigDecimal.ONE);
		item2.setQtdAtendido(new BigDecimal(2));
		item3.setQtdAtendido(BigDecimal.ONE);
		
		assertTrue("O item 1 foi atendido.", item1.isAtendido());
		assertTrue("O item 2 foi atendido.", item2.isAtendido());
		assertTrue("O item 3 foi atendido.", item3.isAtendido());
		
		escolha.atende();
		LocalDateTime horaDeFinalizacao = LocalDateTime.now();
		
		assertTrue("Todos os itens foram atendidos.", escolha.foramTodosOsItensAtendidos());
		assertEquals("O status deve ser atendida.", StatusEscolha.FINALIZADA, escolha.getStatus());
		assertEquals("A data de envio deve estar preechida.", horaDeFinalizacao, escolha.getDataFinalizacao());
	}
	
	
	@Test
	public void deveAtenderParcialmenteUmaEscolha() throws RegraDeNegocioException {
		
		escolha = new EscolhaFactory()
				.doCliente(new Cliente(1L, "Sidronio"))
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(5)
				.comOsItens(itensCarrinho)
				.cria();
		
		ItemEscolha item1 = escolha.getItens().get(0);
		ItemEscolha item2 = escolha.getItens().get(1);
		ItemEscolha item3 = escolha.getItens().get(2);
		
		escolha.setDataEnvio(LocalDateTime.now().minusDays(2));
		
		item1.setQtdAtendido(BigDecimal.ONE);
		item2.setQtdAtendido(new BigDecimal(1));
		item3.setQtdAtendido(BigDecimal.ONE);
		
		assertTrue("O item 1 foi atendido.", item1.isAtendido());
		assertFalse("O item 2 foi atendido.", item2.isAtendido());
		assertTrue("O item 3 foi atendido.", item3.isAtendido());
		
		escolha.atende();
		
		assertFalse("Todos os itens foram atendidos.", escolha.foramTodosOsItensAtendidos());
		assertEquals("O status deve ser atendida.", StatusEscolha.EM_SEPARACAO, escolha.getStatus());
		assertTrue("A data de envio não deve estar preenchida.", null == escolha.getDataFinalizacao());
	}
	
	@Test
	public void deveAtenderOuCancelarUmItem() throws RegraDeNegocioException {
		
		escolha = new EscolhaFactory()
				.doCliente(new Cliente(1L, "Sidronio"))
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(5)
				.comOsItens(itensCarrinho)
				.cria();
		
		ItemEscolha item1 = escolha.getItens().get(0);
		ItemEscolha item2 = escolha.getItens().get(1);
		ItemEscolha item3 = escolha.getItens().get(2);
		
		escolha.setDataEnvio(LocalDateTime.now().minusDays(2));
		
		item1.atendeOuCancelaAtendimento();
		item2.atendeOuCancelaAtendimento();
		item3.atendeOuCancelaAtendimento();
		
		assertTrue("O item 1 foi atendido.", item1.isAtendido());
		assertTrue("O item 2 foi atendido.", item2.isAtendido());
		assertTrue("O item 3 foi atendido.", item3.isAtendido());
		
		item1.atendeOuCancelaAtendimento();
		item2.atendeOuCancelaAtendimento();
		item3.atendeOuCancelaAtendimento();
		
		assertFalse("O item 1 foi atendido.", item1.isAtendido());
		assertFalse("O item 2 foi atendido.", item2.isAtendido());
		assertFalse("O item 3 foi atendido.", item3.isAtendido());
	}
	
	@Test
	public void deveEnviarRequisicaoSeQtdForAMaximaPermitida() throws RegraDeNegocioException {
		
		//Atualiza a data.
		Semana semana = new Semana(
				LocalDate.now().minus(5, ChronoUnit.DAYS), 
				LocalDate.now().plus(1, ChronoUnit.DAYS));
		
		Regiao reg = new Regiao(1L);
		reg.setSemana(semana);
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		cliente.setRegiao(reg);
		cliente.setQtdDePecasParaEscolha(100f);
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(40)
				.comEnivoPara(LocalDateTime.now())
				.comOsItens(itensCarrinho)
				.cria();
		
		assertEquals("Check que a escolha ainda NÃO foi enviada.", StatusEscolha.PARCIAL, escolha.getStatus());
		
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.cria();
		
		escolha.adicionaItensDoCarrinho(itensCarrinho);
		
		DateTimeFormatter dtf = new DateTimeFormatterBuilder().appendPattern("dd/MM/yyyy hh:mm").toFormatter();
		
		LocalDateTime agora = LocalDateTime.now();
		
		LocalDateTime horarioEscola =  escolha.getDataEnvio();
		
		assertEquals("Check que a escolha ainda NÃO foi enviada.", StatusEscolha.ENVIADA, escolha.getStatus());
		assertEquals("Check da data de envio da escolha.", agora.format(dtf), horarioEscola.format(dtf));
				
	}
	
	@Test
	public void deveAtenderTodosOsItens() throws RegraDeNegocioException {
		
		Semana semana = new Semana(LocalDate.parse("2016-03-10"), LocalDate.parse("2016-03-20"));
		Regiao reg = new Regiao(1L);
		reg.setSemana(semana);
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		cliente.setRegiao(reg);
		cliente.setQtdDePecasParaEscolha(100f);
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.cria();
		
		assertEquals("Check de qtd de itens atendidos.", BigDecimal.ZERO, escolha.qtdDeItensAtendidos());
		
		ItemEscolha item1 = escolha.getItens().get(0);
		ItemEscolha item2 = escolha.getItens().get(1);
		
		item1.atendeOuCancelaAtendimento();
		item2.atendeOuCancelaAtendimento();
		
		assertEquals("Check de qtd de itens atendidos.", new BigDecimal(3), escolha.qtdDeItensAtendidos());
		
	}
	
	@Test
	public void deveAtenderTodosItens() throws RegraDeNegocioException {
		
		Semana semana = new Semana(LocalDate.parse("2016-03-10"), LocalDate.parse("2016-03-20"));
		Regiao reg = new Regiao(1L);
		reg.setSemana(semana);
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		cliente.setRegiao(reg);
		cliente.setQtdDePecasParaEscolha(100f);
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.cria();
		
		escolha.atendeTodosItens();
		assertEquals("Check de qtd de itens atendidos.", new BigDecimal(4), escolha.qtdDeItensAtendidos());
		
		escolha.atendeTodosItens();
		assertEquals("Check de qtd de itens atendidos.", new BigDecimal(0), escolha.qtdDeItensAtendidos());
	}
	
	
	@Test
	public void deveInformarAQtdDeItensDescontaveis() throws RegraDeNegocioException {
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.cria();
		
		escolha.adicionaItem(embalagem, BigDecimal.TEN, new GradeUnica());
		
		assertEquals("Check de qtd de itens descontáveis.", new BigDecimal(4), escolha.qtdDeItensDescontaveis());
	}
	
	@Test
	public void deveInformarAQtdDeItensNaoDescontaveis() throws RegraDeNegocioException {
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.cria();
		
		escolha.adicionaItem(embalagem, BigDecimal.TEN, new GradeUnica());
		
		assertEquals("Check de qtd de itens não descontáveis.", new BigDecimal(10), escolha.qtdDeItensNaoDescontaveis());
	}
	
	@Test
	public void deveCalcularOValorDeCadaItem() throws RegraDeNegocioException {
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.cria();
		
		escolha.adicionaItem(embalagem, BigDecimal.TEN, new GradeUnica());
		
		ItemEscolha item1 = escolha.getItens().get(0);
		ItemEscolha item2 = escolha.getItens().get(1);
		ItemEscolha item3 = escolha.getItens().get(2);
		ItemEscolha item4 = escolha.getItens().get(3);
		
		assertEquals("Check valor dos itens.", new BigDecimal(34.90).setScale(2, RoundingMode.HALF_UP), item1.getValorUnitario()); //34,90
		assertEquals("Check valor dos itens.", new BigDecimal(100.90).setScale(2, RoundingMode.HALF_UP), item2.getValorUnitario()); //100,90
		assertEquals("Check valor dos itens.", new BigDecimal(990.90).setScale(2, RoundingMode.HALF_UP), item3.getValorUnitario()); //990,90
		assertEquals("Check valor dos itens.", new BigDecimal(0.60).setScale(2, RoundingMode.HALF_UP), item4.getValorUnitario()); //0,60
		
		assertEquals("Check valor dos itens.", new BigDecimal(34.90).setScale(2, RoundingMode.HALF_UP), item1.valorTotal());
		assertEquals("Check valor dos itens.", new BigDecimal(201.80).setScale(2, RoundingMode.HALF_UP), item2.valorTotal());
		assertEquals("Check valor dos itens.", new BigDecimal(990.90).setScale(2, RoundingMode.HALF_UP), item3.valorTotal());
		assertEquals("Check valor dos itens.", new BigDecimal(6.00).setScale(2, RoundingMode.HALF_UP), item4.valorTotal());
	}
	
	@Test
	public void deveCalcularOValorDaEscolha() throws RegraDeNegocioException {
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.cria();
		
		escolha.adicionaItem(embalagem, BigDecimal.TEN, new GradeUnica());
		
		assertEquals("Check valor dos itens da escolha.", new BigDecimal(1233.60).setScale(2, RoundingMode.HALF_UP), escolha.valorDosItens());
	}
	
	@Test
	public void deveCalcularOValorDescontavelDaEscolha() throws RegraDeNegocioException {
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.cria();
		
		escolha.adicionaItem(embalagem, BigDecimal.TEN, new GradeUnica());
		
		assertEquals("Check valor dos itens da escolha.", new BigDecimal(1227.60).setScale(2, RoundingMode.HALF_UP), escolha.valorDosItensDescontaveis());
	}
	
	@Test
	public void deveCalcularOSaldoDoValorDisponvielParaEscolha() throws RegraDeNegocioException {
		
		Cliente cliente = new Cliente(1L, "Sidronio");
		cliente.setValorParaEscolha(3500f);
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(4)
				.comOsItens(itensCarrinho)
				.comOValorMaximo(new BigDecimal(cliente.getValorParaEscolha()))
				.cria();
		
		escolha.adicionaItem(embalagem, BigDecimal.TEN, new GradeUnica());
		
		System.out.println("Valor dos itens descontáveis: " + escolha.valorDosItensDescontaveis());
		
		assertEquals("Check saldo do valor.", 
				new BigDecimal(2272.40).setScale(2, RoundingMode.HALF_UP), 
				escolha.calculaSaldoDoValorPermitido());
	}
	
	@Test
	public void deveAlterarOStatusAoFinalizarUmaEscolhaParcial() throws RegraDeNegocioException
	{
		Cliente cliente = new Cliente(1L, "Sidronio");
		cliente.setValorParaEscolha(3500f);
		
		escolha = new EscolhaFactory()
				.doCliente(cliente)
				.naData(LocalDateTime.now())
				.comQtdMaximaDeItens(5)
				.comEnivoPara(LocalDateTime.now().plus(3, ChronoUnit.DAYS))
				.comOsItens(itensCarrinho)
				.comOValorMaximo(new BigDecimal(cliente.getValorParaEscolha()))
				.cria();
		
		escolha.adicionaItem(embalagem, BigDecimal.TEN, new GradeUnica());
		
		assertEquals("Check do status Parcial.", StatusEscolha.PARCIAL, escolha.getStatus());
		assertTrue("Check da data de envio.", 
				LocalDateTime.now().toLocalDate().plus(3, ChronoUnit.DAYS).compareTo(escolha.getDataEnvio().toLocalDate()) == 0);
		
		escolha.adicionaItensDoCarrinho(Arrays.asList(new ItemCarrinho(correnteMasculina, BigDecimal.ONE, correnteMasculina.getValor(), new GradeUnica())));

		assertEquals("Check do status Enviado.", StatusEscolha.ENVIADA, escolha.getStatus());
		assertTrue("Check da data de envio.", LocalDateTime.now().toLocalDate().compareTo(escolha.getDataEnvio().toLocalDate()) == 0 );
	}
}
