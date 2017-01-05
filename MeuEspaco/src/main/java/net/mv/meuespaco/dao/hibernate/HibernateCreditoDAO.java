package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;

import net.mv.meuespaco.dao.CreditoDAO;
import net.mv.meuespaco.integracao.Credito;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Implmentação DAO da classe Credito.
 * 
 * @author sidronio
 * @created 02/01/2017
 */
@Stateless
public class HibernateCreditoDAO extends HibernateGenericDAO<Credito, Long> implements CreditoDAO, Serializable
{

	private static final long serialVersionUID = -1903122128861538039L;

	@Override
	public List<Credito> buscaCreditosDoClientePorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim) 
	{
		return Arrays.asList(
				new Credito(1L, "000004", "DANIELA SILVA GODOY", 10.10d, "COM", "", LocalDate.of(2016, 8, 14)),
				new Credito(2L, "000004", "DANIELA SILVA GODOY", 10.10d, "COM", "<20PÇ", LocalDate.of(2016, 8, 14)),
				new Credito(3L, "000004", "DANIELA SILVA GODOY", 10.10d, "DBT", "", LocalDate.of(2016, 8, 14)),
				new Credito(4L, "000004", "DANIELA SILVA GODOY", 10.10d, "CDT", "", LocalDate.of(2016, 8, 14)));
	}

}
