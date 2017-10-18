package net.mv.meuespaco.service;

import java.util.List;
import java.util.Map;

import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.consulta.MenuPorDepartamento;
import net.mv.meuespaco.model.loja.Departamento;

/**
 * Abstração da camada Service de Departamento.
 * 
 * @author sidronio
 *
 */
public interface DepartamentoService extends SimpleServiceLayer<Departamento, Long> 
{
	/**
	 * Lista os departamentos ativos
	 * 
	 * @return departamentos
	 */
	public List<Departamento> listaAtivos();

	/**
	 * Lista os departamentos ativos com grupos e subgrupos.
	 * @param finalidade
	 * @return Lista de departamentos.
	 */
	public Map<Departamento, Map<Grupo, Map<Subgrupo, List<MenuPorDepartamento>>>> listaAtivosComGruposESubgrupos(Finalidade finalidade);
}
