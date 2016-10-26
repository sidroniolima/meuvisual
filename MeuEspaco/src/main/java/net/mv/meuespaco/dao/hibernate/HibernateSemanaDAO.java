package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.dao.SemanaDAO;
import net.mv.meuespaco.model.Semana;

/**
 * Implementação da camada DAO para a entidade Semana.
 * 
 * @author Sidronio
 * 12/11/2015
 */
@Stateless
public class HibernateSemanaDAO extends HibernateGenericDAO<Semana, Long> implements Serializable, SemanaDAO{

	private static final long serialVersionUID = -827637457188788807L;

	@Override
	public Long buscarQtdDeRegioesDaSemana(Semana semana) {
		
		Criteria criteria = getSession().createCriteria(Semana.class);
		criteria.createAlias("regioes", "regiao");
		criteria.setFetchMode("regioes", FetchMode.JOIN);
		
		criteria.setProjection(Projections.count("regiao.codigo"));
		criteria.add(Restrictions.eq("codigo", semana.getCodigo()));
		
		return (Long) criteria.uniqueResult();
	}

}
