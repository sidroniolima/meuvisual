package net.mv.meuespaco.controller;

import net.mv.meuespaco.controller.filtro.FiltroDePesquisa;
import net.mv.meuespaco.util.IConstants;
import net.mv.meuespaco.util.Paginator;

/**
 * Classe acessória para listagens de registros com 
 * navegaçõe e filtro apenas;
 * 
 * @author Sidronio
 * @created 06/07/2016
 */
public abstract class ListaSimples {
	
	/**
	 * Paginador utilizado na navegação dos registros de modo Lazy.
	 */
	private Paginator paginator = new Paginator(IConstants.QTD_PADRAO_REGISTROS_POR_PAGINA);
	
	/**
	 * Inicia os atributos do Bean.
	 */
	public abstract void init();
	
	/**
	 * Lista os registros com paginação.
	 */
	public abstract void listarComPaginacao();
	
	/** 
	 * Retorna uma instância de paginador padrão.
	 * 
	 * @return
	 */
	public Paginator getPaginator() {
		return paginator;
	}

	public abstract FiltroDePesquisa getFiltro();
}
