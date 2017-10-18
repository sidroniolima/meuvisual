package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import net.mv.meuespaco.model.Finalidade;

/**
 * Implementação da navegação dos produtos de finalidade Consignado.
 * 
 * @author Sidronio
 * @updated 10/08/2016
 */
@Named
@RequestScoped
public class NavBarVendaBean extends NavBarBeanAbstract implements Serializable {

	private static final long serialVersionUID = 8981484860819747800L;

	@Override
	protected String getUrl() {
		return "/private/venda/lista-produtos-da-pesquisa.xhtml?faces-redirect=true&paramPesquisa=";
	}

	@Override
	public Finalidade getFinalidade() 
	{
		return Finalidade.VENDA;
	}
}
