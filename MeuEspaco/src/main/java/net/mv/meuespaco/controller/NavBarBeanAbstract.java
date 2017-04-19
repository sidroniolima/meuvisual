package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.consulta.Menu;
import net.mv.meuespaco.service.GrupoService;

/**
 * Abstração da barra (nav) de navegação utilizado nas implementações 
 * da navegação para Consigando e Venda.
 * 
 * @author Sidronio
 * @create 10/08/2016
 */
public abstract class NavBarBeanAbstract implements Serializable {
	
	private static final long serialVersionUID = -2882194940330116877L;

	@Inject
	private GrupoService grupoSrvc;
	
	private Departamento[] departamentos;
	
	private String pesquisa;
	
	@PostConstruct
	public void init() {
		
		if (null == departamentos) {
			departamentos = Departamento.getAtivos();
		}
	}
	
	/**
	 * Cria os menus pela lista de subgrupos com grupo  
	 * do departamento de acordo com a finalidade.
	 * 
	 * @param dep Departamento atual.
	 * @return Lista de subgrupos com grupos.
	 */
	public abstract List<Grupo> criaMenuPorDepartamento(Departamento dep);	
	/**
	 * Criação do menu estático.
	 * 
	 * @param dep
	 * @return Menu estático.
	 */
	public Map<Grupo, Set<Subgrupo>> montaMenuPeloDepartamento(Departamento dep) {
		List<Grupo> gruposComSubgrupos = grupoSrvc.listaGruposPorDepartamento(dep);
		
		Menu menu = new Menu();
		
		for (Grupo grupo : gruposComSubgrupos) {
			menu.montaMenu(dep, grupo);
		}
		
		return menu.getMenus().get(dep);
	}
	
	/**
	 * Redireciona a pesquisa para a listagem de produtos.
	 * 
	 * @return
	 */
	public String pesquisar() 
	{
		if (pesquisa != null && !pesquisa.isEmpty()) { 

			return 
				new StringBuilder()
					.append(getUrl())
					.append(pesquisa).toString();
		}
		
		return null;
		
	}

	/**
	 * Informa a URL de resposta da pesquisa.
	 * 
	 * @return URL da pesquisa.
	 */
	protected abstract String getUrl();
	
	/**
	 * @return
	 */
	public Departamento[] getDepartamentos() {
		return departamentos;
	}

	/**
	 * @return the pesquisa
	 */
	public String getPesquisa() {
		return pesquisa;
	}
	/**
	 * @param pesquisa the pesquisa to set
	 */
	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public GrupoService getGrupoSrvc() {
		return grupoSrvc;
	}
}
