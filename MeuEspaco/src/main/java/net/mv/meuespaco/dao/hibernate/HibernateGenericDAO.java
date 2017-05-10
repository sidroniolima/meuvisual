package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.util.Paginator;

public class HibernateGenericDAO<T, ID extends Serializable> implements GenericDAO<T, ID>{

	@PersistenceContext
	private EntityManager manager;
	
	private Class<T> classeDaEntidade;
	
	@SuppressWarnings("unchecked")
	public HibernateGenericDAO() {
		
		classeDaEntidade = (Class<T>) 
				( (ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	public T salvar(T entidade) {
		return manager.merge(entidade);
	}

	@Override
	public void excluir(ID id) {
		T entidade = buscarPeloCodigo(id);
		
		manager.remove(entidade);
		
		manager.flush();
		manager.clear();

	}

	@Override
	public T buscarPeloCodigo(ID id) {
		return manager.find(classeDaEntidade, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> filtrar(
			T entidade, 
			List<String> propriedades, 
			List<String> relacionamentos, 
			List<String> ordenacao, 
			List<String> aliases) {
		
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(classeDaEntidade);

		if (null != aliases) {
			for (String alias : aliases) {
				
				if (alias.contains(".")) {
					criteria.createAlias(alias, alias.substring(alias.lastIndexOf('.') + 1, alias.length()));
				} else {
					criteria.createAlias(alias, alias);
				}
				
			}
		}	
		
		if (propriedades != null) {
			
			for (String propriedade : propriedades) {
				
				try {
					
					Object valor = PropertyUtils.getProperty(entidade, propriedade);
					
					if (null != valor) {
						
						if (propriedade.equals("nome") || propriedade.equals("descricao")) {
							
							criteria.add(Restrictions.ilike(propriedade, valor.toString(), MatchMode.ANYWHERE));
						} else 
						{
							criteria.add(Restrictions.eq(propriedade, valor));
						}
					}
					
				} catch (IllegalAccessException | InvocationTargetException
						| NoSuchMethodException e) {
					
					throw new RuntimeException("Propriedade não encontrada");
				}
			}
			
		}
		
		if (null != relacionamentos) {
		
			for (String relacionamento : relacionamentos) {
				
				criteria.setFetchMode(relacionamento, FetchMode.JOIN);
			} 
		}
		
		if (null != ordenacao) {
			for (String ordem : ordenacao) {
				criteria.addOrder(Order.asc(ordem));
			}
		}
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	protected EntityManager getManager() {
		return manager;
	}
	
	protected Session getSession() {
		return manager.unwrap(Session.class);
	}
	
	protected Class<T> getClasseDaEntidade() {
		return classeDaEntidade;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> listarComPaginacao(
			Paginator paginator, 
			List<String> ordenacao, 
			List<String> relacionamentos,
			List<String> aliases) {
		
		Session session = manager.unwrap(Session.class);
		Criteria criteriaSublist = session.createCriteria(classeDaEntidade);
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		if (null != ordenacao) {
			for (String ordem : ordenacao) {
				criteriaSublist.addOrder(Order.asc(ordem));
			}
		}
		
		if (null != aliases) {
			for (String alias : aliases) {
				
				if (alias.contains(".")) {
				
					criteriaSublist.createAlias(alias, alias.substring(alias.lastIndexOf('.') + 1, alias.length()));
				} else {
					criteriaSublist.createAlias(alias, alias);
				}
			} 
		}		
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);		
		
		Criteria criteria = session.createCriteria(classeDaEntidade);
		
		if (null != relacionamentos) {
			
			for (String relacionamento : relacionamentos) {
				
				criteria.setFetchMode(relacionamento, FetchMode.JOIN);
			} 
		}
		
		if (null != aliases) {
			for (String alias : aliases) {
				
				if (alias.contains(".")) {
					criteria.createAlias(alias, alias.substring(alias.lastIndexOf('.') + 1, alias.length()));
				} else {
					criteria.createAlias(alias, alias);
				}
				
			}
		}		
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		if (null != ordenacao) {
			for (String ordem : ordenacao) {
				criteria.addOrder(Order.asc(ordem));
			}
		}
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T buscarPeloCodigoComRelacionamento(ID id, List<String> relacionamentos) {

		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(classeDaEntidade);

		criteria.add(Restrictions.eq("codigo", id));
		
		if (null != relacionamentos) {
		
			for (String relacionamento : relacionamentos) {
				
				criteria.setFetchMode(relacionamento, FetchMode.JOIN);
			} 
		}
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return (T) criteria.uniqueResult();
	}

}
