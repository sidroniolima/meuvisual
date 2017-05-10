package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;

/**
 * Interface DAO para a entidade Grupo.
 * 
 * @author Sidronio
 * 17/11/2015
 */
public interface GrupoDAO extends GenericDAO<Grupo, Long>{

	/**
	 * Lista os grupos cadastrados para produtos com subgrupos 
	 * dos produtos de um departamento específico.
	 * 
	 * @param dep Departamento específico.
	 * @return Lista de grupos com subgrupos.
	 */
	public List<Grupo> listaGrupoComSubgruposPorDepartamento(Departamento dep);

	/**
	 * Lista os Grupos com Subgrupos para os departamentos 
	 * e finalidade dos produtos ativos.
	 * 
	 * @param dep
	 * @param consignado
	 * @return Lista de grupos.
	 */
	public List<Grupo> listaGrupoComSubgruposPorDepartamentoEFinalidade(
			Departamento dep, 
			Finalidade consignado);
}
