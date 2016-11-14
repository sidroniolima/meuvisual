package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Caracteristica;
import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.service.GrupoService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.service.SubgrupoService;
import net.mv.meuespaco.util.EstadoDeNavegacao;
import net.mv.meuespaco.util.FacesUtil;
import net.mv.meuespaco.util.IConstants;
import net.mv.meuespaco.util.Paginator;

/**
 * Abstração da Listagem de Produtos para implementação de 
 * da listagem de produtos para venda e de consignados.
 * 
 * @author Sidronio
 * 10/08/2016
 */
public abstract class ListaProdutosAbstractBean implements Serializable {

	private static final long serialVersionUID = -890468050899264227L;

	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private GrupoService grupoService;
	
	@Inject
	private SubgrupoService subgrupoService;
	
	@Inject @Param
	private String paramDep;
	
	@Inject @Param
	private Long paramGrupo;
	
	@Inject @Param
	private Long paramSubgrupo;
	
	private Departamento dep;
	private Grupo grupo;
	private Subgrupo subgrupo;
	
	private List<Produto> produtos;
	
	private List<Subgrupo> subgrupos;
	
	private Composicao[] composicoes;

	private Paginator paginator = new Paginator(IConstants.QTD_EXIBIDA_NA_LISTAGEM_DE_PRODUTOS);
	
	/**
	 * Inicia o Bean por um Estado de Navegação já utilizado ou
	 * pelos parâmetros.
	 * 
	 */
	@PostConstruct
	public void init() {
		
		if (this.getEstadoDeNavegacao().isEstadoCriado() && !isParametrizado()) {
			this.restauraEstado();
			
			this.criaListaDeSubgrupos();
			this.listarComPaginacaoESalvarEstado();
							
		} else {
		
			try {
				this.preencheInformacoesDeNavegacao(paramDep, paramGrupo, paramSubgrupo);
				this.getEstadoDeNavegacao().criaEstado();
				
				this.criaListaDeSubgrupos();
				this.listarComPaginacaoESalvarEstado();
			
			} catch (RegraDeNegocioException e) {
				FacesUtil.addErrorMessage(e.getMessage() + " Clique nos links de navegação acima para listar as peças.");
			}
		}
		
		composicoes = Composicao.values();

	}
	
	/**
	 * Cria a lista de subgrupos disponíveis para o Departamento e Grupo 
	 * selecionado.
	 */
	private void criaListaDeSubgrupos() {
		subgrupos = this.subgrupoService.listarSubgruposPorDepEGrupo(dep, grupo);
	}

	/**
	 * Se a requisição vier do Menu de navegação são utilizados parametros, 
	 * portanto parametrizado. Assim o estado de navegação é recriado, possibitando 
	 * a navegação.
	 * 
	 * @return
	 */
	private boolean isParametrizado() {
		return null != paramDep || null != paramGrupo || null != paramSubgrupo;
	}

	/**
	 * Pesquisa as informações de navegação: departamento, grupo ou subrupo.
	 * 
	 * @param paramDep
	 * @param paramGrupo
	 * @param paramSubgrupo
	 * @throws RegraDeNegocioException 
	 */
	private void preencheInformacoesDeNavegacao(String paramDep, Long paramGrupo, Long paramSubgrupo) throws RegraDeNegocioException {
		
		if (null != paramDep) {
			
			try
			{
				dep = Departamento.valueOf(paramDep);
			} catch (IllegalArgumentException ex)
			{
				throw new RegraDeNegocioException("Não foi possível listar produtos para o Departamento selecionado.");
			}
			
		}
		
		if (null != paramGrupo) {
			grupo = grupoService.buscaPeloCodigo(paramGrupo);
			
			if (null == grupo)
			{
				throw new RegraDeNegocioException("Não foi possível listar produtos para o Grupo selecionado.");
			}
		}
		
		if (null != paramSubgrupo) {
			subgrupo = subgrupoService.buscaPeloCodigoComGrupoEFamilia(paramSubgrupo);
			
			if (null == subgrupo)
			{
				throw new RegraDeNegocioException("Não foi possível listar produtos para o Subgrupo selecionado.");
			}
			
			grupo = subgrupo.getGrupo();
		}
		
	}

	/**
	 * Restaura um estado de navegação.
	 */
	public void restauraEstado() {
		this.setFiltro(this.getEstadoDeNavegacao().getEstado().getFiltro());
		this.dep = this.getEstadoDeNavegacao().getEstado().getDep();
		this.grupo = this.getEstadoDeNavegacao().getEstado().getGrupo();
		this.subgrupo = this.getEstadoDeNavegacao().getEstado().getSubgrupo();
		this.paginator = this.getEstadoDeNavegacao().getEstado().getPaginator();
	}

	/**
	 * Lista os registros de produtos de forma paginada. Como abstração 
	 * para listagem de produtos consignados e para venda.
	 */
	public abstract void listarComPaginacao();

	/**
	 * Lista os produtos de forma paginada e salva  
	 * o estado de navegação atual.
	 * 
	 */
	public void listarComPaginacaoESalvarEstado()
	{
		this.listarComPaginacao();
		
		this.getEstadoDeNavegacao().salvaEstado(
				this.getDep(), 
				this.getGrupo(), 
				this.getSubgrupo(), 
				this.getFiltro(), 
				this.getPaginator());
	}
	
	/**
	 * Limpa os filtros do Tipo e Composição
	 */
	public void limpaFiltro() {
		this.getFiltro().limpa();
		listarComPaginacaoESalvarEstado();
	}
	
	/**
	 * Filtra os produtos pelo tipo selecionado.
	 * 
	 * @param event Tipo selecionado.
	 */
	public void filtraPorCaracteristicaListener(ValueChangeEvent event) {
		
		String caracteristica = (String) event.getNewValue();
		if (null != caracteristica) {
			this.getFiltro().setCaracteristica(caracteristica);
		}
		
		this.reiniciaPaginator();
		
		listarComPaginacaoESalvarEstado();
		
	}
	
	/**
	 * Reinicia o Paginador.
	 */
	private void reiniciaPaginator() {
		paginator = new Paginator(IConstants.QTD_EXIBIDA_NA_LISTAGEM_DE_PRODUTOS);
	}

	/**
	 * Filtra os produtos pela composição.
	 * 
	 * @param event Composição para o filtro
	 */
	public void filtraPorComposicaoListener(ValueChangeEvent event) {
		
		Composicao comp = (Composicao) event.getNewValue();
		
		if (null != comp) {
			this.getFiltro().setComposicao(comp);
		}
		
		this.reiniciaPaginator();
		
		listarComPaginacaoESalvarEstado();
		
		/*
		Composicao comp = (Composicao) event.getNewValue();
		limpaFiltro();
		if (null != comp) {
			
			produtosFiltrados = produtos
					.stream()
					.filter(p -> p.getComposicao().equals(comp))
					.collect(Collectors.toList());
		}
		*/
	}

	
	
	/**
	 * @return the produtos
	 */
	public List<Produto> getProdutos() {
		
		return produtos;
	}
	protected void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	/**
	 * @return the dep
	 */
	public Departamento getDep() {
		return dep;
	}

	/**
	 * @return the grupo
	 */
	public Grupo getGrupo() {
		return grupo;
	}

	/**
	 * @return the subgrupos
	 */
	public List<Subgrupo> getSubgrupos() {
		return subgrupos;
	}

	/**
	 * @return the caracteristicas
	 */
	public Map<Caracteristica, List<String>> getCaracteristicas() {
		return this.produtoService.listarCaracteristicasPorDepGrupoESubgrupo(dep, grupo, subgrupo);
	}

	/**
	 * @return the composicoes
	 */
	public Composicao[] getComposicoes() {
		return composicoes;
	}

	/**
	 * @return the subgrupo
	 */
	public Subgrupo getSubgrupo() {
		return subgrupo;
	}

	/**
	 * @return the paginator
	 */
	public Paginator getPaginator() {
		return paginator;
	}
	/**
	 * @param paginator the paginator to set
	 */
	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}

	public ProdutoService getProdutoService() {
		return produtoService;
	}

	public abstract EstadoDeNavegacao getEstadoDeNavegacao();

	public abstract FiltroListaProduto getFiltro();
	public abstract void setFiltro(FiltroListaProduto filtro);
}
