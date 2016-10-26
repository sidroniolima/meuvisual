package net.mv.meuespaco.dao;

import java.util.Collection;
import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaVenda;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ItemVenda;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface DAO para a entidade Venda.
 * 
 * @author Sidronio
 * @created 23/08/2016
 */
public interface VendaDAO extends GenericDAO<Venda, Long> {

	/**
	 * Lista vendas pelo cliente.
	 * 
	 * @param cliente
	 * @return lista de vendas.
	 */
	List<Venda> listarVendasPorCliente(Cliente cliente);

	/**
	 * Busca os itens de uma venda com os relacionamentos 
	 * entre grade, produto, subgrupo e grupo.
	 * 
	 * @param codigo
	 * @return
	 */
	List<ItemVenda> buscarItensCompletos(Long codigo);

	/**
	 * Busca uma Venda pelo código com os 
	 * relacionamentos de cliente, itens e seus produtos, 
	 * com grupo, subgrup e grade.
	 * 
	 * @param codigo
	 * @return
	 */
	Venda buscarCompleta(Long codigo);

	/**
	 * Lista os registros de venda que atenderem ao filtro.
	 * 
	 * @param filtro código, cliente, data de venda e status.
	 * @param paginator
	 * @return lista de vendas com cliente.
	 */
	List<Venda> filtrarPeloModoEspecifico(FiltroPesquisaVenda filtro, Paginator paginator);

	/**
	 * Busca a última venda do cliente de acordo com a data.
	 * 
	 * @param cliente
	 * @return última venda
	 */
	Venda buscarUltimaDoCliente(Cliente cliente);

}
