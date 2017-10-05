package net.mv.meuespaco.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.model.Finalidade;
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
 * 
 * Alteração em 19/01/2017
 * @author Sidronio
 * 
 * Tornar abstract para diferenciar a pesquisa 
 * do consignado e venda.
 * 
 */
public abstract class PesquisaProdutoAbstractBean {

	@Inject
	private ProdutoService produtoSrvc;
	
	@Inject @Param
	private String paramPesquisa;
	
	private List<Produto> produtos;
	
	private Paginator paginator = new Paginator(IConstants.QTD_EXIBIDA_NA_LISTAGEM_DE_PRODUTOS);
	
	private boolean habilitaEscolha; 
	/**
	 * Inicia o Bean.
	 */
	@PostConstruct
	public void init() 
	{
		this.habilitaEscolha = this.verificaDisponibilidadeDaEscolha();
		
		if (null != paramPesquisa && !paramPesquisa.isEmpty()) {
			listarComPaginacao();
		}
		
	}

	/**
	 * Lista o resultado da pesquisa de forma paginada.
	 */
	public void listarComPaginacao() {
		produtos = produtoSrvc.filtrarProdutosPelaPesquisaDoUsuario(paramPesquisa, paginator, this.getFinalidade());
	}
	/**
	 * Verifica se o cliente pode adicionar peças ao carrinho.
	 */
	public abstract boolean verificaDisponibilidadeDaEscolha();
	
	/**
	 * Finalidade para a pesquisa.
	 * 
	 * @return finalidade.
	 */
	public abstract Finalidade getFinalidade();
	
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
	
	public ProdutoService getProdutoSrvc() 
	{
		return produtoSrvc;
	}

	public boolean isHabilitaEscolha() {
		return habilitaEscolha;
	}
	public void setHabilitaEscolha(boolean habilitaEscolha) {
		this.habilitaEscolha = habilitaEscolha;
	}
}
