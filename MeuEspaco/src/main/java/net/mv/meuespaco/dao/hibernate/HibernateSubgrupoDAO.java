package net.mv.meuespaco.dao.hibernate;

import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.dao.SubgrupoDAO;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;

/**
 * Implementação da interface DAO da entidade Subgrupo.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class HibernateSubgrupoDAO extends HibernateGenericDAO<Subgrupo, Long> implements SubgrupoDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<Subgrupo> buscarPeloGrupo(Grupo grupo) {
		
		Criteria criteria = getSession().createCriteria(Subgrupo.class);
		
		criteria.add(Restrictions.eq("grupo", grupo));
		
		criteria.addOrder(Order.asc("descricao"));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Grupo> listaSubgrupoComGruposPorDepartamento(Departamento dep) 
	{
		Criteria criteria = getSession().createCriteria(Subgrupo.class);
		
		criteria.setFetchMode("grupo", FetchMode.JOIN);
		criteria.setFetchMode("produtos", FetchMode.JOIN);
		
		criteria.createAlias("produtos", "prod");
		
		criteria.add(Restrictions.eq("prod.departamento", dep));
		criteria.add(Restrictions.eq("prod.ativo", true));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Subgrupo> listarPorDepartamentoEGrupoDeProdutosAtivos(Departamento dep, Grupo grupo) {

		Criteria criteria = getSession().createCriteria(Subgrupo.class);
		
		criteria.setFetchMode("grupo", FetchMode.JOIN);
		criteria.setFetchMode("produtos", FetchMode.JOIN);
		
		criteria.createAlias("produtos", "prod");
		
		criteria.add(Restrictions.eq("grupo", grupo));
		criteria.add(Restrictions.eq("prod.departamento", dep));
		criteria.add(Restrictions.eq("prod.ativo", true));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
		
	}
}
