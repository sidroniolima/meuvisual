package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.GradeUnica;
import net.mv.meuespaco.model.grade.Tamanho;

public class VendaTest {
	
	private Produto brinco;
	private Grade gradeBrinco;
	
	private Produto anel;
	private Grade gradeAnel;
	
	private Produto corrente;
	private Grade gradeCorrente;
	
	@Before
	public void init()
	{
		brinco = new Produto(1L, "Brinco");
		brinco.setCodigoInterno("21008976MV09990");
		gradeBrinco = new GradeCorETamanho(Cor.AMARELO, Tamanho.TAM_22);
		
		anel = new Produto(2L, "Anel");
		anel.setCodigoInterno("20894356MV03390");
		gradeAnel = new GradeTamanho(Tamanho.TAM_18);

		corrente = new Produto(3L, "Corrente");
		corrente.setCodigoInterno("20784512MV14900");
		gradeCorrente = new GradeUnica();
		
		try {
			brinco.adicionaGrade(gradeBrinco);
			anel.adicionaGrade(gradeAnel);
			corrente.adicionaGrade(gradeCorrente);
		} catch (RegraDeNegocioException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deveCalcularOValorDaCompra() {
		
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();

		Venda venda2 = new VendaBuilder().comCodigo(2L)
				.doCliente(new Cliente(2L, "Maria"))
				.noHorario(LocalDateTime.now())
				.doProduto(anel, new BigDecimal(1), gradeAnel)
				.constroi();
		
		assertEquals("Valor da compra", new BigDecimal(99.90).setScale(2, RoundingMode.HALF_UP), venda1.valor().setScale(2, RoundingMode.HALF_UP));
		assertEquals("Valor da compra", new BigDecimal(33.90).setScale(2, RoundingMode.HALF_UP), venda2.valor().setScale(2, RoundingMode.HALF_UP));
		
		assertEquals("Valor da compra", new BigDecimal(84.92).setScale(2, RoundingMode.HALF_UP), venda1.valorComDesconto().setScale(2, RoundingMode.HALF_UP));
		assertEquals("Valor da compra", new BigDecimal(33.90).setScale(2, RoundingMode.HALF_UP), venda2.valorComDesconto().setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test
	public void deveCalcularAQuantidadeDosItens()
	{
		
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertEquals("Qtd de itens da compra", new BigDecimal(1), venda1.qtdDeItens());
	}

	@Test
	public void deveAdicionarItemOuAtualizarAQuantidadeDeUmJaExistente()
	{
		
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.doProduto(brinco, BigDecimal.ONE, gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertEquals("Quantidade dos itens da venda", 1, venda1.getItens().size());
		
		venda1.addItem(anel, new BigDecimal(1), gradeAnel);
		
		assertEquals("Quantidade dos itens da venda", 2, venda1.getItens().size());
		
		assertEquals("Qtd do Item 1", new BigDecimal(2), venda1.getItens().get(0).getQtd());
		assertEquals("Qtd do Item 2", new BigDecimal(1), venda1.getItens().get(1).getQtd());
		
		assertEquals("Qtd de itens", new BigDecimal(3), venda1.qtdDeItens());
		
		assertEquals("Valor da compra", new BigDecimal(198.65).setScale(2, RoundingMode.HALF_UP), 
				venda1.valorComDesconto().setScale(2, RoundingMode.HALF_UP));
		
		venda1.addItem(anel, new BigDecimal(1), new GradeTamanho(Tamanho.TAM_13));
		assertEquals("Quantidade dos itens da venda", 3, venda1.getItens().size());
		
		assertEquals("Qtd do Item 1", new BigDecimal(2), venda1.getItens().get(0).getQtd());
		assertEquals("Qtd do Item 2", new BigDecimal(1), venda1.getItens().get(1).getQtd());
		assertEquals("Qtd do Item 3", new BigDecimal(1), venda1.getItens().get(2).getQtd());
		
		assertEquals("Qtd de itens", new BigDecimal(4), venda1.qtdDeItens());
		
		assertEquals("Valor da compra", new BigDecimal(227.46).setScale(2, RoundingMode.HALF_UP), 
				venda1.valorComDesconto().setScale(2, RoundingMode.HALF_UP));
	}
	
	@Test
	public void deveRemoverUmItem()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.doProduto(brinco, BigDecimal.ONE, gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		venda1.addItem(anel, BigDecimal.ONE, gradeAnel);
		
		venda1.removeItem(brinco, gradeBrinco);
		
		assertEquals("Quantidade dos itens da venda", BigDecimal.ONE, venda1.qtdDeItens());
		
		assertEquals("Item restante", venda1.getItens().get(0).getProduto().getCodigoInterno(), 
				anel.getCodigoInterno());
		
		assertEquals("Qtd de itens", new BigDecimal(1), venda1.qtdDeItens());
	}
	
	@Test
	public void naoDeveRemoverUmItemInexistente()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.doProduto(brinco, BigDecimal.ONE, gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		venda1.removeItem(anel, gradeAnel);
		venda1.removeItem(brinco, new GradeCorETamanho(Cor.AMETISTA_VERDE, Tamanho.TAM_20));
		
		assertEquals("Quantidade dos itens da venda", 1, venda1.getItens().size());
		
		assertEquals("Item restante", venda1.getItens().get(0).getProduto().getCodigoInterno(), 
				brinco.getCodigoInterno());
		
		assertEquals("Qtd de itens", new BigDecimal(2), venda1.qtdDeItens());
	}
	
	@Test
	public void deveVerificarSeOItemEstaOuNaoPresente()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.doProduto(brinco, BigDecimal.ONE, gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		ItemVenda item = new ItemVenda(brinco, gradeBrinco);
		assertTrue("Deve achar o item", venda1.achaItem(item).isPresent());
		
		ItemVenda item2 = new ItemVenda(brinco, new GradeCorETamanho(Cor.AMARELO, Tamanho.TAM_11));
		assertFalse("Não deve achar o item", venda1.achaItem(item2).isPresent());
	}
	
	@Test
	public void deveValidarUmaVenda()
	{
		Venda venda1 = new Venda();
		
		try {
			venda1.valida();
			fail("Venda inválida.");
		} catch (RegraDeNegocioException e) {
			assertTrue("A venda deve ser para um cliente.".equals(e.getMessage()));
		}
		
		venda1.setCliente(new Cliente(1L, "Cliente"));
		
		try {
			venda1.valida();
			fail("Venda inválida.");
		} catch (RegraDeNegocioException e) {
			assertTrue("A venda deve conter ao menos um item.".equals(e.getMessage()));
		}
		
		venda1.addItem(brinco, BigDecimal.ONE, gradeBrinco);
		
		try {
			venda1.valida();
			
			assertEquals(StatusVenda.AGUARDANDO_PAGAMENTO, venda1.getStatus());
			
		} catch (RegraDeNegocioException e) {
			fail("Venda válida.");
		}
	}
	
	@Test
	public void deveInformarQueAVendaEstaPaga()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		venda1.registraPagamento("0000", LocalDateTime.parse("2016-11-23T08:50:40"));
		
		assertTrue("Venda paga", venda1.isPaga());
	}
	
	@Test
	public void deveInformarQueAVendaNaoEstaPaga()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertFalse("Venda NÃO paga", venda1.isPaga());
	}
	
	@Test
	public void devePermitirOCancelamentoDaVenda()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertTrue("Venda PODE ser cancelada.", venda1.isCancelavel());
		
		venda1.registraPagamento("0000", LocalDateTime.now());
		
		assertTrue("Venda PODE ser cancelada.", venda1.isCancelavel());
	}
	
	@Test
	public void deveImpedirOCancelamentoDaVenda()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now().minusDays(1))
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		venda1.registraPagamento("0000", LocalDateTime.now().minusDays(1));
		
		assertFalse("Venda NÃO PODE ser cancelada.", venda1.isCancelavel());
	}
	
	@Test
	public void deveCancelarAVenda() throws RegraDeNegocioException
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now().minusDays(1))
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		venda1.registraPagamento("0000", LocalDateTime.now());
		
		assertTrue("Venda foi paga", venda1.isPaga() && venda1.isCancelavel());
		
		venda1.cancela();
		assertFalse("Venda cancelada", venda1.isPaga());
		assertEquals("Status", StatusVenda.CANCELADA, venda1.getStatus());
		assertFalse("Horario cancelamento", null == venda1.getHorarioCancelamento());
	}
	
	@Test
	public void deveGeraroUUID()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now().minusDays(1))
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertTrue("UUID gerado", null != venda1.getUniqueId().toString());
		
		Venda venda2 = new Venda(new Cliente(1L, "Sidronio"));
		assertTrue("UUID gerado", null != venda2.getUniqueId());
	}
	
	@Test
	public void devePermitirOParcelamento()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now().minusDays(1))
				.doProduto(brinco, new BigDecimal(2), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertTrue("Pode ser parcelada", venda1.isParcelavel());
		
		Venda venda2 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now().minusDays(1))
				.doProduto(corrente, new BigDecimal(1), gradeCorrente)
				.doProduto(brinco, BigDecimal.ONE, gradeBrinco)
				.constroi();
		assertTrue("Pode ser parcelada pelo valor bruto == 150,00.", venda2.isParcelavel());
		
		Venda venda3 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now().minusDays(1))
				.doProduto(corrente, new BigDecimal(1), gradeCorrente)
				.doProduto(brinco, BigDecimal.ONE, gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertTrue("Pode ser parcelada por considerar o valor bruto.", venda3.isParcelavel());
	}
	
	@Test
	public void naoDevePermitirOParcelamento()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now().minusDays(1))
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertFalse("Não pode ser parcelada", venda1.isParcelavel());

		Venda venda2 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now().minusDays(1))
				.doProduto(corrente, new BigDecimal(1), gradeCorrente)
				.comDesconto(new BigDecimal(15))
				.constroi();
		
		assertFalse("Não pode ser parcelada", venda2.isParcelavel());
	}
}
