package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.controller.filtro.IFiltroPesquisaAcao;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.util.Paginator;

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

	/**
	 * Soma os pontos utilizados em resgates do cliente.
	 * 
	 * @param cliente para pesquisa
	 * @return total de pontos.
	 */
	Long buscarPontosResgatadosDoCliente(Cliente cliente);

	/**
	 * Filtra os registros elo modo específico, 
	 * utilizando o código, data inicial e final, cliente ou 
	 * status.
	 * 
	 * @param filtro Filtro da pesquisa.
	 * @param paginator Primeiro registro e quantidade de registros por página.
	 * @return Lista de registros paginados.
	 */
	List<ResgateBrinde> filtrarPeloModoEspecifico(IFiltroPesquisaAcao filtro, Paginator paginator);
}
