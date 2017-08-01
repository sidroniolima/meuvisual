package net.mv.meuespaco.dao.hibernate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DateType;

import net.mv.meuespaco.controller.filtro.FiltroEscolha;
import net.mv.meuespaco.dao.EscolhaDAO;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.model.loja.StatusEscolha;
import net.mv.meuespaco.relatorio.RelatorioEscolha;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.UtilDateTimeConverter;

/**
 * Implementação da camada DAO da entidade Escolha;
 * 
 * @author Sidronio
 * 08/01/2016
 */
public class HibernateEscolhaDAO extends HibernateGenericDAO<Escolha, Long> implements EscolhaDAO{
	
	private static final String sqlRelatorio =
			new StringBuilder()
			.append("SELECT ")
			.append("e.codigo as 'codigo', ")
			.append("c.codigo_siga as 'codigoCliente', ")
			.append("c.nome as 'nomeCliente', ")
			.append("r.codigo_interno as 'codigoRegiao', ") 
			.append("r.descricao as 'descricaoRegiao', ")
			.append("e.data_envio as 'data', ")
			.append("p.codigo_interno as 'codigoProduto', ")
			.append("p.descricao as 'descricao', ")
			.append("g.localizacao as 'localizacao', ")
			.append("g.cor as 'cor', ")
			.append("g.tamanho as 'tamanho', ")
			.append("g.letra as 'letra', ")
			.append("i.qtd as 'qtd', ")
			.append("i.qtdAtendido as 'qtdAtendido' ")

			.append("FROM ")
			.append("escolha e ")
			.append("	LEFT JOIN itens_escolha i ON e.codigo = i.escolha_codigo ")
			.append("	LEFT JOIN produto p ON i.produto_codigo = p.codigo ")
			.append("   LEFT JOIN grade g ON i.grade_codigo = g.codigo ")
			.append("   LEFT JOIN cliente c ON e.cliente_codigo = c.codigo ")
			.append("   LEFT JOIN regiao r ON c.regiao_codigo = r.codigo ")

			.append("WHERE ")
			.append("e.codigo = :codigo ")	

			.append("ORDER BY ")
			.append("g.localizacao	 ")
			.toString();
			
	private static final String qryAtualizaEscolhasVencidas =
			new StringBuilder("UPDATE					")
			.append("escolha 							")
			.append("SET status_escolha = 'ENVIADA' 	")
			.append("WHERE 								")
			.append("date(data_envio) < :data 		AND	")
			.append("(status_escolha IS NULL 	OR 		")
			.append("status_escolha = 'PARCIAL');		")
			.toString();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Escolha> filtrarPeloModoEspecifico(FiltroEscolha filtro, Paginator paginator) {

		Criteria criteriaSublist = this.getSession().createCriteria(Escolha.class);
		
		criteriaSublist.createAlias("cliente", "cli", JoinType.LEFT_OUTER_JOIN);
		criteriaSublist.createAlias("cli.regiao", "reg", JoinType.LEFT_OUTER_JOIN);
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		if (null != filtro.getCodigo()) {
			criteriaSublist.add(Restrictions.eq("codigo", filtro.getCodigo()));
		}
		
		if (null != filtro.getDataInicial()) {
			
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
		
		if (null != filtro.getCodigoCliente() && !filtro.getCodigoCliente().isEmpty()) {
			criteriaSublist.add(Restrictions.eq("cli.codigoSiga", filtro.getCodigoCliente()));
		}
		
		if (null != filtro.getCodigoRegiao() && !filtro.getCodigoRegiao().isEmpty()) {
			criteriaSublist.add(Restrictions.eq("reg.codigoInterno", filtro.getCodigoRegiao()));
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
		
		Criteria criteria = getSession().createCriteria(Escolha.class);
		
		criteria.setFetchMode("itens", FetchMode.JOIN);
		criteria.setFetchMode("cliente", FetchMode.JOIN);
		criteria.setFetchMode("cliente.regiao", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("codigo"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	@Override
	public BigDecimal calcularQtdDePecasEscolhidasPorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim) {
		
		Criteria criteria = getSession().createCriteria(Escolha.class);
		
		criteria.setFetchMode("itens", FetchMode.JOIN);
		criteria.createAlias("itens", "item");
		criteria.createAlias("itens.produto", "produto");
		criteria.createAlias("produto.subgrupo", "subgrupo");
		criteria.createAlias("subgrupo.grupo", "grupo");
		
		criteria.setProjection(Projections.sum("item.qtd"));
		
		criteria.add(Restrictions.eq("cliente", cliente));
		criteria.add(Restrictions.sqlRestriction(
				"date(data) between ? and ?", 
				new Date[] {UtilDateTimeConverter.toDate(inicio), UtilDateTimeConverter.toDate(fim)},
				new DateType[] {DateType.INSTANCE, DateType.INSTANCE}));		
		criteria.add(Restrictions.eq("grupo.descontavel", true));
		
		return (BigDecimal) criteria.uniqueResult();
	}
	
	@Override
	public Escolha buscarAbertaDoCliente(Cliente cliente) {
		
		Criteria criteria = getSession().createCriteria(Escolha.class);
		
		criteria.setFetchMode("itens", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("cliente", cliente));
		
		criteria.add(Restrictions.eq("status", StatusEscolha.PARCIAL));
		
		criteria.addOrder(Order.desc("data"));
		
		return (Escolha) criteria.uniqueResult();
	}
	
	@Override
	public Escolha buscarPeloCodigoComItensOrdenadoPelaLocalizacao(Long codigo) {
		
		Criteria criteria = getSession().createCriteria(Escolha.class);
		
		criteria.setFetchMode("itens", FetchMode.JOIN);
		criteria.setFetchMode("itens.grade", FetchMode.JOIN);
		criteria.setFetchMode("itens.produto", FetchMode.JOIN);
		criteria.setFetchMode("itens.produto.subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("itens.produto.subgrupo.grupo", FetchMode.JOIN);
		
		criteria.setFetchMode("cliente", FetchMode.JOIN);
		criteria.setFetchMode("cliente.regiao", FetchMode.JOIN);
		
		criteria.createAlias("itens.grade", "grade", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("codigo", codigo));
		
		criteria.addOrder(Order.asc("grade.localizacao"));
		
		return (Escolha) criteria.uniqueResult();
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioEscolha> gerarInformacoesDoRelatorio(Long codigo) {
		
		SQLQuery criteria = getSession().createSQLQuery(sqlRelatorio);
		
		criteria.setParameter("codigo", codigo);
		
		criteria.setResultTransformer(new AliasToBeanResultTransformer(RelatorioEscolha.class));
		
		return criteria.list();
	}
	
	@Override
	public Escolha buscarComRelacionamentos(Long codigo) {
		Criteria criteria = getSession().createCriteria(Escolha.class);
		
		criteria.setFetchMode("cliente", FetchMode.JOIN);
		criteria.setFetchMode("itens", FetchMode.JOIN);
		criteria.setFetchMode("itens.grade", FetchMode.JOIN);

		criteria.setFetchMode("itens.produto", FetchMode.JOIN);
		criteria.setFetchMode("itens.produto.subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("itens.produto.subgrupo.grupo", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("codigo", codigo));
		
		return (Escolha) criteria.uniqueResult();
	}
	
	@Override
	public Escolha buscarEscolhaPorClienteEPeriodo(Cliente cliente, LocalDate dataInicial, LocalDate dataFinal) {
		Criteria criteria = getSession().createCriteria(Escolha.class);
		
		criteria.setFetchMode("cliente", FetchMode.JOIN);
		criteria.setFetchMode("itens", FetchMode.JOIN);
		criteria.setFetchMode("itens.produto", FetchMode.JOIN);

		criteria.add(Restrictions.eq("cliente", cliente));
		criteria.add(Restrictions.sqlRestriction(
				"date(data) between ? and ?", 
				new Date[] {UtilDateTimeConverter.toDate(dataInicial), UtilDateTimeConverter.toDate(dataFinal)},
				new DateType[] {DateType.INSTANCE, DateType.INSTANCE}));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return (Escolha) criteria.uniqueResult();
	}
	
	@Override
	public void atualizaStatusEDataDeEnvioDeEscolhasVencidas() {
		SQLQuery criteria = getSession().createSQLQuery(qryAtualizaEscolhasVencidas);
		
		criteria.setParameter("data", UtilDateTimeConverter.toDate(LocalDate.now()));
		
		criteria.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Escolha> filtrarPeloModoEspecificoSemPaginacao(FiltroEscolha filtro) 
	{
		Criteria criteria = getSession().createCriteria(Escolha.class);
		
		criteria.setFetchMode("itens", FetchMode.JOIN);
		criteria.setFetchMode("itens.produto", FetchMode.JOIN);
		
		criteria.createAlias("itens", "i", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cliente", "cli", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("cli.regiao", "reg", JoinType.LEFT_OUTER_JOIN);		
		
		if (null != filtro.getCodigo()) {
			criteria.add(Restrictions.eq("codigo", filtro.getCodigo()));
		}
		
		if (null != filtro.getDataInicial()) 
		{
			if (null == filtro.getDataFinal()) 
			{
				criteria.add(Restrictions.sqlRestriction("date(data_finalizacao) = ?", 
						UtilDateTimeConverter.toDate(filtro.getDataInicial()), DateType.INSTANCE));
			} else 
			{
				criteria.add(Restrictions.sqlRestriction(
						"date(data_finalizacao) between ? and ?", 
						new Date[] {UtilDateTimeConverter.toDate(filtro.getDataInicial()), 
											UtilDateTimeConverter.toDate(filtro.getDataFinal())}, 
						new DateType[] {DateType.INSTANCE, DateType.INSTANCE}));
			}
		}
		
		if (null != filtro.getCodigoCliente() && !filtro.getCodigoCliente().isEmpty()) {
			criteria.add(Restrictions.eq("cli.codigoSiga", filtro.getCodigoCliente()));
		}
		
		if (null != filtro.getCodigoRegiao() && !filtro.getCodigoRegiao().isEmpty()) {
			criteria.add(Restrictions.eq("reg.codigoInterno", filtro.getCodigoRegiao()));
		}
		
		if (null != filtro.getStatus()) 
		{
			criteria.add(Restrictions.eq("status", filtro.getStatus()));
		}		
		
		criteria.addOrder(Order.asc("codigo"));
		criteria.addOrder(Order.asc("i.codigo"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
}
