package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.DateType;
import org.hibernate.type.Type;

import net.mv.meuespaco.controller.filtro.FiltroEntradaProdutos;
import net.mv.meuespaco.controller.filtro.FiltroPesquisaMovimento;
import net.mv.meuespaco.dao.EstoqueDAO;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.consulta.EstoqueDoProdutoConsulta;
import net.mv.meuespaco.model.consulta.MovimentoPorComposicaoSubgrupo;
import net.mv.meuespaco.model.estoque.Almoxarifado;
import net.mv.meuespaco.model.estoque.Movimento;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.UtilDateTimeConverter;

/**
 * Implementação DAO do Estoque representando as movimentações.
 * 
 * @author Sidronio
 * 15/12/2015
 */
@Stateless
public class HibernateEstoqueDAO extends HibernateGenericDAO<Movimento, Long> implements EstoqueDAO, Serializable {

	private static final long serialVersionUID = 8481902443464795316L;
	
	private String sqlGradesDisponiveis = new StringBuilder()
			.append("SELECT ")
			.append("grade.codigo as 'codigo', ")
			.append("grade.localizacao as 'localizacao', ")
			.append("grade.cor as 'cor', ")
			.append("grade.tamanho as 'tamanho', ")
			.append("grade.produto_codigo as 'produto', ")
			.append("grade.letra as 'letra', ")
			.append("grade.simbolo as 'simbolo' ")

			.append("FROM  ")
			.append("produto prod,  ")
			.append("grade  ")

			.append("WHERE  ")
			.append("grade.produto_codigo = prod.codigo AND ")
			.append("grade.produto_codigo = :produto_codigo ")
			
			.append("HAVING ")
			.append("coalesce(  ")
			.append("( ")
			.append("	SELECT coalesce(sum(qtd),0.00) FROM movimento  ")
			.append("	WHERE  ")
			.append("		tipo_movimento = 'ENTRADA'		AND  ")
			.append("		produto_codigo = prod.codigo	AND  ")
			.append("		grade_codigo = grade.codigo		AND  ")
			.append("		almoxarifado_codigo = :almoxarifado_codigo ) - ")
			.append("(  ")
			.append("	SELECT coalesce(sum(qtd),0.00) FROM movimento  ")
			.append("	WHERE  ")
			.append("		tipo_movimento = 'SAIDA'		AND  ")
			.append("		produto_codigo = prod.codigo	AND  ")
			.append(" 		grade_codigo = grade.codigo		AND  ")
			.append("		almoxarifado_codigo = :almoxarifado_codigo ) ")
			.append(",0.00) > 0.00 ").toString();

	private String sqlQtdParaVenda = new StringBuilder()
			.append("SELECT ")
			.append("coalesce( ")
			.append("	(")
			.append("		SELECT coalesce(sum(qtd),0.00) FROM movimento ")
			.append("		WHERE ")
			.append("		tipo_movimento = 'ENTRADA'		AND ")
			.append("		produto_codigo = prod.codigo	AND ")
			.append("        grade_codigo = grade.codigo		AND ")
			.append("        alm.codigo = almoxarifado_codigo) - ")
			.append("	( ")
			.append("		SELECT coalesce(sum(qtd),0.00) FROM movimento ")
			.append("		WHERE ")
			.append("		tipo_movimento = 'SAIDA'		AND ")
			.append("		produto_codigo = prod.codigo	AND ")
			.append("        grade_codigo = grade.codigo		AND ")
			.append("        alm.codigo = almoxarifado_codigo) ")
			.append(",0.00) AS 'SALDO' ")
			     
			.append("FROM ")
			.append("produto prod, ")
			.append("grade, ")
			.append("almoxarifado alm ")

			.append("WHERE ")
			.append("grade.produto_codigo = prod.codigo AND ")
			.append("prod.codigo = :codigo_produto 		AND ")
			.append("alm.codigo = :codigo_alm 			AND ")
			.append("grade.codigo = :codigo_grade ").toString();
	

	private String sqlQtdDisponiveldoProduto = new StringBuilder()
			.append("SELECT ")
			.append("coalesce( ")
			.append("	(")
			.append("		SELECT coalesce(sum(qtd),0.00) FROM movimento ")
			.append("		WHERE ")
			.append("		tipo_movimento = 'ENTRADA'		AND ")
			.append("		produto_codigo = prod.codigo	AND ")
			.append("       alm.codigo = almoxarifado_codigo) - ")
			.append("	( ")
			.append("		SELECT coalesce(sum(qtd),0.00) FROM movimento ")
			.append("		WHERE ")
			.append("		tipo_movimento = 'SAIDA'		AND ")
			.append("		produto_codigo = prod.codigo	AND ")
			.append("       alm.codigo = almoxarifado_codigo) ")
			.append(",0.00) AS 'SALDO' ")
			     
			.append("FROM ")
			.append("produto prod, ")
			.append("almoxarifado alm ")

			.append("WHERE ")
			.append("prod.codigo = :codigo_produto 		AND ")
			.append("alm.codigo = :codigo_alm 				").toString();
	
	private String sqlEstoquedoProduto = new StringBuilder()
			.append("SELECT ")
			.append("prod.codigo as 'codigoProduto', ")
			.append("alm.descricao as 'almoxarifado', ")
			.append("grade.tipo_grade as 'tipoGrade', ")
			.append("grade.cor as 'cor', ")
			.append("grade.tamanho as 'tamanho', ")
			.append("coalesce(grade.letra, ' ') as 'letra', ")			
			.append("coalesce(grade.simbolo, ' ') as 'simbolo', ")			
			.append("coalesce( ")
			.append("	(")
			.append("		SELECT coalesce(sum(qtd),0.00) FROM movimento ")
			.append("		WHERE ")
			.append("		tipo_movimento = 'ENTRADA'		AND ")
			.append("		produto_codigo = prod.codigo	AND ")
			.append("       grade_codigo = grade.codigo		AND ")
			.append("       alm.codigo = almoxarifado_codigo) - ")
			.append("	( ")
			.append("		SELECT coalesce(sum(qtd),0.00) FROM movimento ")
			.append("		WHERE ")
			.append("		tipo_movimento = 'SAIDA'		AND ")
			.append("		produto_codigo = prod.codigo	AND ")
			.append("       grade_codigo = grade.codigo		AND ")
			.append("       alm.codigo = almoxarifado_codigo) ")
			.append(",0.00) AS 'qtd' ")
			     
			.append("FROM ")
			.append("produto prod, ")
			.append("grade, ")
			.append("almoxarifado alm ")

			.append("WHERE ")
			.append("grade.produto_codigo = prod.codigo AND ")
			.append("prod.codigo = :codigo_produto 			")
			
			.append("GROUP BY ")
			.append("alm.descricao, ")
			.append("grade.tipo_grade, ")
			.append("grade.tamanho, ")
			.append("grade.cor, ")
			.append("grade.letra, ")
			.append("grade.simbolo ")
			
			.append("ORDER BY ")
			.append("alm.descricao, ")
			.append("grade.tipo_grade;")			
			.toString();

	@Override
	public BigDecimal qtdEmEstoqueDoProdutoEGrade(Almoxarifado alm, Produto produto, Grade grade) {
		
		SQLQuery query = this.getSession().createSQLQuery(sqlQtdParaVenda);
		
		query.setParameter("codigo_produto", produto.getCodigo());
		query.setParameter("codigo_alm", alm.getCodigo());
		query.setParameter("codigo_grade", grade.getCodigo());
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal qtdEmEstoqueDoProduto(Almoxarifado alm, Produto produto) {

		SQLQuery query = this.getSession().createSQLQuery(sqlQtdDisponiveldoProduto);
		
		query.setParameter("codigo_produto", produto.getCodigo());
		query.setParameter("codigo_alm", alm.getCodigo());
		
		return (BigDecimal) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EstoqueDoProdutoConsulta> estoqueDoProduto(Produto produto) {
		
		SQLQuery query = this.getSession().createSQLQuery(sqlEstoquedoProduto);
		
		query.setParameter("codigo_produto", produto.getCodigo());
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueDoProdutoConsulta.class));
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Grade> gradesComSaldoPositivo(Produto produto, Almoxarifado almoxarifado) {
		
		SQLQuery query = this.getSession().createSQLQuery(sqlGradesDisponiveis);
		
		query.setParameter("produto_codigo", produto.getCodigo());
		query.setParameter("almoxarifado_codigo", almoxarifado.getCodigo());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Movimento> listarPeloFiltro(FiltroPesquisaMovimento filtro, Paginator paginator) 
	{
	
		Criteria criteriaSublist = this.getSession().createCriteria(Movimento.class);
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		criteriaSublist.createAlias("usuario", "usu", JoinType.LEFT_OUTER_JOIN);
		criteriaSublist.createAlias("produto", "pro", JoinType.LEFT_OUTER_JOIN);
		
		if (null != filtro)
		{
			
			if (null != filtro.getTipo())
				criteriaSublist.add(Restrictions.eq("tipoMovimento", filtro.getTipo()));
			
			if (null != filtro.getOrigem())
				criteriaSublist.add(Restrictions.eq("origem", filtro.getOrigem()));
			
			if (null != filtro.getNomeUsuario() && !filtro.getNomeUsuario().isEmpty())
				criteriaSublist.add(Restrictions.like("usu.nome", filtro.getNomeUsuario(), MatchMode.ANYWHERE));
			
			if (null != filtro.getCodigoInterno() && !filtro.getCodigoInterno().isEmpty())
				criteriaSublist.add(Restrictions.like("pro.codigoInterno", filtro.getCodigoInterno(), MatchMode.ANYWHERE));
			
			
			if (filtro.isPeriodoSimples())
			{
				criteriaSublist.add(Restrictions.sqlRestriction(
						"date(horario) = ?", 
						filtro.getDataInicialComoDate(),
						DateType.INSTANCE));
			}

			if (filtro.isPeriodoComposto())
			{
				criteriaSublist.add(Restrictions.sqlRestriction(
						"date(horario) between ? and ?", 
						new Date[] {filtro.getDataInicialComoDate(), filtro.getDataFinalComoDate()},
						new Type[] {DateType.INSTANCE, DateType.INSTANCE}));
			}
			
		}

		criteriaSublist.addOrder(Order.asc("horario"));
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
		
		Criteria criteria = this.getSession().createCriteria(Movimento.class);
		criteria.setFetchMode("usuario", FetchMode.JOIN);
		criteria.setFetchMode("produto", FetchMode.JOIN);
		criteria.setFetchMode("almoxarifado", FetchMode.JOIN);
		criteria.setFetchMode("grade", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("horario"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	@Override
	public List<MovimentoPorComposicaoSubgrupo> agruparMovimentacaoPeloFiltro(FiltroEntradaProdutos filtro, Paginator paginator) 
	{
		StringBuilder sqlStrBuilder = new StringBuilder()
			.append("SELECT ") 
			.append("DATE(horario) as data, ")
			.append("concat(f.descricao, ', ', g.descricao, ', ', s.descricao) as subgrupo, ")
			.append("p.composicao as composicao, ")
			.append("c.valor as caracteristica, ")
			.append("SUM(qtd) as qtd, ")
            .append("COUNT(distinct(m.produto_codigo)) as qtdItens ")			
            
			.append("FROM ")
			.append("movimento m ")
			.append("LEFT JOIN produto p ON p.codigo = m.produto_codigo ")
			.append("LEFT JOIN sub_grupo s ON s.codigo = p.subgrupo_codigo ")
			.append("LEFT JOIN grupo g ON g.codigo = s.grupo_codigo ")
			.append("LEFT JOIN familia f ON f.codigo = g.familia_codigo ")
			.append("LEFT JOIN produto_caracteristicas c on c.produto_codigo = p.codigo ")
				
			.append("WHERE ")
			.append("tipo_movimento = 'ENTRADA' AND ")
			.append("origem = 'AJUSTE' AND ")
			.append("DATE(horario) BETWEEN :data_inicial AND :data_final ");
				
		if (null != filtro.getFiltroProduto().getSubgrupo())
			sqlStrBuilder.append("AND s.codigo = :subgrupo ");
				
		if (null != filtro.getFiltroProduto().getComposicao())
			sqlStrBuilder.append("AND p.composicao = :composicao ");
				
		sqlStrBuilder.append("GROUP BY ")
			.append("DATE(horario), ")
			.append("s.descricao, ")
			.append("p.composicao, ")
			.append("c.valor ")
				
			.append("ORDER BY ")
			.append("DATE(horario), ")
			.append("s.descricao, ")
			.append("p.composicao, ")
			.append("c.valor ");

		
		SQLQuery query =  this.getSession().createSQLQuery(sqlStrBuilder.toString());
		
		query.setParameter("data_inicial", UtilDateTimeConverter.toDate(filtro.getFiltroPeriodo().getDataInicial()));				

		if (null != filtro.getFiltroProduto().getSubgrupo())
			query.setLong("subgrupo", filtro.getFiltroProduto().getSubgrupo().getCodigo());
				
		if (null != filtro.getFiltroProduto().getComposicao())
			query.setString("composicao", filtro.getFiltroProduto().getComposicao().toString());
		
		if (filtro.getFiltroPeriodo().isComposto())
		{
			query.setParameter("data_final", UtilDateTimeConverter.toDate(filtro.getFiltroPeriodo().getDataFinal()));
		} else 
		{
			query.setParameter("data_final", UtilDateTimeConverter.toDate(filtro.getFiltroPeriodo().getDataInicial()));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(MovimentoPorComposicaoSubgrupo.class));
		
		return query.list();
	}

}