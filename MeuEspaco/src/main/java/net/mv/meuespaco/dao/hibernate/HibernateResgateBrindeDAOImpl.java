package net.mv.meuespaco.dao.hibernate;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.dao.ResgateBrindeDAO;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ResgateBrinde;

/**
 * Implementação da camada DAO para a entidade ResgateBrinde.
 * 
 * @author sidronio
 * @created 02/06/2017
 */
@Stateless
public class HibernateResgateBrindeDAOImpl extends HibernateGenericDAO<ResgateBrinde, Long> implements ResgateBrindeDAO
{
	@SuppressWarnings("unchecked")
	@Override
	public List<ResgateBrinde> listarUltimosResgatesPorCliente(Cliente cliente, int num) 
	{
		Criteria criteria = this.getSession().createCriteria(ResgateBrinde.class);
		criteria.setFetchMode("brindes", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("cliente", cliente));

		criteria.addOrder(Order.desc("horario"));
		criteria.setMaxResults(num);
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

}
