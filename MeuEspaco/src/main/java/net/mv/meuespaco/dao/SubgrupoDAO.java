package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;

/**
 * Interface DAO para a entidade Subgrupo.
 * 
 * @author Sidronio
 * 17/11/2015
 */
public interface SubgrupoDAO extends GenericDAO<Subgrupo, Long>{

	/**
	 * Busca os registros de subgrupo que pertencem ao 
	 * grupo de pesquisa.
	 * 
	 * @param grupo Grupo de pesquisa.
	 * @return Lista de subgrupos do grupo.
	 */
	List<Subgrupo> buscarPeloGrupo(Grupo grupo);
	
	/**
	 * Lista os subgrupos cadastrados para produtos com grupo  
	 * de produtos de um departamento específico.
	 * 
	 * @param dep Departamento específico.
	 * @return Lista de subgrupos com grupo.
	 */
	public List<Grupo> listaSubgrupoComGruposPorDepartamento(Departamento dep);

	/**
	 * Lista os Subgrupo de um Departamento e Grupo, dos produtos ativos.
	 * 
	 * @param dep
	 * @param grupo
	 * @return
	 */
	List<Subgrupo> listarPorDepartamentoEGrupoDeProdutosAtivos(Departamento dep, Grupo grupo);

}
