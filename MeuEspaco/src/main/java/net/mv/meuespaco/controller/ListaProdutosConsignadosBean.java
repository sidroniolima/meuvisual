package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.util.FiltroListaProdutoConsignadoAnnotation;

/**
 * Implementação da listagem de produtos para consignados.
 * 
 * @author Sidronio
 * @created 10/08/2016
 */
@Named
@ViewScoped
public class ListaProdutosConsignadosBean extends ListaProdutosAbstractBean implements Serializable{

	private static final long serialVersionUID = -1044183808309706397L;
	
	@Inject
	@FiltroListaProdutoConsignadoAnnotation
	private FiltroListaProduto filtro;
	
	/**
	 * Lista os registros de produtos para consignação de forma paginada.
	 */
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
