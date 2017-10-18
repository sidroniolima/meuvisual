package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.consulta.MenuPorDepartamento;
import net.mv.meuespaco.model.loja.Departamento;

public interface DepartamentoDAO extends GenericDAO<Departamento, Long>
{
	/**
	 * Lista os departamentos por finalidade com grupos e subgrupos 
	 * cujos produtos estejam ativos. 
	 * 
	 * @param finalidade
	 * @return Departamentos com grupos e subgrupos.
	 */
	List<MenuPorDepartamento> listarAtivosPorFinalidadeComGrupoESubgrupo(Finalidade finalidade);
}
