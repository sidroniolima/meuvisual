package net.mv.meuespaco.model.estoque;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.Tamanho;

public class AjusteTest {

	private Ajuste ajuste;
	private Produto prodAnel;
	private Produto prodBrinco;
	
	private Grade gradeAnel;
	private Grade gradeBrinco_1;
	private Grade gradeBrinco_2;
	
	@Before
	public void config() throws RegraDeNegocioException {
		
		ajuste = new Ajuste();
		
		prodAnel = new Produto(1L, "Anel");
		prodBrinco = new Produto(2L, "Brinco");
		
		gradeAnel = new GradeTamanho(Tamanho.TAM_20);
		gradeBrinco_1 = new GradeCorETamanho(Cor.AMETISTA_VERDE, Tamanho.TAM_18);
		gradeBrinco_2 = new GradeCorETamanho(Cor.C_ROSA_LEITOSO, Tamanho.TAM_18);
		
		prodAnel.adicionaGrade(gradeAnel);
		prodBrinco.adicionaGrade(gradeBrinco_1);
		prodBrinco.adicionaGrade(gradeBrinco_2);
	}
	
	@Test
	public void deveValidarUmItem() {
		
		ItemAjuste item1 = new ItemAjuste(prodAnel, gradeAnel, new BigDecimal(15));
		ItemAjuste item2 = new ItemAjuste();
		
		try {
			item1.valida();

		} catch (RegraDeNegocioException e) {
			fail("Check de item válido");
		}
		
		try {
			item2.valida();
			fail("Check de item inválido");
		} catch (RegraDeNegocioException e) {
		}
	}
	
	@Test
	public void deveAdicionaItensAoAjuste(){
		
		ItemAjuste item1 = new ItemAjuste(prodAnel, gradeAnel, new BigDecimal(15));
		
		try {
			ajuste.adicionaItem(item1);
		} catch (RegraDeNegocioException e) {
		}
		
		ItemAjuste item2 = new ItemAjuste(prodBrinco, gradeBrinco_1, new BigDecimal(5));
		try {
			ajuste.adicionaItem(item2);
		} catch (RegraDeNegocioException e) {
		}
		
		assertEquals("Check de adição de item.", 2, ajuste.getItens().size());
	}
	
	@Test
	public void deveRemoverUmItem(){
		
		ItemAjuste item1 = new ItemAjuste(prodAnel, gradeAnel, new BigDecimal(15));
		
		try {
			ajuste.adicionaItem(item1);
		} catch (RegraDeNegocioException e) {
		}
		
		ItemAjuste item2 = new ItemAjuste(prodBrinco, gradeBrinco_1, new BigDecimal(5));
		try {
			ajuste.adicionaItem(item2);
		} catch (RegraDeNegocioException e) {
		}
		
		ajuste.removeItem(item1);
		
		assertEquals("Check da remoção de item.", 1, ajuste.getItens().size());
		assertEquals("Check da remoção do certo.", prodBrinco, ajuste.getItens().get(0).getProduto());
		
	}

	@Test
	public void deveRemoverUmItemPeloProdutoEGrade(){
		
		ItemAjuste item1 = new ItemAjuste(prodAnel, gradeAnel, new BigDecimal(15));
		
		try {
			ajuste.adicionaItem(item1);
		} catch (RegraDeNegocioException e) {
		}
		
		ItemAjuste item2 = new ItemAjuste(prodBrinco, gradeBrinco_1, new BigDecimal(5));
		try {
			ajuste.adicionaItem(item2);
		} catch (RegraDeNegocioException e) {
		}
		
		ItemAjuste item3 = new ItemAjuste(prodBrinco, gradeBrinco_2, new BigDecimal(10));
		try {
			ajuste.adicionaItem(item3);
		} catch (RegraDeNegocioException e) {
		}
		
		ajuste.removeItem(prodBrinco, gradeBrinco_1);
		
		assertEquals("Check da remoção de item.", 2, ajuste.getItens().size());
		assertEquals("Check da remoção do certo.", prodAnel, ajuste.getItens().get(0).getProduto());
		assertEquals("Check da remoção do certo.", gradeBrinco_2, ajuste.getItens().get(1).getGrade());
	}
	
	@Test
	public void deveValidarOAjuste()
	{
		try {
			ajuste.valida();
			fail("Check de ajuste inválido.");
			
		} catch (RegraDeNegocioException e) {
		}
		
		ajuste.setAlmEntrada(new Almoxarifado());
		ajuste.setAlmSaida(new Almoxarifado());
		ajuste.setTipoMovimento(TipoMovimento.ENTRADA);

		
		ItemAjuste item1 = new ItemAjuste(prodAnel, gradeAnel, new BigDecimal(15));
		
		try {
			ajuste.adicionaItem(item1);
		} catch (RegraDeNegocioException e) {
		}
		
		try {
			ajuste.valida();
			fail("Check de ajuste inválido.");
		} catch (RegraDeNegocioException e) {
		}
		
		ajuste.setUsuario(new Usuario(1L));
		ajuste.setMotivo("Teste da classe");
		
		try {
			ajuste.valida();
		} catch (RegraDeNegocioException e) {
			fail("Check de ajuste válido.");
		}
	}
}
