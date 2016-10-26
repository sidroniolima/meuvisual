package net.mv.meuespaco.service;

import java.util.List;

import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Grupo;

/**
 * Interface da camada Service da entidade Grupo.
 * 
 * @author Sidronio
 * 17/11/2015
 */
public interface GrupoService extends SimpleServiceLayer<Grupo, Long>{

	/**
	 * Retorna uma instância de Grupo pelo código com 
	 * o relacionamento de Familia.
	 * 
	 * @param codigo
	 * @return
	 */
	public Grupo buscaGrupoComFamilia(Long codigo);
	
	/**
	 * Lista os grupos com Familia ordenado 
	 * pela descrição da família e do grupo.
	 * 
	 * @return
	 */
	public List<Grupo> listaTodosComFamilia();

	/**
	 * Lista grupos com seus subgrupos.
	 * @return
	 */
	public List<Grupo> buscaTodosComSubGrupos();
	
	/**
	 * Lista os grupos por departamento relacionado com 
	 * seus subgrupos de produtos de um departamento 
	 * específico.
	 * 
	 * @param dep Departamento para consulta.
	 * @return Lista de grupos.
	 */
	public List<Grupo> listaGruposPorDepartamento(Departamento dep);
	
	/**
	 * Lista os grupos por departamento relacionado com 
	 * seus subgrupos de produtos de um departamento 
	 * específico para produtos de finalidade consignado.
	 * 
	 * @param dep Departamento para consulta.
	 * @return Lista de grupos.
	 */
	public List<Grupo> listaGruposPorDepartamentoParaConsignado(Departamento dep);
	
	/**
	 * Lista os grupos por departamento relacionado com 
	 * seus subgrupos de produtos de um departamento 
	 * específico para produtos de finalidade venda.
	 * 
	 * @param dep Departamento para consulta.
	 * @return Lista de grupos.
	 */
	public List<Grupo> listaGruposPorDepartamentoParaVenda(Departamento dep);	
}
