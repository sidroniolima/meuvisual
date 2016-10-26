package net.mv.meuespaco.service.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.service.ReservaProdutoService;

/**
 * Teste do serviço de reserva de produtos, grade 
 * e quantidade.
 * 
 * @author Sidronio
 * 14/12/2015
 */
public class ResevaProdutoServiceImplTest {

	private ReservaProdutoService reservaSrvc;
	
	private Produto produtoAnel;
	private Produto produtoBrinco;
	
	private Grade gradeCorAmetistaVerde;
	private Grade gradeCorVermelho;
	private Grade gradeCorETamanho;
	
	@Before
	public void init() throws RegraDeNegocioException {
		reservaSrvc = new ResevaProdutoServiceImpl();
		
		produtoAnel = new Produto(1L, "Anel");
		produtoBrinco = new Produto(2L, "Brinco");
		
		gradeCorAmetistaVerde = new GradeCor(1L, Cor.AMETISTA_VERDE);
		gradeCorVermelho = new GradeCor(2L, Cor.VERMELHO);
		gradeCorETamanho = new GradeCorETamanho(3L, Cor.PARAIBA_VERDE, Tamanho.TAM_18);
		
		produtoBrinco.adicionaGrade(gradeCorAmetistaVerde);
		produtoAnel.adicionaGrade(gradeCorETamanho);
		
	}
	
	public void deveAdicionarOProdutoAReserva() {
		
		assertTrue("Chekc que não há reservas.", reservaSrvc.getReserva().isEmpty());
		
		reservaSrvc.adicionaReserva(produtoAnel, gradeCorETamanho, new BigDecimal(2));

		//Produto Brinco com Grade Cor AmetistaVerde
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorAmetistaVerde, BigDecimal.ONE);
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorAmetistaVerde, BigDecimal.ONE);
		
		//Produto Brinco com Grade Cor Vermelho
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		
		
		assertFalse("Chekc que há reservas.", reservaSrvc.getReserva().isEmpty());
		
		assertEquals("Check da quantidade reservada do Anel Azul 18", 
				new BigDecimal(2), reservaSrvc.qtdReservadaDoProduto(produtoAnel, gradeCorETamanho));
		
		assertEquals("Check da quantidade reservada Brinco Verde", 
				new BigDecimal(2), reservaSrvc.qtdReservadaDoProduto(produtoBrinco, gradeCorAmetistaVerde));
		
		assertEquals("Check da quantidade reservada Brinco Vermelho", 
				new BigDecimal(3), reservaSrvc.qtdReservadaDoProduto(produtoBrinco, gradeCorVermelho));	
		
	}
	
	public void deveRemoverAReseva() {
		
		//reservaSrvc.adicionaReserva(produtoAnel, gradeCorETamanho, new BigDecimal(2));
		//reservaSrvc.removeReserva(produtoAnel, gradeCorETamanho, BigDecimal.ONE);
		
		//Produto Brinco com Grade Cor AmetistaVerde
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorAmetistaVerde, BigDecimal.ONE);
		reservaSrvc.removeReserva(produtoBrinco, gradeCorAmetistaVerde, BigDecimal.ONE);
		
		//Produto Brinco com Grade Cor Vermelho
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		reservaSrvc.removeReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		reservaSrvc.removeReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		
		//assertEquals("Check da quantidade reservada do Anel Azul 18", 
		//		new BigDecimal(1), reservaSrvc.qtdReservadaDoProduto(produtoAnel, gradeCorETamanho));
		
		assertEquals("Check da quantidade reservada Brinco Verde", 
				new BigDecimal(0), reservaSrvc.qtdReservadaDoProduto(produtoBrinco, gradeCorAmetistaVerde));
		assertEquals("Check da quantidade reservada Brinco Vermelho", 
				new BigDecimal(1), reservaSrvc.qtdReservadaDoProduto(produtoBrinco, gradeCorVermelho));		
		
		reservaSrvc.imprimeReserva();
	}
	
	public void deveRetornarAsGradesEQtdDoProdutoReservado()
	{
		//Produto Brinco com Grade Cor AmetistaVerde
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorAmetistaVerde, BigDecimal.ONE);
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorAmetistaVerde, BigDecimal.ONE);
		
		//Produto Brinco com Grade Cor Vermelho
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		
		Map<Grade, BigDecimal> mapaDeGrades = reservaSrvc.gradesReservadasDoProduto(produtoBrinco);
		assertEquals("Check das grades do Brinco", 2, mapaDeGrades.size());
		
		reservaSrvc.imprimeReserva();
	}

	public void deveCalcularAQtdDeReservaParaAGradeDoProduto() 
	{
		//Produto Brinco com Grade Cor Vermelho
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		
		reservaSrvc.adicionaReserva(produtoBrinco, gradeCorAmetistaVerde, BigDecimal.ONE);
		reservaSrvc.removeReserva(produtoBrinco, gradeCorAmetistaVerde, BigDecimal.ONE);
		
		assertEquals("Check da qtd reservada do Brinco na cor Vermelho", 
				new BigDecimal(1), reservaSrvc.qtdReservadaDoProduto(produtoBrinco, gradeCorVermelho));
		
		reservaSrvc.removeReserva(produtoBrinco, gradeCorVermelho, BigDecimal.ONE);
		
		assertEquals("Check da qtd reservada do Brinco na cor Vermelho", 
				new BigDecimal(0), reservaSrvc.qtdReservadaDoProduto(produtoBrinco, gradeCorVermelho));
		
		assertEquals("Check da qtd reservada do Brinco na cor Ametista verde", 
				new BigDecimal(0), reservaSrvc.qtdReservadaDoProduto(produtoBrinco, gradeCorAmetistaVerde));
	}
}
