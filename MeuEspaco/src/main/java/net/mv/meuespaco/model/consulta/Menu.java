package net.mv.meuespaco.model.consulta;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;

/**
 * Menu com informações de departamento, grupo e subgrupos.
 * 
 * @author Sidronio
 * 01/02/2016
 */
public class Menu {
	
	Map<Departamento, Map<Grupo, Set<Subgrupo>>> menus;
	
	/**
	 * Construtor padrão.
	 */
	public Menu() {	
		
		menus = new HashMap<Departamento, Map<Grupo, Set<Subgrupo>>>();
		
		for (Departamento dep: Departamento.values()) {
			menus.put(dep, new HashMap<Grupo, Set<Subgrupo>>());
		} 
	}
	
	/**
	 * Monta um menu para um departamento, grupo e subgrupos.
	 * 
	 * @param dep
	 * @param grupo
	 * @param subgrupos
	 */
	public void montaMenu(Departamento dep, Grupo grupoComSubgrupos) {
		
		Map<Grupo, Set<Subgrupo>> submenus = menus.get(dep);
		
		if (!submenus.containsKey(grupoComSubgrupos)){
			submenus.put(grupoComSubgrupos, grupoComSubgrupos.getSubgrupos());
			
		} else {
			/*List<Subgrupo> subsJaIncluidos = submenus.get(grupoComSubgrupos);
			List<Subgrupo> subsDoGrupo = grupoComSubgrupos.getSubgrupos();
			
			subsDoGrupo.removeAll(subsJaIncluidos);
			
			submenus.get(grupoComSubgrupos).addAll(subsDoGrupo);
			*/
		}
	}

	/**
	 * @return the menus
	 */
	public Map<Departamento, Map<Grupo, Set<Subgrupo>>> getMenus() {
		return menus;
	}
}