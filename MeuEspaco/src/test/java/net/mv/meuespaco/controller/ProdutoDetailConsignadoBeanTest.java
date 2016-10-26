package net.mv.meuespaco.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeLetra;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.model.loja.CarrinhoConsignado;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.service.impl.ClienteServiceImpl;
import net.mv.meuespaco.service.impl.ProdutoServiceImpl;

public class ProdutoDetailConsignadoBeanTest {

	private ProdutoService produtoSrvcFalso = Mockito.mock(ProdutoServiceImpl.class);
	private ClienteService clienteSrvcFalso = Mockito.mock(ClienteServiceImpl.class);
	private CarrinhoAbstractBean carrinhoBeanFalso = Mockito.mock(CarrinhoConsignadoBean.class);
	
	private ProdutoDetailConsignadoBean produtoDetailBean;
	
	private Grupo grupo;
	private Subgrupo subgrupo;
	private Produto produtoGradeCor;
	private Produto produtoGradeTamanho;
	private Produto produtoGradeTamanhoECor;
	private Produto produtoGradeLetra;
	
	private Semana semana;
	
	List<Cor> cores;
	List<String> letras;
	List<Tamanho> tamanhos;
	
	@Before
	public void init() throws RegraDeNegocioException
	{
		grupo = new Grupo(1L, "Anéis");
		subgrupo = new Subgrupo(1L, "Anéis de formatura");
		subgrupo.setGrupo(grupo);
		
		produtoGradeCor = new Produto(1L, "Produto Cor", "21874512MV19990");
		produtoGradeCor.setSubgrupo(subgrupo);
		produtoGradeCor.setTipoGrade(TipoGrade.COR);
		produtoGradeCor.adicionaGrade(new GradeCor(1L, Cor.COBRE));
		
		produtoGradeTamanho = new Produto(1L, "Produto Tam", "26547871MV19990");
		produtoGradeTamanho.setSubgrupo(subgrupo);
		produtoGradeTamanho.setTipoGrade(TipoGrade.TAMANHO);
		produtoGradeTamanho.adicionaGrade(new GradeTamanho(2L, Tamanho.TAM_16));
		
		produtoGradeTamanhoECor = new Produto(1L, "Produto Tam e Cor", "28745147MV19990");
		produtoGradeTamanhoECor.setSubgrupo(subgrupo);
		produtoGradeTamanhoECor.setTipoGrade(TipoGrade.COR_E_TAMANHO);
		produtoGradeTamanhoECor.adicionaGrade(new GradeCorETamanho(3L, Cor.BRANCO_PEROLA, Tamanho.TAM_16));

		produtoGradeLetra = new Produto(1L, "Produto de Letra", "20001245MV19990");
		produtoGradeLetra.setSubgrupo(subgrupo);
		produtoGradeLetra.setTipoGrade(TipoGrade.LETRA);
		produtoGradeLetra.adicionaGrade(new GradeLetra(4L, 'A'));
		
		cores = produtoGradeCor.getGrades().stream().map(g -> ((GradeCor) g).getCor()).collect(Collectors.toList());
		letras = produtoGradeLetra.getGrades().stream().map(g -> String.valueOf(((GradeLetra) g).getLetra())).collect(Collectors.toList());
		tamanhos = produtoGradeTamanho.getGrades().stream().map(g -> ((GradeTamanho) g).getTamanho()).collect(Collectors.toList());
		
		semana = new Semana(LocalDate.of(2016, 10, 10), LocalDate.now());
	}
	
	@Test
	public void deveIniciarOsParametrosComGradeCor() throws RegraDeNegocioException 
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeCor);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(cores);
		when(produtoSrvcFalso.tamanhosDisponiveisDoProduto(produtoGradeCor)).thenReturn(null);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeCor)).thenReturn(null);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
		
		assertTrue("Habilita escolha", this.produtoDetailBean.isHabilitaEscolha());
		assertTrue("Grade selecionada", 
				this.produtoDetailBean.getGradeSelecionada().tipoDeGrade().equals(new GradeCor().tipoDeGrade()));
		
		assertEquals("Quantidade", BigDecimal.ONE, this.produtoDetailBean.getQtdDoProduto());
	}
	
	@Test
	public void deveIniciarOsParametrosComGradeTamanho() throws RegraDeNegocioException 
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeTamanho);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeTamanho)).thenReturn(null);
		when(produtoSrvcFalso.tamanhosDisponiveisDoProduto(produtoGradeTamanho)).thenReturn(tamanhos);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeTamanho)).thenReturn(null);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
		
		assertTrue("Habilita escolha", this.produtoDetailBean.isHabilitaEscolha());
		assertTrue("Grade selecionada", 
				this.produtoDetailBean.getGradeSelecionada().tipoDeGrade().equals(new GradeTamanho().tipoDeGrade()));
		
		assertEquals("Quantidade", BigDecimal.ONE, this.produtoDetailBean.getQtdDoProduto());
	}
	
	@Test
	public void deveIniciarOsParametrosComGradeCorETamanho() throws RegraDeNegocioException 
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeTamanhoECor);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(cores);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(null);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeCor)).thenReturn(null);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
		
		assertTrue("Habilita escolha", this.produtoDetailBean.isHabilitaEscolha());
		assertTrue("Grade selecionada", 
				this.produtoDetailBean.getGradeSelecionada().tipoDeGrade().equals(new GradeCorETamanho().tipoDeGrade()));
		
		assertEquals("Quantidade", BigDecimal.ONE, this.produtoDetailBean.getQtdDoProduto());
	}
	
	@Test
	public void deveListarTamanhosDisponiveis() throws RegraDeNegocioException
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeCor);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(cores);
		when(produtoSrvcFalso.tamanhosDisponiveisDoProduto(produtoGradeCor)).thenReturn(new ArrayList<Tamanho>());
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeCor)).thenReturn(null);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
		
		assertEquals("Tamanhos disponíveis", new ArrayList<Tamanho>(), this.produtoDetailBean.getTamanhosDisponiveis());
		
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeTamanho);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeTamanho)).thenReturn(null);
		when(produtoSrvcFalso.tamanhosDisponiveisDoProduto(produtoGradeTamanho)).thenReturn(tamanhos);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeTamanho)).thenReturn(null);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
		
		assertEquals("Tamanhos disponíveis", Arrays.asList(Tamanho.TAM_16), this.produtoDetailBean.getTamanhosDisponiveis());		
	}
	
	@Test
	public void deveListarCoresDisponiveis() throws RegraDeNegocioException
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeCor);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(cores);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeCor)).thenReturn(null);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
		
		assertEquals("Cores disponíveis", Arrays.asList(Cor.COBRE), this.produtoDetailBean.getCoresDisponiveis());
	}
	
	@Test
	public void deveListarLetrasDisponiveis() throws RegraDeNegocioException
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeCor);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(null);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeCor)).thenReturn(letras);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
		
		assertEquals("Letras disponíveis", Arrays.asList("A"), this.produtoDetailBean.getLetrasDisponiveis());
	}
	
	@Test
	public void deveListarCoresParaOTamanho() throws RegraDeNegocioException
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeCor);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(cores);
		when(produtoSrvcFalso.tamanhosDisponiveisDoProduto(produtoGradeTamanho)).thenReturn(tamanhos);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeCor)).thenReturn(null);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
		
		assertEquals("Letras disponíveis", null, this.produtoDetailBean.getCoresParaOTamanho());
	}
	
	@Test
	public void deveAdicionarAoCarrinho() throws RegraDeNegocioException
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeCor);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(cores);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeCor)).thenReturn(null);
		
		doNothing().when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.init();
				
		GradeCor gradeDaView = (GradeCor) this.produtoDetailBean.getGradeSelecionada();
		gradeDaView.setCor(Cor.COBRE);
		
		when(carrinhoBeanFalso.getCarrinho()).thenReturn(new CarrinhoConsignado(new BigDecimal(1800), 40));
		when(produtoSrvcFalso.gradeDoProduto(produtoGradeCor, this.produtoDetailBean.getGradeSelecionada())).thenReturn(
				new GradeCor(1L, Cor.COBRE));
		
		this.produtoDetailBean.addToChart();
		
		verify(produtoSrvcFalso).gradeDoProduto(produtoGradeCor, this.produtoDetailBean.getGradeSelecionada());
	}

	@Test
	public void deveIniciarProibindoAEscolha() throws RegraDeNegocioException
	{
		produtoDetailBean = new ProdutoDetailConsignadoBean(produtoSrvcFalso, clienteSrvcFalso, carrinhoBeanFalso, new Long(1));
		
		when(produtoSrvcFalso.buscaPeloCodigoComRelacionamentos(1L)).thenReturn(produtoGradeCor);
		when(produtoSrvcFalso.coresDisponiveisDoProduto(produtoGradeCor)).thenReturn(cores);
		when(produtoSrvcFalso.tamanhosDisponiveisDoProduto(produtoGradeCor)).thenReturn(null);
		when(produtoSrvcFalso.letrasDisponiveis(produtoGradeCor)).thenReturn(null);
		
		doThrow(new RegraDeNegocioException()).when(clienteSrvcFalso).verificaSeOUsuarioLogadoPodeEscolher();
		
		this.produtoDetailBean.verificaDisponibilidadeDeEscolha();
		
		assertFalse("Não deve permitir a escolha.", this.produtoDetailBean.isHabilitaEscolha());
	}
}
