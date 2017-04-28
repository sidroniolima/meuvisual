package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;

import net.mv.meuespaco.controller.filtro.FiltroPontuacao;
import net.mv.meuespaco.dao.PontuacaoDAO;
import net.mv.meuespaco.model.integracao.Pontuacao;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.UtilDateTimeConverter;

/**
 * Implementação DAO da entidade Pontuacao. 
 * 
 * @author sidronio
 * @created 25/04/2017
 */
@Stateless
public class HibernatePontuacaoDAO extends HibernateGenericDAO<Pontuacao, Long> implements PontuacaoDAO, Serializable
{
	private static final long serialVersionUID = -7512155564470760852L;
	
	private final String sqlTruncate = "TRUNCATE TABLE pontuacao";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pontuacao> buscarPeloCodigoCliente(String codigoCliente, Paginator paginator) 
	{		
		Criteria criteriaSublist = this.getSession().createCriteria(Pontuacao.class);
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		criteriaSublist.add(Restrictions.eq("codigoCliente", codigoCliente));
		
		criteriaSublist.addOrder(Order.asc("codigo"));
		
		List<Long> registrosSublist = criteriaSublist.list();
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
		
		Criteria criteria = this.getSession().createCriteria(Pontuacao.class);
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("codigo"));
		
		return criteria.list();
	}
	
	@Override
	public Long buscarSomaDosPontosAcumuladosPeloCodigoCliente(String codigoCliente) 
	{
		Criteria criteria = this.getSession().createCriteria(Pontuacao.class);
		criteria.setProjection(Projections.sum("pontos"));
		
		criteria.add(Restrictions.eq("codigoCliente", codigoCliente));
		
		return (Long) criteria.uniqueResult();
	}

	@Override
	public void removerRegistros() 
	{
		SQLQuery query = this.getSession().createSQLQuery(this.sqlTruncate);
		query.executeUpdate();
	}
	
	@Override
	public List<Pontuacao> filtrarPeloModoEspecifico(FiltroPontuacao filtro, Paginator paginator) 
	{
		Criteria criteriaSublist = this.getSession().createCriteria(Pontuacao.class);
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		if (filtro.getCodigoCliente() != null && !filtro.getCodigoCliente().isEmpty())
		{
			criteriaSublist.add(Restrictions.eq("codigoCliente", filtro.getCodigoCliente()));
		}
		
		if (filtro.getTipo() != null && !filtro.getTipo().isEmpty())
		{
			criteriaSublist.add(Restrictions.eq("tipo", filtro.getTipo()));
		}
		
		if (null != filtro.getDataInicial()) 
		{
			if (null == filtro.getDataFinal()) 
			{
				criteriaSublist.add(Restrictions.sqlRestriction("date(data) = ?", 
						UtilDateTimeConverter.toDate(filtro.getDataInicial()), DateType.INSTANCE));
			} else 
			{
				criteriaSublist.add(Restrictions.sqlRestriction(
						"date(data) between ? and ?", 
						new Date[] {UtilDateTimeConverter.toDate(filtro.getDataInicial()), 
											UtilDateTimeConverter.toDate(filtro.getDataFinal())}, 
						new DateType[] {DateType.INSTANCE, DateType.INSTANCE}));
			}
			
		}
		
		criteriaSublist.addOrder(Order.asc("codigo"));
		
		List<Long> registrosSublist = criteriaSublist.list();
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
		
		Criteria criteria = this.getSession().createCriteria(Pontuacao.class);
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("codigo"));
		
		return criteria.list();
	}
}
