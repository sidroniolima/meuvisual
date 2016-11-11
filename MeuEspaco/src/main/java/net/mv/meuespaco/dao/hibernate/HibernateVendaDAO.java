package net.mv.meuespaco.dao.hibernate;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.DateType;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaVenda;
import net.mv.meuespaco.dao.VendaDAO;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ItemVenda;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.UtilDateTimeConverter;

/**
 * Implementação DAO para a entidade Venda.
 * 
 * @author Sidronio
 * @created 23/08/2016
 */
@Stateless
public class HibernateVendaDAO extends HibernateGenericDAO<Venda, Long> implements VendaDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<Venda> listarVendasPorCliente(Cliente cliente) {
		
		Criteria query = this.getSession().createCriteria(Venda.class);
		
		query.setFetchMode("cliente", FetchMode.JOIN);
		query.setFetchMode("itens", FetchMode.JOIN);
		query.setFetchMode("itens.produto", FetchMode.JOIN);
		
		query.createAlias("itens.produto", "prod");
		
		query.add(Restrictions.eq("cliente", cliente));
		
		query.addOrder(Order.asc("horarioVenda"));
		
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ItemVenda> buscarItensCompletos(Long codigo) {
		
		Criteria query = this.getSession().createCriteria(ItemVenda.class);
		
		query.setFetchMode("grade", FetchMode.JOIN);		
		query.setFetchMode("produto", FetchMode.JOIN);
		query.setFetchMode("produto.subgrupo", FetchMode.JOIN);
		query.setFetchMode("produto.subgrupo.grupo", FetchMode.JOIN);
		query.setFetchMode("produto.fotos", FetchMode.JOIN);
		
		query.createAlias("produto", "prod");
		query.createAlias("venda", "vend");
		
		query.add(Restrictions.eq("vend.codigo", codigo));
		
		query.addOrder(Order.asc("prod.codigoInterno"));
		
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return query.list();
	}
	
	@Override
	public Venda buscarCompleta(Long codigo) {
		
		Criteria query = this.getSession().createCriteria(Venda.class);
		
		query.setFetchMode("grade", FetchMode.JOIN);
		query.setFetchMode("itens", FetchMode.JOIN);
		
		query.setFetchMode("itens.produto", FetchMode.JOIN);
		query.setFetchMode("itens.produto.subgrupo", FetchMode.JOIN);
		query.setFetchMode("itens.produto.subgrupo.grupo", FetchMode.JOIN);
		
		query.createAlias("itens.produto", "prod");
		
		query.add(Restrictions.eq("codigo", codigo));
		
		query.addOrder(Order.asc("prod.codigoInterno"));
		
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return (Venda) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Venda> filtrarPeloModoEspecifico(FiltroPesquisaVenda filtro, Paginator paginator) {
		
		Criteria criteriaSublist = this.getSession().createCriteria(Venda.class);
		criteriaSublist.createAlias("cliente", "cli");
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		if (null != filtro.getCodigo())
		{
			criteriaSublist.add(Restrictions.eq("codigo", filtro.getCodigo()));
		}
		
		if (null != filtro.getCodigoCliente() && !filtro.getCodigoCliente().isEmpty())
		{
			criteriaSublist.add(Restrictions.eq("cli.codigoSiga", filtro.getCodigoCliente()));
		}
		
		if (null != filtro.getDataInicial())
		{
			
			if (null == filtro.getDataFinal())
			{
				criteriaSublist.add(Restrictions.sqlRestriction("date(horario_venda) = ?", 
						UtilDateTimeConverter.toDate(filtro.getDataInicial()), DateType.INSTANCE));
			} else 
			{
				criteriaSublist.add(Restrictions.sqlRestriction(
						"date(horario_venda) between ? and ?", 
						new Date[] {UtilDateTimeConverter.toDate(filtro.getDataInicial()), 
											UtilDateTimeConverter.toDate(filtro.getDataFinal())}, 
						new DateType[] {DateType.INSTANCE, DateType.INSTANCE}));
			}
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

		Criteria criteria = this.getSession().createCriteria(Venda.class);
		criteria.setFetchMode("cliente", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("codigo"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	@Override
	public Venda buscarUltimaDoCliente(Cliente cliente) 
	{
		Criteria subQuery = this.getSession().createCriteria(Venda.class);
		
		subQuery.setProjection(Projections.distinct(Projections.property("codigo")));
		subQuery.add(Restrictions.eq("cliente", cliente));
		
		subQuery.addOrder(Order.desc("horarioVenda"));
		subQuery.setMaxResults(1);
		
		Long codigo = (Long) subQuery.uniqueResult();
		
		if (null == codigo)
		{
			return null;
		}
		
		Criteria query = this.getSession().createCriteria(Venda.class);
				
		query.setFetchMode("grade", FetchMode.JOIN);
		query.setFetchMode("itens", FetchMode.JOIN);
		
		query.createAlias("itens", "it");
		
		query.setFetchMode("itens.produto", FetchMode.JOIN);
		query.setFetchMode("itens.produto.subgrupo", FetchMode.JOIN);
		query.setFetchMode("itens.produto.subgrupo.grupo", FetchMode.JOIN);
		
		query.createAlias("itens.produto", "prod");
		
		query.add(Restrictions.eq("codigo", codigo));
		
		query.addOrder(Order.asc("prod.codigoInterno"));
		
		return (Venda) query.uniqueResult();
	}
}
