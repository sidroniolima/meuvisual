package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.util.FiltroListaProdutoVendaAnnotation;

/**
 * Implementação da listagem de produtos para venda.
 * 
 * @author Sidronio
 * @created 10/08/2016
 */
@ViewScoped
@Named
public class ListaProdutosVendaBean extends ListaProdutosAbstractBean implements Serializable {

	private static final long serialVersionUID = -3029144559746168672L;

	@Inject
	@FiltroListaProdutoVendaAnnotation
	private FiltroListaProduto filtro;
	
	@Override
	public void listarComPaginacao() {
		super.setProdutos(
				super.getProdutoService().listaProdutosPelaNavegacao(
						super.getDep(), 
						super.getGrupo(), 
						super.getSubgrupo(), 
						this.getFiltro(), 
						super.getPaginator()));
	}

	@Override
	public FiltroListaProduto getFiltro() {
		return filtro;
	}
	
	@Override
	public void setFiltro(FiltroListaProduto filtro) {
		this.filtro = filtro;
	}

}
