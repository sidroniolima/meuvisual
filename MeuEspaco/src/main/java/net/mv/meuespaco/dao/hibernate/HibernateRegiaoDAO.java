package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.dao.RegiaoDAO;
import net.mv.meuespaco.model.Regiao;

/**
 * Implementação estendida da camada DAO da Entidade Região. 
 * 
 * @author Sidronio
 * 05/11/2015
 */
@Stateless
public class HibernateRegiaoDAO extends HibernateGenericDAO<Regiao, Long> implements RegiaoDAO, Serializable{

	private static final long serialVersionUID = 4040242637239513034L;

	@Override
	public Regiao buscarPeloCodigoInterno(String codigoInterno) 
	{
		Criteria query = this.getSession().createCriteria(Regiao.class);
		
		query.add(Restrictions.eq("codigoInterno", codigoInterno));
		
		return (Regiao) query.uniqueResult();
	}
}
