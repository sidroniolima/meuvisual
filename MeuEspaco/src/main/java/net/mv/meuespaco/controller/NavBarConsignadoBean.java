package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.loja.Departamento;

/**
 * Implementação da navegação dos produtos de finalidade Consignado.
 * 
 * @author Sidronio
 * @updated 10/08/2016
 */
@Named
@RequestScoped
public class NavBarConsignadoBean extends NavBarBeanAbstract implements Serializable {

	private static final long serialVersionUID = 567080204723347689L;

	/**
	 * Cria os menus pela lista de subgrupos com grupo  
	 * do departamento para os produtos com finalidade Consignado.
	 * 
	 * @param dep Departamento atual.
	 * @return Lista de subgrupos com grupos.
	 */
	public List<Grupo> criaMenuPorDepartamento(Departamento dep) 
	{
		return super.getGrupoSrvc().listaGruposPorDepartamentoParaConsignado(dep);
	}

	protected String getUrl() {
		return "/private/site/lista-produtos-da-pesquisa.xhtml?faces-redirect=true&paramPesquisa=";
	}

	@Override
	public Finalidade getFinalidade() 
	{
		return Finalidade.CONSIGNADO;
	}
}
	
