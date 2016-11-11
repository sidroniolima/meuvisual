package net.mv.meuespaco.model.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.cielo.PaymentType;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.util.DataDoSistema;

public class VendaTest {
	
	private Produto brinco;
	private Grade gradeBrinco;
	
	private Produto anel;
	private Grade gradeAnel;
	
	private DataDoSistema horarioFalso = Mockito.mock(DataDoSistema.class);
	
	@Before
	public void init()
	{
		brinco = new Produto(1L, "Brinco");
		brinco.setCodigoInterno("21008976MV09990");
		gradeBrinco = new GradeCorETamanho(Cor.AMARELO, Tamanho.TAM_22);
		
		anel = new Produto(2L, "Anel");
		anel.setCodigoInterno("20894356MV03390");
		gradeAnel = new GradeTamanho(Tamanho.TAM_18);
		
		try {
			brinco.adicionaGrade(gradeBrinco);
			anel.adicionaGrade(gradeAnel);
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
	public void deveRegistrarPagamento()
	{
		Venda venda1 = new VendaBuilder().comCodigo(1L)
				.doCliente(new Cliente(1L, "Sidronio"))
				.noHorario(LocalDateTime.now())
				.doProduto(brinco, new BigDecimal(1), gradeBrinco)
				.doProduto(brinco, BigDecimal.ONE, gradeBrinco)
				.comDesconto(new BigDecimal(15))
				.comDataDoSistema(horarioFalso)
				.constroi();
		
		when(horarioFalso.agora()).thenReturn(LocalDateTime.parse("2016-11-07T14:00:15"));
		
		venda1.registraPagamento("1817e007-a41e-49b0-9e17-12642ccb6323", "3931228", PaymentType.CreditCard);
		
		assertEquals("Payment ID", venda1.getPaymentId(), "1817e007-a41e-49b0-9e17-12642ccb6323");
		assertEquals("Proof of Sale", venda1.getProofOfSale(), "3931228");
		assertEquals("Status do pagamento", StatusVenda.PAGAMENTO_CONFIRMADO, venda1.getStatus());
		assertEquals("Horario do pagamento", LocalDateTime.parse("2016-11-07T14:00:15"), venda1.getHorarioPagamento());
	}
}
