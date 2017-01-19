package net.mv.meuespaco.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.primefaces.model.UploadedFile;

import net.mv.meuespaco.controller.PesquisaProdutoBean.FiltroProduto;
import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.ProdutoDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Caracteristica;
import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeComCor;
import net.mv.meuespaco.model.grade.GradeComLetra;
import net.mv.meuespaco.model.grade.GradeComTamanho;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.GradeService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.service.ReservaProdutoService;
import net.mv.meuespaco.util.FotoHelper;
import net.mv.meuespaco.util.Paginator;

/**
 * Implementação da camada DAO para a Entidade Produto.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class ProdutoServiceImpl extends SimpleServiceLayerImpl<Produto, Long> implements ProdutoService, Serializable {

	private static final long serialVersionUID = -8154179524604954079L;

	@Inject
	private ProdutoDAO produtoDAO;
	
	@Inject
	private EstoqueService estoqueSrvc;
	
	@Inject
	private GradeService gradeSrvc;
	
	@Inject
	private ReservaProdutoService reservaProdutoSrvc;
	
	@Override
	public void validaInsercao(Produto produto) throws RegraDeNegocioException {
		
		produto.valida();
	}
	
	@Override
	public void validaExclusao(Produto entidade) throws RegraDeNegocioException {
		
	}

	@Override
	public GenericDAO getDAO() {
		return produtoDAO;
	}

	@Override
	public Produto buscaPeloCodigoComRelacionamentos(Long paramCodigo) {
		
		return produtoDAO.buscarPeloCodigoComRelacionamento(paramCodigo, 
				Arrays.asList(
						"subgrupo",
						"subgrupo.grupo",
						"subgrupo.grupo.familia",
						"grades",
						"caracteristicas",
						"fotos",
						"produtosRelacionados",
						"produtosRelacionados.fotos"));
				
	}

	@Override
	public List<Tamanho> filtraGradesPorCor(Produto produto, Cor corParaPesquisa) {
		
		if (!produto.getTipoGrade().equals(TipoGrade.COR_E_TAMANHO)) {
			return null;
		}
		
		List<Tamanho> tamanhos = new LinkedList<Tamanho>();
		
		for (Grade grade: this.verificaGradesDisponiveis(produto)) {
			
			Cor cor = ((GradeComCor) grade).getCor();
			
			if ( cor == corParaPesquisa || null == corParaPesquisa) {
				
				Tamanho tam = ((GradeComTamanho) grade).getTamanho();
				
				if (!tamanhos.contains(tam) ) {
					tamanhos.add(tam);
				}
				
			}
			
		}
		
		return tamanhos;
	}

	@Override
	public List<Cor> filtraGradesPeloTamanho(Produto produto, Tamanho tamanho) {
		
		if (!produto.getTipoGrade().equals(TipoGrade.COR_E_TAMANHO)) {
			return null;
		}
		
		List<Cor> cores = new LinkedList<Cor>();
		
		for (Grade grade: this.verificaGradesDisponiveis(produto)) {
			
			Tamanho tam = ((GradeComTamanho) grade).getTamanho();
			
			if ( tam == tamanho || tamanho == null) {
				
				Cor cor = ((GradeComCor) grade).getCor();
				
				if (!cores.contains(cor) ) {
					cores.add(cor);
				}
				
			}
			
		}
		
		return cores;
	}
	
	@Override
	public List<Cor> coresDisponiveisDoProduto(Produto produto) {
		
		if (produto.getTipoGrade().equals(TipoGrade.TAMANHO)|| 
				produto.getTipoGrade().equals(TipoGrade.LETRA) || 
				produto.getTipoGrade().equals(TipoGrade.UNICA))  {
			
			return null;
		}
		
		List<Cor> cores = new LinkedList<Cor>();
		
		for (Grade grade : this.verificaGradesDisponiveis(produto)) {
			
			Cor cor = ((GradeComCor)grade).getCor();
			
			if (!cores.contains(cor)) {
				cores.add(cor);
			}
		}
		
		return cores;
	}
	
	@Override
	public List<String> letrasDisponiveis(Produto produto) {
		
		if (!produto.getTipoGrade().equals(TipoGrade.LETRA))  {
			return null;
		}
		
		List<String> letras = new ArrayList<String>();
		
		for (Grade grade : this.verificaGradesDisponiveis(produto)) {
			
			char letra = ((GradeComLetra) grade).getLetra();
			
			String strLetra = String.valueOf(letra);
			
			if (!letras.contains(strLetra)) {
				letras.add(strLetra);
			}
		}
		
		return letras;
	}
	
	@Override
	public List<Tamanho> tamanhosDisponiveisDoProduto(Produto produto) {
		
		if (produto.getTipoGrade().equals(TipoGrade.COR) || 
				produto.getTipoGrade().equals(TipoGrade.LETRA) || 
				produto.getTipoGrade().equals(TipoGrade.UNICA)) {
			return null;
		}
		
		List<Tamanho> tamanhos = new LinkedList<Tamanho>();

		for (Grade grade : this.verificaGradesDisponiveis(produto)) {
			
			Tamanho tamanho = ((GradeComTamanho)grade).getTamanho();
			
			if (!tamanhos.contains(tamanho)) {
				tamanhos.add(tamanho);
			}
		}
		
		return tamanhos;
	}
	
	@Override
	public Grade gradeDoProduto(Produto produto, Grade gradeSelecionada) {
		
		for (Grade grade : produto.getGrades()) 
		{
			if (grade.temAsMesmasCaracteristicas(gradeSelecionada)) {
				return grade;
			}
		}
		
		return null;
		
	}
	
	@Override
	public Produto buscaProdutoComGrade(Long codigo) {
		return this.produtoDAO.buscarPeloCodigoComRelacionamento(codigo, Arrays.asList("unidade","grades"));
	}
	
	@Override
	public void inativaProduto(Produto produto) throws RegraDeNegocioException {
		produto.inativa();
		this.salva(produto);
	}
	
	@Override
	public void ativaProduto(Produto produto) throws RegraDeNegocioException {
		produto.ativa();
		this.salva(produto);
	}
	
	@Override
	public Produto buscaProdutoComFotos(Long codigo) 
	{
		return produtoDAO.buscarPeloCodigoComRelacionamento(
				codigo, 
				Arrays.asList(
						"departamento",
						"subgrupo",
						"fotos"));
	}
	
	@Override
	public void adicionaFotoAoProduto(Produto produto, UploadedFile file) throws IOException, RegraDeNegocioException 
	{
		new FotoHelper().salvaFotoEmArquivo(file);
		
		produto.adicionaFoto(file.getFileName());
		this.salva(produto);
	}

	@Override
	public void removeFotoDoProduto(Produto produto, String fotoSelecionada) throws RegraDeNegocioException {
		
		produto.removeFoto(fotoSelecionada);
		this.salva(produto);
	}
	
	@Override
	public Produto buscaProdutoComInfoParaOSite(Long codigo) {
		return produtoDAO.buscarPeloCodigoComRelacionamento(
				codigo, 
				Arrays.asList(
						"departamento",
						"subgrupo",
						"fotos",
						"produtosRelacionados"));
	}

	@Override
	public List<Produto> filtraPelaPesquisa(FiltroProduto filtro, Paginator paginator) {
		
		return produtoDAO.filtrarPeloModoEspecifico(
				filtro, 
				paginator.getFirstResult(), 
				paginator.getQtdPorPagina());
	}
	
	@Override
	public List<Produto> ultimosDezProdutosCadastrados() {
		return produtoDAO.listarUltimosCadastros(10);
	}
	
	@Override
	public Produto buscaPeloCodigoInterno(String codigoInterno) {
		return produtoDAO.buscarPeloCodigoInterno(codigoInterno);
	}

	@Override
	public List<Produto> listaProdutosPelaNavegacao(Departamento dep, Grupo grupo, Subgrupo subgrupo, FiltroListaProduto filtro,
			Paginator paginator) {
		return this.produtoDAO.fitrarPelaNavegacao(dep, grupo, subgrupo, filtro, paginator);
	}

	@Override
	public Map<Caracteristica, List<String>> listarCaracteristicasPorDepGrupoESubgrupo(Departamento dep, Grupo grupo,
			Subgrupo subgrupo) {
		
		Map<Caracteristica, List<String>> caracteristicas = new HashMap<Caracteristica, List<String>>();
		
		List<Produto> produtos = this.produtoDAO.listarCaracteristicasDeProdutos(dep, grupo, subgrupo);
		
		produtos.forEach(p -> p.getCaracteristicas().forEach((k,v) -> {
			
			if (caracteristicas.containsKey(k)) {
				
				if (!caracteristicas.get(k).contains(v)) {
					caracteristicas.get(k).add(v);
				}
				
			} else {
				List<String> lista = new ArrayList<String>();
				lista.add(v);
				caracteristicas.put(k, lista);
			}
		}));

		return caracteristicas;
	}
	
	@Override
	public List<Produto> filtrarProdutosPelaPesquisaDoUsuario(String pesquisa, Paginator paginator) {
		
		return this.produtoDAO.filtrarPeloCodigoInternoOuPelaDescricao(
				pesquisa, paginator);
	}
	
	@Override
	public List<Produto> filtrarProdutosPelaPesquisaDoUsuario(String pesquisa, Paginator paginator, Finalidade finalidade) {
		
		return this.produtoDAO.filtrarPeloCodigoInternoOuPelaDescricao(
				pesquisa, finalidade, paginator);
	}
	
	@Override
	public Produto buscaPeloCodigoComGradeESubgrupo(Long codigo) {
		
		return produtoDAO.buscarPeloCodigoComRelacionamento(
				codigo, 
				Arrays.asList(
						"departamento",
						"subgrupo",
						"grades"));
	}
	
	@Override
	public void removeGrade(Produto produto, Grade grade) throws RegraDeNegocioException {
		
		if (this.estoqueSrvc.verificaSeHaMovimentacaoDoProdutoParaAGrade(produto, grade)) {
			throw new RegraDeNegocioException("Não é possível remover a grade pois existe(m) movimentação(ões).");
		} else {
			produto.removeGrade(grade);
			
		}
	}
	
	@Override
	public void alteraGradeDoProduto(Produto produto, TipoGrade novoTipo) throws RegraDeNegocioException {
		
		this.atualizaTipoGradeDoProduto(produto, novoTipo);
		gradeSrvc.alteraGradeDoProduto(produto, novoTipo);
	}

	@Override
	public void atualizaTipoGradeDoProduto(Produto produto, TipoGrade novoTipo) throws RegraDeNegocioException {
		if (this.produtoDAO.atualizaGradeDoProduto(produto.getCodigo(), novoTipo.toString()) != 1) {
			throw new RegraDeNegocioException("Não foi possível atualizar a grade do produto.");
		}
	}
	
	@Override
	public List<Grade> verificaGradesDisponiveis(Produto produto) {
		
		List<Grade> gradesDisponiveis = new ArrayList<Grade>();
		
		for (Grade grade : produto.getGrades()) {
			if ((estoqueSrvc.qtdDisponivelParaVenda(produto, grade)
					.subtract(reservaProdutoSrvc.qtdReservadaDoProduto(produto, grade)))
					.compareTo(BigDecimal.ZERO) > 0) {
				gradesDisponiveis.add(grade);
			}
		}
		
		return gradesDisponiveis;
	}
}
