package net.mv.meuespaco.controller.filtro;

import java.io.Serializable;

import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.util.FiltroListaProdutoConsignadoAnnotation;

/**
 * Implementação do filtro para listagem de produtos para Venda.
 * 
 * @author Sidronio
 * @created 10/08/2016
 */
@FiltroListaProdutoConsignadoAnnotation
public class FiltroListaProdutoConsignado extends FiltroListaProduto implements Serializable {

	private static final long serialVersionUID = 5374745334919993372L;

	@Override
	public Finalidade getFinalidade() {
		return Finalidade.CONSIGNADO;
	}

}
