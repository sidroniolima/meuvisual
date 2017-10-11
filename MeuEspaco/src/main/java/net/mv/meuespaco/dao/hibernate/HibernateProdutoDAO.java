package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.CollectionPropertyNames;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;

import net.mv.meuespaco.controller.ProdutosEQtdPorSubgrupo;
import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.controller.filtro.FiltroProduto;
import net.mv.meuespaco.dao.ProdutoDAO;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.consulta.ReferenciaProdutoComQtd;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.util.Paginator;

/**
 * Implementação da camada DAO para a Entidade Produto.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class HibernateProdutoDAO extends HibernateGenericDAO<Produto, Long> implements ProdutoDAO, Serializable
{

	private static final long serialVersionUID = 5349700629000674111L;
	
	private static final String sqlAtualizaTipoGrade = 
			new StringBuilder()
			.append("UPDATE produto ")
			.append("SET tipo_grade = :novo_tipo ")
			.append("WHERE ")
			.append("codigo = :produto_codigo ;")
			.toString();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> fitrarPeloDepartamentoEGrupo(Departamento dep, Grupo grupo, int first, int qtd) {
		
		Criteria criteria = this.getSession().createCriteria(Produto.class);
		
		criteria.setFetchMode("departamento", FetchMode.JOIN);
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("subgrupo.grupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		
		criteria.createAlias("subgrupo", "sub");
		
		criteria.add(Restrictions.eq("departamento", dep));
		criteria.add(Restrictions.eq("sub.grupo", grupo));
		
		criteria.add(Restrictions.eq("ativo", true));
		
		criteria.addOrder(Order.asc("descricao"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		criteria.setFirstResult(first);
		criteria.setMaxResults(qtd);
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> fitrarPeloDepartamentoESubgrupo(Departamento dep, Subgrupo subgrupo, int first, int qtd) {
		
		Criteria criteria = this.getSession().createCriteria(Produto.class);
		
		criteria.setFetchMode("departamento", FetchMode.JOIN);
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("departamento", dep));
		criteria.add(Restrictions.eq("subgrupo", subgrupo));
		
		criteria.add(Restrictions.eq("ativo", true));
		
		criteria.addOrder(Order.asc("descricao"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		criteria.setFirstResult(first);
		criteria.setMaxResults(qtd);
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> listarUltimosCadastros(int qtd) {
		
		Criteria subCriteria = this.getSession().createCriteria(Produto.class);
		subCriteria.setProjection(Projections.property("codigo"));
		subCriteria.add(Restrictions.eq("ativo", true));
		subCriteria.add(Restrictions.eq("finalidade", Finalidade.CONSIGNADO));
		
		subCriteria.addOrder(Order.desc("dataDeCadastro"));
		subCriteria.addOrder(Order.desc("codigo"));
		subCriteria.setMaxResults(qtd);
		
		Criteria criteria = this.getSession().createCriteria(Produto.class);
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		criteria.add(Restrictions.in("codigo", subCriteria.list()));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> filtrarPeloModoEspecifico(FiltroProduto filtro, int firstResult, int qtdPorPagina) {

		Criteria criteria = getSession().createCriteria(Produto.class);
		
		criteria.setFetchMode("departamento", FetchMode.JOIN);
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("subgrupo.grupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		
		criteria.createAlias("departamento", "dep", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("subgrupo", "sub", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("subgrupo.grupo", "gru", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("caracteristicas", "car", JoinType.LEFT_OUTER_JOIN);
		
		if (null != filtro.getCodigoInterno() && !filtro.getCodigoInterno().isEmpty()) {
			criteria.add(Restrictions.eq("codigoInterno", filtro.getCodigoInterno()));
		}
		
		if (null != filtro.getDescricao() && !filtro.getDescricao().isEmpty()) {
			criteria.add(Restrictions.like("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
		}
		
		if (null != filtro.getSubgrupo())
		{
			criteria.add(Restrictions.eq("subgrupo", filtro.getSubgrupo()));
		}
		
		if (null != filtro.getComposicao())
		{
			criteria.add(Restrictions.eq("composicao", filtro.getComposicao()));
		}		
		
		if (null != filtro.getDepartamentoDescricao() && !filtro.getDepartamentoDescricao().isEmpty())
		{
			criteria.add(Restrictions.eq("dep.descricao", filtro.getDepartamentoDescricao()));
		}
		
		if (null != filtro.getGrupoDescricao() && !filtro.getGrupoDescricao().isEmpty())
		{
			criteria.add(Restrictions.eq("gru.descricao", filtro.getGrupoDescricao()));
		}	
		
		if (null != filtro.getSubgrupoDescricao() && !filtro.getSubgrupoDescricao().isEmpty())
		{
			criteria.add(Restrictions.eq("sub.descricao", filtro.getSubgrupoDescricao()));
		}			
		
		if (null != filtro.getCaracteristica() && !filtro.getCaracteristica().isEmpty())
		{
			criteria.add(Restrictions.eq("car.valor", filtro.getCaracteristica()));
		}			
		
		if (null != filtro.getFinalidade())
		{
			criteria.add(Restrictions.eq("finalidade", filtro.getFinalidade()));
		}
		
		if (null != filtro.isAtivo())
		{
			criteria.add(Restrictions.eq("ativo", filtro.isAtivo()));
		}
		
		criteria.addOrder(Order.asc("codigoInterno"));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(qtdPorPagina);
		
		return criteria.list();
	}
	
	@Override
	public Produto buscarPeloCodigoInterno(String codigoInterno) {

		Criteria criteria = getSession().createCriteria(Produto.class);
		
		criteria.setFetchMode("grades", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("codigoInterno", codigoInterno));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return (Produto) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> listarCaracteristicasDeProdutos(Departamento dep, Grupo grupo,
			Subgrupo subgrupo) {

		Criteria criteria = this.getSession().createCriteria(Produto.class);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		criteria.createAlias("subgrupo", "sub");
		
		criteria.add(Restrictions.eq("departamento", dep));
		
		if (null != grupo) {
			criteria.add(Restrictions.eq("sub.grupo", grupo));
		}
		
		if (null != subgrupo) {
			criteria.add(Restrictions.eq("subgrupo", subgrupo));
		}
		
		criteria.add(Restrictions.eq("ativo", true));
		
		return criteria.list();
	}
	
	@Override
	public List<Produto> filtrarPeloCodigoInternoOuPelaDescricao(String pesquisa, Paginator paginator) {
		
		Criteria criteriaSublist = this.getSession().createCriteria(Produto.class);
		criteriaSublist.setFetchMode("subgrupo", FetchMode.JOIN);
		
		criteriaSublist.createAlias("caracteristicas", "carac", JoinType.LEFT_OUTER_JOIN);
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		criteriaSublist.add(Restrictions.eq("ativo", true));
		
		criteriaSublist.add(
				Restrictions.or(
						Restrictions.like("descricao", pesquisa, MatchMode.ANYWHERE),
						Restrictions.like("codigoInterno", pesquisa, MatchMode.ANYWHERE))
				);
		
		criteriaSublist.addOrder(Order.asc("codigoInterno"));
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
		
		Criteria criteria = getSession().createCriteria(Produto.class);
		
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	@Override
	public List<Produto> filtrarPeloCodigoInternoOuPelaDescricao(String pesquisa, Finalidade finalidade, Paginator paginator) {
		
		Criteria criteriaSublist = this.getSession().createCriteria(Produto.class);
		criteriaSublist.setFetchMode("subgrupo", FetchMode.JOIN);
		
		criteriaSublist.createAlias("caracteristicas", "carac", JoinType.LEFT_OUTER_JOIN);
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		criteriaSublist.add(Restrictions.eq("ativo", true));
		
		criteriaSublist.add(Restrictions.eq("finalidade", finalidade));
		
		criteriaSublist.add(
				Restrictions.or(
						Restrictions.like("descricao", pesquisa, MatchMode.ANYWHERE),
						Restrictions.like("codigoInterno", pesquisa, MatchMode.ANYWHERE))
				);
		
		criteriaSublist.addOrder(Order.asc("codigoInterno"));
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
		
		Criteria criteria = getSession().createCriteria(Produto.class);
		
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	@Override
	public List<Produto> fitrarPelaNavegacao(Departamento dep, Grupo grupo, Subgrupo subgrupo, FiltroListaProduto filtro,
			Paginator paginator) 
	{
		Order orderFiltro = Order.asc("codigoInterno");
		Order orderNatural = Order.asc("codigoInterno");
		
		Criteria criteriaSublist = this.getSession().createCriteria(Produto.class);
		criteriaSublist.setFetchMode("departamento", FetchMode.JOIN);
		criteriaSublist.setFetchMode("subgrupo", FetchMode.JOIN);
		criteriaSublist.setFetchMode("subgrupo.grupo", FetchMode.JOIN);
		criteriaSublist.createAlias("subgrupo", "sub");
		
		criteriaSublist.createAlias("caracteristicas", "carac", JoinType.LEFT_OUTER_JOIN);
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		criteriaSublist.add(Restrictions.eq("ativo", true));
		
		if (null != filtro) {
			
			if (null != filtro.getTipo()) {
				criteriaSublist.add(Restrictions.eq("tipo", filtro.getTipo()));
			}
			
			if (null != filtro.getComposicao()) {
				criteriaSublist.add(Restrictions.eq("composicao", filtro.getComposicao()));
			}
			
			if (null != filtro.getCaracteristica() && !filtro.getCaracteristica().isEmpty()) {
				criteriaSublist.add(Restrictions.eq("carac." + CollectionPropertyNames.COLLECTION_ELEMENTS, filtro.getCaracteristica()));
			}
			
			if (null != filtro.getOrdenacao())
			{
				String ordem = filtro.getOrdenacao();
				String direction = FiltroListaProduto.verificaOrdem(ordem);
				ordem = ordem.replaceAll("\\+|\\-", "");
				orderFiltro = (direction == "ASC" ? Order.asc(ordem) : Order.desc((ordem)));
				criteriaSublist.addOrder(orderFiltro);
			}
			
			criteriaSublist.add(Restrictions.eq("finalidade", filtro.getFinalidade()));
		}	
		
		criteriaSublist.add(Restrictions.eq("departamento", dep));
		
		if (null != grupo) {
			criteriaSublist.add(Restrictions.eq("sub.grupo", grupo));
		}
		
		if (null != subgrupo) {
			criteriaSublist.add(Restrictions.eq("subgrupo", subgrupo));
		}
		
		criteriaSublist.addOrder(orderNatural);
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty()) {
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
				
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
		
		Criteria criteria = this.getSession().createCriteria(Produto.class);
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		if (null != filtro.getOrdenacao())
		{
			criteria.addOrder(orderFiltro);
		}
		criteria.addOrder(orderNatural);
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	@Override
	public int atualizaGradeDoProduto(Long codigo, String novoTipo) {
		
		SQLQuery query = getSession().createSQLQuery(sqlAtualizaTipoGrade);
		
		query.setParameter("produto_codigo", codigo);
		query.setParameter("novo_tipo", novoTipo);
		
		return query.executeUpdate();
	}

	@Override
	public List<Produto> filtrarProdutosPorFinalidadeEDescricoes(String pesquisa, Finalidade finalidade, Paginator paginator) 
	{
		Criteria criteriaSublist = this.getSession().createCriteria(Produto.class);
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		criteriaSublist.add(Restrictions.eq("finalidade", finalidade));
		criteriaSublist.add(Restrictions.or(
					Restrictions.like("codigoInterno", pesquisa, MatchMode.ANYWHERE),
					Restrictions.like("descricao", pesquisa, MatchMode.ANYWHERE)));
		
		criteriaSublist.add(Restrictions.eq("ativo", true));

		criteriaSublist.addOrder(Order.asc("descricao"));
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty())
		{
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
		
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
		
		Criteria criteria = this.getSession().createCriteria(Produto.class);
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("descricao"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	@Override
	public List<Produto> filtrarProdutosPorFinalidadeEValor(Finalidade finalidade, BigDecimal min, BigDecimal max, Paginator paginator) 
	{
		Criteria criteriaSublist = this.getSession().createCriteria(Produto.class);
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		criteriaSublist.add(Restrictions.eq("finalidade", finalidade));
		criteriaSublist.add(Restrictions.between("valor", min, max));
		
		criteriaSublist.add(Restrictions.eq("ativo", true));

		criteriaSublist.addOrder(Order.desc("valor"));
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty())
		{
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
		
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
			
		Criteria criteria = this.getSession().createCriteria(Produto.class);
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.desc("valor"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}

	@Override
	public List<Produto> filtrarProdutosPorFinalidadeESubgrupo(Finalidade finalidade, Subgrupo subgrupo, Paginator paginator) 
	{
		Criteria criteriaSublist = this.getSession().createCriteria(Produto.class);
		
		criteriaSublist.setProjection(Projections.property("codigo"));
		
		criteriaSublist.add(Restrictions.eq("finalidade", finalidade));
		criteriaSublist.add(Restrictions.eq("subgrupo", subgrupo));
		
		criteriaSublist.add(Restrictions.eq("ativo", true));

		criteriaSublist.addOrder(Order.asc("descricao"));
		
		List registrosSublist = criteriaSublist.list();
		
		paginator.setTotalDeRegistros(registrosSublist.size());
		
		if (registrosSublist.isEmpty())
		{
			return null;
		}
		
		int lastResult = registrosSublist.size() <= paginator.getLastResult() ? 
				registrosSublist.size() : paginator.getLastResult() + 1;
		
		registrosSublist = registrosSublist.subList(paginator.getFirstResult(), lastResult);
		
		Criteria criteria = this.getSession().createCriteria(Produto.class);
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("caracteristicas", FetchMode.JOIN);
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		
		criteria.add(Restrictions.in("codigo", registrosSublist));
		
		criteria.addOrder(Order.asc("descricao"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	@Override
	public List<Produto> listarNProdutosMaisVendidosPorFinalidade(Finalidade finalidade, int numero) 
	{
		Criteria subCriteria = this.getSession().createCriteria(Produto.class);
		subCriteria.setProjection(Projections.property("codigo"));
		subCriteria.add(Restrictions.eq("ativo", true));
		subCriteria.add(Restrictions.eq("finalidade", finalidade));
		subCriteria.add(Restrictions.eq("ativo", true));
		
		subCriteria.addOrder(Order.desc("dataDeCadastro"));
		subCriteria.addOrder(Order.desc("codigo"));
		subCriteria.setMaxResults(numero);
		
		List<Produto> subResult = subCriteria.list();

		if (subResult.isEmpty())
			return subResult;
		
		Criteria criteria = this.getSession().createCriteria(Produto.class);
		criteria.setFetchMode("fotos", FetchMode.JOIN);
		criteria.add(Restrictions.in("codigo", subCriteria.list()));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();	
	}

	@Override
	public List<ProdutosEQtdPorSubgrupo> listarQtdDeProdutosPorSubgrupoEGrupo()
	{
		String strQuery = "SELECT " +
		"g.descricao as grupoDescricao, " +
		"s.descricao as subgrupoDescricao, " +
		"d.descricao as departamentoDescricao, " +
		"p.composicao as composicao, " +
		"c.valor as caracteristica, " +
		"COUNT(p.codigo) as qtd " +
		
		"FROM " +
		"produto p " +
		"LEFT JOIN sub_grupo s on p.subgrupo_codigo = s.codigo " +
		"LEFT JOIN grupo g on s.grupo_codigo = g.codigo " +
		"LEFT JOIN produto_caracteristicas c on c.produto_codigo = p.codigo " +
		"LEFT JOIN departamento d on d.codigo = p.departamento_codigo " +
		"WHERE " +
		"p.ativo = 1 AND " +
		"p.finalidade = 'CONSIGNADO' " +	
		
		"GROUP BY " +
		"g.descricao, " +
		"s.descricao, " +
		"d.descricao, " +
		"p.composicao, " + 
		"c.valor " +
		
		"ORDER BY " +
		"g.descricao, " +
		"s.descricao, " + 
		"d.descricao, " +
		"p.composicao, " +
		"c.valor";
		
		SQLQuery query = this.getSession().createSQLQuery(strQuery);
		query.setResultTransformer(new AliasToBeanResultTransformer(ProdutosEQtdPorSubgrupo.class));
		
		return query.list();
	}
	
	@Override
	public List<ReferenciaProdutoComQtd> detalharQtdDeProdutosPorSubgrupoEGrupo(FiltroProduto filtro)
	{
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("SELECT ")
		.append("p.codigo_interno as codigoInterno, ")
		.append("p.descricao as descricao, ")
		.append("coalesce( ")
		.append("	(")
		.append("		SELECT coalesce(sum(qtd),0.00) FROM movimento ")
		.append("		WHERE ")
		.append("		tipo_movimento = 'ENTRADA'		AND ")
		.append("		produto_codigo = p.codigo		AND ")
		.append("       a.codigo = almoxarifado_codigo) - ")
		.append("	( ")
		.append("		SELECT coalesce(sum(qtd),0.00) FROM movimento ")
		.append("		WHERE ")
		.append("		tipo_movimento = 'SAIDA'		AND ")
		.append("		produto_codigo = p.codigo		AND ")
		.append("       a.codigo = almoxarifado_codigo) ")
		.append(",0.00) AS 'qtd' ")
		
		.append("FROM ")
		.append("almoxarifado a, ")		
		.append("produto p ")
		.append("LEFT JOIN sub_grupo s on p.subgrupo_codigo = s.codigo ")
		.append("LEFT JOIN grupo g on s.grupo_codigo = g.codigo ")
		.append("LEFT JOIN produto_caracteristicas c on c.produto_codigo = p.codigo ")
		.append("LEFT JOIN departamento d on d.codigo = p.departamento_codigo ")
		
		.append("WHERE ")
		
		.append("a.codigo = 1	AND ")
		.append("p.ativo = 1 ");

		if (null != filtro.getComposicao())
		{
			strBuilder.append("AND p.composicao = '" + filtro.getComposicao() + "' ");
		}		
		
		if (null != filtro.getDepartamentoDescricao() && !filtro.getDepartamentoDescricao().isEmpty())
		{
			strBuilder.append("AND d.descricao = '" + filtro.getDepartamentoDescricao() + "' ");
		}
		
		if (null != filtro.getGrupoDescricao() && !filtro.getGrupoDescricao().isEmpty())
		{
			strBuilder.append("AND g.descricao = '" + filtro.getGrupoDescricao() + "' ");
		}	
		
		if (null != filtro.getSubgrupoDescricao() && !filtro.getSubgrupoDescricao().isEmpty())
		{
			strBuilder.append("AND s.descricao = '" + filtro.getSubgrupoDescricao() + "' ");
		}			
		
		if (null != filtro.getCaracteristica() && !filtro.getCaracteristica().isEmpty())
		{
			strBuilder.append("AND c.valor = '" + filtro.getCaracteristica() + "' ");	
		}			
		
		if (null != filtro.getFinalidade())
		{
			strBuilder.append("AND p.finalidade = '" + filtro.getFinalidade() + "' ");	
		}
		
		strBuilder.append("ORDER BY ")
		.append("p.descricao; ");
		
		SQLQuery query = this.getSession().createSQLQuery(strBuilder.toString());
		query.setResultTransformer(new AliasToBeanResultTransformer(ReferenciaProdutoComQtd.class));
		
		return query.list();
	}	
	
}