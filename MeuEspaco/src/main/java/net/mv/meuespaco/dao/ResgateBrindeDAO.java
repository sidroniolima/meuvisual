package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ResgateBrinde;

/**
 * Abstração da camada DAO para a entidade ResgateBrinde.
 * 
 * @author sidronio
 * @created 02/06/2017
 */
public interface ResgateBrindeDAO extends GenericDAO<ResgateBrinde, Long> 
{

	/**
	 * Lista os últimos n resgates do cliente.
	 * 
	 * @param cliente
	 * @param num número de resgates
	 * @return 10 últimos resgates.
	 */
	List<ResgateBrinde> listarUltimosResgatesPorCliente(Cliente cliente, int num);

}
