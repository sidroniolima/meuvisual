package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import net.mv.meuespaco.model.Finalidade;

/**
 * Implementação da Pesquisa de Produto para consignados.
 * 
 * @author sidronio
 * @created 19/01/2017
 */
@Named
@ViewScoped
public class PesquisaProdutoConsignadoBean extends PesquisaProdutoAbstractBean implements Serializable {

	private static final long serialVersionUID = -4901213277675439586L;

	@Override
	public Finalidade getFinalidade() {
		return Finalidade.CONSIGNADO;
	}

}
