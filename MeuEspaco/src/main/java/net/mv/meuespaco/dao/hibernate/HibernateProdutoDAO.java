package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
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

import net.mv.meuespaco.controller.PesquisaProdutoBean.FiltroProduto;
import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.dao.ProdutoDAO;
import net.mv.meuespaco.model.Departamento;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.util.Paginator;

/**
 * Implementação da camada DAO para a Entidade Produto.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class HibernateProdutoDAO extends HibernateGenericDAO<Produto, Long> implements ProdutoDAO, Serializable{

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
		
		criteria.setFetchMode("subgrupo", FetchMode.JOIN);
		criteria.setFetchMode("subgrupo.grupo", FetchMode.JOIN);
		
		if (null != filtro.getCodigoInterno() && !filtro.getCodigoInterno().isEmpty()) {
			criteria.add(Restrictions.eq("codigoInterno", filtro.getCodigoInterno()));
		}
		
		if (null != filtro.getDescricao() && !filtro.getDescricao().isEmpty()) {
			criteria.add(Restrictions.like("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
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
			Paginator paginator) {

		Criteria criteriaSublist = this.getSession().createCriteria(Produto.class);
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
			
			criteriaSublist.add(Restrictions.eq("finalidade", filtro.getFinalidade()));
		}	
		
		criteriaSublist.add(Restrictions.eq("departamento", dep));
		
		if (null != grupo) {
			criteriaSublist.add(Restrictions.eq("sub.grupo", grupo));
		}
		
		if (null != subgrupo) {
			criteriaSublist.add(Restrictions.eq("subgrupo", subgrupo));
		}
		
		criteriaSublist.addOrder(Order.asc("descricao"));
		
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
		
		criteria.addOrder(Order.asc("descricao"));
		
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
	
}
