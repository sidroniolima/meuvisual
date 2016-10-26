package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Grupo;

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
	public List<Grupo> criaMenuPorDepartamento(Departamento dep) {
		return this.getGrupoSrvc().listaGruposPorDepartamentoParaVenda(dep);
	}

	@Override
	protected String getUrl() {
		return "/private/site/venda/lista-produtos-da-pesquisa.xhtml?faces-redirect=true&paramPesquisa=";
	}

}
