package net.mv.meuespaco.service;

import java.util.List;

import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;

/**
 * Interface da camada Service da entidade Subgrupo.
 * 
 * @author Sidronio
 * 17/11/2015
 */
public interface SubgrupoService extends SimpleServiceLayer<Subgrupo, Long> {

	/**
	 * Retorna uma instância de subgrupo pelo código 
	 * com relacionamento entre Grupo e Familia.
	 * 
	 * @param codigo
	 * @return
	 */
	public Subgrupo buscaPeloCodigoComGrupoEFamilia(Long codigo);

	/**
	 * Retorna os registros de subgrupos com a associação 
	 * de grupo e família.
	 * 
	 * @return
	 */
	public List<Subgrupo> buscaTodosComGrupoEFamilia();

	/**
	 * Lista os subgrupo do grupo.
	 * 
	 * @param grupo Grupo para pesquisa.
	 * @return Lista de subgrupos do grupo.
	 */
	public List<Subgrupo> buscaPeloGrupo(Grupo grupo);
	
	/**
	 * Lista os subgrupos por departamento relacionado com 
	 * seus grupos de produtos de um departamento 
	 * específico.
	 * 
	 * @param dep Departamento para consulta.
	 * @return Lista de subgrupos.
	 */
	public List<Grupo> listaSubgruposPorDepartamento(Departamento dep);

	/**
	 * Retorna a lista de Subgrupo de um determinado Departamento 
	 * e Grupo, dos produtos ativos.
	 * 
	 * @param dep Departamento informado.
	 * @param grupo Grupo informado.
	 * @return Lista de subgrupos.
	 */
	public List<Subgrupo> listarSubgruposPorDepEGrupo(Departamento dep, Grupo grupo);

}
