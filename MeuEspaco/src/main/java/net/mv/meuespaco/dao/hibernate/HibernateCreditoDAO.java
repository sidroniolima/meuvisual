package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;

import net.mv.meuespaco.dao.CreditoDAO;
import net.mv.meuespaco.model.integracao.Credito;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.util.UtilDateTimeConverter;

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Credito> buscaCreditosDoClientePorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim) 
	{
		Criteria criteria = this.getSession().createCriteria(Credito.class);
		
		criteria.add(Restrictions.eq("cliente", cliente));
		
		criteria.add(Restrictions.sqlRestriction(
				"date(baixa) between ? and ?", 
				new Date[] {UtilDateTimeConverter.toDate(inicio), UtilDateTimeConverter.toDate(fim)},
				new DateType[] {DateType.INSTANCE, DateType.INSTANCE}));
		
		criteria.addOrder(Order.asc("classe"));
		criteria.addOrder(Order.asc("nome"));
		
		return criteria.list();
	}

	@Override
	public void removerRegistros() 
	{
		SQLQuery query = this.getSession().createSQLQuery("truncate table credito");
		query.executeUpdate();
	}
}
