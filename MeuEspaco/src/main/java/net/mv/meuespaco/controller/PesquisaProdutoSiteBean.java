package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.IConstants;
import net.mv.meuespaco.util.Paginator;

/**
 * Classe da camada View responsável pela exibição dos 
 * resultados da pesquisa de produtos.
 * 
 * @author Sidronio
 * 02/03/2016
 */
@Named
@ViewScoped
public class PesquisaProdutoSiteBean implements Serializable {

	private static final long serialVersionUID = 4326911169519596036L;

	@Inject
	private ProdutoService produtoSrvc;
	
	@Inject @Param
	private String paramPesquisa;
	
	private List<Produto> produtos;
	
	private Paginator paginator = new Paginator(IConstants.QTD_EXIBIDA_NA_LISTAGEM_DE_PRODUTOS);
	
	/**
	 * Inicia o Bean.
	 */
	@PostConstruct
	public void init() {
		
		if (null != paramPesquisa && !paramPesquisa.isEmpty()) {
			listarComPaginacao();
		}
		
	}

	/**
	 * Lista o resultado da pesquisa de forma paginada.
	 */
	public void listarComPaginacao() {
		produtos = produtoSrvc.filtrarProdutosPelaPesquisaDoUsuario(paramPesquisa, paginator);
	}
	
	/**
	 * @return the paramPesquisa
	 */
	public String getParamPesquisa() {
		return paramPesquisa;
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

	/**
	 * @return the produtos
	 */
	public List<Produto> getProdutos() {
		return produtos;
	}

}
