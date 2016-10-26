package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.UsuarioLogado;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.estoque.Ajuste;
import net.mv.meuespaco.model.estoque.Almoxarifado;
import net.mv.meuespaco.model.estoque.ItemAjuste;
import net.mv.meuespaco.model.estoque.TipoMovimento;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.service.AlmoxarifadoService;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Bean do ajuste de estoque.
 * 
 * @author Sidronio
 * 15/12/2015
 */
@Named
@ViewScoped
public class AjusteEstoqueBean extends CadastroMasterDetail implements Serializable {

	private static final long serialVersionUID = -6864485099458351310L;

	@Inject
	private EstoqueService estoqueSrvc;
	
	@Inject
	private ProdutoService produtoSrvc;
	
	@Inject
	private AlmoxarifadoService almoxarifadoSrvc;
	
	@Inject
	@UsuarioLogado
	private Usuario usuario;

	private Ajuste ajuste;
	private ItemAjuste item;
	private ItemAjuste itemSelecionado;
	
	private List<Produto> produtos;
	private List<Almoxarifado> almoxarifados;
	
	private TipoMovimento[] tiposMovs;
	
	private List<Grade> gradesDisponiveis;
	
	private String pesquisaProduto;
	
	@Override
	@PostConstruct
	public void init() {
		
		produtos = produtoSrvc.buscaTodas();
		almoxarifados = almoxarifadoSrvc.buscaTodas();
		
		tiposMovs = TipoMovimento.values();
		
		novoRegistro();
	}

	@Override
	void novoRegistro() {
		ajuste = new Ajuste();
		ajuste.setUsuario(usuario);

		novoItem();
	}

	@Override
	public void salvar() {
		
		try {
			
			estoqueSrvc.ajusta(ajuste);
			
			FacesUtil.addSuccessMessage(this.getMensagemDeInclusao(ajuste.getTipoMovimento().getDescricao()));
			
			novoRegistro();
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(String.format("Não foi possível realizar o ajuste.", e.getMessage()));
		}
	}

	/**
	 * Preenche as informações do produto e grades de acordo com o 
	 * código interno pesquisado pelo usuário. 
	 */
	public void preencheInfoDoProduto() {
		
		if (!pesquisaProduto.isEmpty()) {
			
			Produto produto = produtoSrvc.buscaPeloCodigoInterno(pesquisaProduto);
			
			if (null == produto) {
				FacesUtil.addErrorMessage("Não foi possível localizar o produto com o código informado.");
			
			} else {
				
				gradesDisponiveis = produto.getGrades();
				item.atualizaPeloProduto(produto);

			}
		}
	}
	
	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Ajuste de %s realizado com sucesso.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return null;
	}
	
	@Override
	public boolean isEdicaoDeItem() {
		return null != itemSelecionado;
	}

	@Override
	public void editaItem() {
		item = itemSelecionado;
	}

	@Override
	String getMensagemDeInclusaoDeItem(String registro) {
		return String.format("%s incluído.", registro);
	}

	@Override
	String getMensagemDeAlteracaoDeItem(String registro) {
		return String.format("%s alterado.", registro);
	}

	@Override
	String getMensagemDeExclusaoDeItem(String registro) {
		return String.format("%s excluído.", registro);
	}

	@Override
	public void adicionaItem() {
		try {
			
			item.valida();
			
			if (isEdicaoDeItem()) {
				
				FacesUtil.addSuccessMessage(this.getMensagemDeAlteracaoDeItem(item.toString()));
			
			} else {
				ajuste.adicionaItem(item);
			
				FacesUtil.addSuccessMessage(this.getMensagemDeInclusaoDeItem(item.toString()));
			}
			
			novoItem();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	@Override
	public void removeItem() {
		
		ajuste.removeItem(itemSelecionado);
		FacesUtil.addSuccessMessage(this.getMensagemDeExclusaoDeItem(itemSelecionado.toString()));
	}

	@Override
	public void novoItem() {
		item = new ItemAjuste();
		itemSelecionado = null;
		
		pesquisaProduto = "";
	}
	
	/**
	 * @return the ajuste
	 */
	public Ajuste getAjuste() {
		return ajuste;
	}
	/**
	 * @param ajuste the ajuste to set
	 */
	public void setAjuste(Ajuste ajuste) {
		this.ajuste = ajuste;
	}

	/**
	 * @return the item
	 */
	public ItemAjuste getItem() {
		return item;
	}
	/**
	 * @param item the item to set
	 */
	public void setItem(ItemAjuste item) {
		this.item = item;
	}

	/**
	 * @return the itemSelecionado
	 */
	public ItemAjuste getItemSelecionado() {
		return itemSelecionado;
	}
	/**
	 * @param itemSelecionado the itemSelecionado to set
	 */
	public void setItemSelecionado(ItemAjuste itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}

	/**
	 * @return the produtos
	 */
	public List<Produto> getProdutos() {
		return produtos;
	}

	/**
	 * @return the almoxarifados
	 */
	public List<Almoxarifado> getAlmoxarifados() {
		return almoxarifados;
	}

	/**
	 * @return the tiposMovs
	 */
	public TipoMovimento[] getTiposMovs() {
		return tiposMovs;
	}

	/**
	 * @return the gradesDisponiveis
	 */
	public List<Grade> getGradesDisponiveis() {
		return gradesDisponiveis;
	}

	/**
	 * @return the pesquisaProduto
	 */
	public String getPesquisaProduto() {
		return pesquisaProduto;
	}
	/**
	 * @param pesquisaProduto the pesquisaProduto to set
	 */
	public void setPesquisaProduto(String pesquisaProduto) {
		this.pesquisaProduto = pesquisaProduto;
	}
	
}
