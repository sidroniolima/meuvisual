package net.mv.meuespaco.dao;

import java.time.LocalDate;
import java.util.List;

import net.mv.meuespaco.model.integracao.Credito;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Interface DAO da classe Credito.
 * 
 * @author sidronio
 * @created 02/01/2017
 */
public interface CreditoDAO extends GenericDAO<Credito, Long>
{
	/**
	 * Busca os Creditos do cliente por período.
	 * 
	 * @param cliente
	 * @param inicio
	 * @param fim
	 * @return
	 */
	public List<Credito> buscaCreditosDoClientePorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim);

	/**
	 * Remove os registros da tabela.
	 */
	public void removerRegistros();
}
