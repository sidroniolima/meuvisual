package net.mv.meuespaco.controller.filtro;

import java.io.Serializable;

import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.util.FiltroListaProdutoVendaAnnotation;

/**
 * Implementação do filtro para listagem de produtos para Venda.
 * 
 * @author Sidronio
 * @created 10/08/2016
 */
@FiltroListaProdutoVendaAnnotation
public class FiltroListaProdutoVenda extends FiltroListaProduto implements Serializable {
	
	private static final long serialVersionUID = 8740019231697206142L;

	@Override
	public Finalidade getFinalidade() {
		return Finalidade.VENDA;
	}

}
