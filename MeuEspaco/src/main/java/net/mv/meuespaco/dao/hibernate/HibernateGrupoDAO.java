package net.mv.meuespaco.dao.hibernate;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.dao.GrupoDAO;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;

/**
 * Implementação da interface DAO da entidade Grupo.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class HibernateGrupoDAO extends HibernateGenericDAO<Grupo, Long> implements GrupoDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> listaGrupoComSubgruposPorDepartamento(Departamento dep) {
		
		Criteria criteria = getSession().createCriteria(Grupo.class);
		
		criteria.setFetchMode("subgrupos", FetchMode.JOIN);
		
		criteria.createAlias("subgrupos.produtos", "prod");
		
		criteria.add(Restrictions.eq("prod.departamento", dep));
		criteria.add(Restrictions.eq("prod.ativo", true));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> listaGrupoComSubgruposPorDepartamentoEFinalidade(
			Departamento dep, 
			Finalidade finalidade) {
		
		Criteria criteria = getSession().createCriteria(Grupo.class);
		
		criteria.setFetchMode("subgrupos", FetchMode.JOIN);
		
		criteria.createAlias("subgrupos.produtos", "prod");
		
		criteria.add(Restrictions.eq("prod.departamento", dep));
		criteria.add(Restrictions.eq("prod.ativo", true));
		criteria.add(Restrictions.eq("prod.finalidade", finalidade));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}
}
