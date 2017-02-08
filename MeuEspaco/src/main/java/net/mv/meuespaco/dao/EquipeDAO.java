package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.model.integracao.Equipe;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Abstração DAO para Equipe.
 * 
 * @author sidronio
 * @created 06/02/2017
 */
public interface EquipeDAO extends GenericDAO<Equipe, Long> {

	/**
	 * Truncate de Equipes.
	 */
	void removerTodosRegistros();
	
	/**
	 * Lista a equipe do cliente. 
	 */
	List<Equipe> listarEquipesPorCliente(Cliente cliente);

}
