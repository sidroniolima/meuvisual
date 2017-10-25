package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Caracteristica;
import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.service.DepartamentoService;
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
	
	@Inject 
	private DepartamentoService depSrvc;
	
	@Inject @Param
	private Long paramDep;
	
	@Inject @Param
	private Long paramGrupo;
	
	@Inject @Param
	private Long paramSubgrupo;
	
	private Departamento dep;
	private Grupo grupo;
	private Subgrupo subgrupo;
	
	private List<Produto> produtos;
	
	private Composicao[] composicoes;
	private Tamanho[] tamanhos;

	private Paginator paginator = new Paginator(IConstants.QTD_EXIBIDA_NA_LISTAGEM_DE_PRODUTOS);
	
	private boolean habilitaEscolha;
	private boolean mostraTamanhos;
	
	/**
	 * Inicia o Bean por um Estado de Navegação já utilizado ou
	 * pelos parâmetros.
	 * 
	 */
	@PostConstruct
	public void init() 
	{
		this.habilitaEscolha = this.verificaDisponibilidadeDaEscolha();
		
		if (this.getEstadoDeNavegacao().isEstadoCriado() && !isParametrizado()) {
			this.restauraEstado();
			
		} else {
		
			try {
				
				this.preencheInformacoesDeNavegacao(paramDep, paramGrupo, paramSubgrupo);
				this.getEstadoDeNavegacao().criaEstado();
				
			} catch (RegraDeNegocioException e) {
				FacesUtil.addErrorMessage(e.getMessage() + " Clique nos links de navegação acima para listar as peças.");
			}
		}
		
		this.listarComPaginacao();
		this.verificaSeMostraTamanhos(this.produtos);
		this.salvaEstado();
		
		composicoes = Composicao.values();
		tamanhos = Tamanho.values();
	}
	
	/**
	 * Verifica se os produtos da lista tem a grade de 
	 * tamanho. 
	 */
	public void verificaSeMostraTamanhos(List<Produto> produtos) 
	{
		Predicate<Produto> predicate = p -> {
					return 	(p.getTipoGrade() == TipoGrade.TAMANHO) || 
							(p.getTipoGrade() == TipoGrade.COR_E_TAMANHO);};
			
		Optional<Produto> optProd = produtos
											.stream()
											.filter(predicate)
											.findAny();
		
		this.setMostraTamanhos(optProd.isPresent());
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
	private void preencheInformacoesDeNavegacao(Long paramDep, Long paramGrupo, Long paramSubgrupo) throws RegraDeNegocioException {
		
		if (null != paramDep) {
			
			try
			{
				dep = depSrvc.buscaPeloCodigo(paramDep);
				
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
		this.mostraTamanhos = this.getEstadoDeNavegacao().getEstado().isMostraTamanhos();
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
		this.salvaEstado();
	}
	
	/**
	 * Salva o estado de navegação.
	 */
	public void salvaEstado()
	{
		this.getEstadoDeNavegacao().salvaEstado(
				this.getDep(), 
				this.getGrupo(), 
				this.getSubgrupo(), 
				this.getFiltro(), 
				this.getPaginator(),
				this.mostraTamanhos);
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
	 * Lista os resultados utilizando a ordenação.
	 * 
	 * @param event Ordem selecionada.
	 */
	public void filtraComOrdemListener(ValueChangeEvent event) 
	{
		String ordem = (String) event.getNewValue();
		if (null != ordem) 
		{
			this.getFiltro().setOrdenacao(ordem);
		}
		
		this.reiniciaPaginator();
		
		listarComPaginacaoESalvarEstado();
	}
	
	/**
	 * Filtra os registros pelo tamanho selecionado.
	 * 
	 * @param event Tamanho selecionado.
	 */
	public void filtraPorTamanho(ValueChangeEvent event) 
	{
		String selecionado = (String) event.getNewValue();
		if (null != selecionado) 
		{
			this.getFiltro().setTamanho(Tamanho.valueOf(selecionado));
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
	
	public Tamanho[] getTamanhos() 
	{
		return tamanhos;
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

	public boolean isHabilitaEscolha() 
	{
		return habilitaEscolha;
	}
	public void setHabilitaEscolha(boolean habilitaEscolha) 
	{
		this.habilitaEscolha = habilitaEscolha;
	}

	public boolean isMostraTamanhos() 
	{
		return mostraTamanhos;
	}
	public void setMostraTamanhos(boolean mostraTamanhos) 
	{
		this.mostraTamanhos = mostraTamanhos;
	}
	
	public abstract EstadoDeNavegacao getEstadoDeNavegacao();

	public abstract FiltroListaProduto getFiltro();
	public abstract void setFiltro(FiltroListaProduto filtro);
	
	/**
	 * Verifica se o cliente pode adicionar peças ao carrinho.
	 */
	public abstract boolean verificaDisponibilidadeDaEscolha();
}
