package net.mv.meuespaco.dao.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.DateType;

import net.mv.meuespaco.controller.filtro.IFiltroPesquisaAcao;
import net.mv.meuespaco.dao.ResgateBrindeDAO;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.UtilDateTimeConverter;

/**
 * Implementação da camada DAO para a entidade ResgateBrinde.
 * 
 * @author sidronio
 * @created 02/06/2017
 */
@Stateless
public class HibernateResgateBrindeDAOImpl extends HibernateGenericDAO<ResgateBrinde, Long> implements ResgateBrindeDAO
{
	private final String queryPontosResgatados = new StringBuilder()
			.append("SELECT ")
			.append("	sum(i.qtd * i.valor_unitario) ")
			
			.append("FROM ")
			.append("	resgate_brinde r, ")
			.append("	item_resgate i ")
			
			.append("WHERE ")
			.append("	r.codigo = i.resgate_codigo		AND ")
			.append("	r.cliente_codigo = :cliente ")
			.toString();
	
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

	@Override
	public Long buscarPontosResgatadosDoCliente(Cliente cliente) 
	{
		SQLQuery query = this.getSession().createSQLQuery(this.queryPontosResgatados);
		query.setParameter("cliente", cliente.getCodigo());
		
		BigDecimal result = (BigDecimal) query.uniqueResult();
		
		if (null == result)
			return 0L;
		
		return result.longValue(); 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResgateBrinde> filtrarPeloModoEspecifico(IFiltroPesquisaAcao filtro, Paginator paginator) {

		Criteria criteriaSublist = this.getSession().createCriteria(ResgateBrinde.class);
		
		criteriaSublist.createAlias("cliente", "cli", JoinType.LEFT_OUTER_JOIN);
		criteriaSublist.createAlias("cli.regiao", "reg", JoinType.LEFT_OUTER_JOIN);
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		if (null != filtro.getCodigo()) {
			criteriaSublist.add(Restrictions.eq("codigo", filtro.getCodigo()));
		}
		
		if (null != filtro.getDataInicial()) {
			
			if (null == filtro.getDataFinal()) 
			{
				criteriaSublist.add(Restrictions.sqlRestriction("date(horario) = ?", 
						UtilDateTimeConverter.toDate(filtro.getDataInicial()), DateType.INSTANCE));
			} else 
			{
				criteriaSublist.add(Restrictions.sqlRestriction(
						"date(horario) between ? and ?", 
						new Date[] {UtilDateTimeConverter.toDate(filtro.getDataInicial()), 
											UtilDateTimeConverter.toDate(filtro.getDataFinal())}, 
						new DateType[] {DateType.INSTANCE, DateType.INSTANCE}));
			}
			
		}
		
		if (null != filtro.getCodigoCliente() && !filtro.getCodigoCliente().isEmpty()) {
			criteriaSublist.add(Restrictions.eq("cli.codigoSiga", filtro.getCodigoCliente()));
		}
		
		if (null != filtro.getStatus()) 
		{
			criteriaSublist.add(Restrictions.eq("status", filtro.getStatus()));
		}
		
		criteriaSublist.addOrder(Order.asc("codigo"));
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);	
		
		Criteria criteria = getSession().createCriteria(ResgateBrinde.class);
		
		criteria.setFetchMode("itens", FetchMode.JOIN);
		criteria.setFetchMode("cliente", FetchMode.JOIN);
		criteria.setFetchMode("cliente.regiao", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("codigo"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

}
