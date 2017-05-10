package net.mv.meuespaco.service;

import java.util.List;

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
}
