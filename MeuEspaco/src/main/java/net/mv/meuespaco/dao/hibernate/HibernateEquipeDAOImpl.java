package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.dao.EquipeDAO;
import net.mv.meuespaco.model.integracao.Equipe;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Implementação do DAO para Equipe.
 * 
 * @author sidronio
 * @created 06/02/2016
 */
@Stateless
public class HibernateEquipeDAOImpl extends HibernateGenericDAO<Equipe, Long> implements EquipeDAO, Serializable
{
	private static final long serialVersionUID = 3660182769893123873L;

	@Override
	public void removerTodosRegistros() 
	{
		SQLQuery query = this.getSession().createSQLQuery("TRUNCATE TABLE equipe");
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Equipe> listarEquipesPorCliente(Cliente cliente) 
	{
		Criteria query = this.getSession().createCriteria(Equipe.class);
		query.setFetchMode("regiaoEquipe", FetchMode.JOIN);
		
		query.add(Restrictions.eq("cliente", cliente));
		
		query.addOrder(Order.desc("statusEquipe"));
		query.addOrder(Order.asc("nomeEquipe"));
		
		return query.list();
	}
}
