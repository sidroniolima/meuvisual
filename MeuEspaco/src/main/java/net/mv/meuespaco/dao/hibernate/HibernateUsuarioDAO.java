package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;

import net.mv.meuespaco.dao.UsuarioDAO;
import net.mv.meuespaco.model.Usuario;

/**
 * Implementação da camada DAO da entidade Usuario.
 * 
 * @author Sidronio
 * 05/01/2016
 */
@Stateless
public class HibernateUsuarioDAO extends HibernateGenericDAO<Usuario, Long> implements UsuarioDAO, Serializable {

	private static final long serialVersionUID = -1292034632158618122L;

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> filtrarPelaPesquisa(String login, String nome) {
		
		Criteria query = this.getSession().createCriteria(Usuario.class);
		query.setFetchMode("permissoes", FetchMode.JOIN);
		
		if (null != login && !login.isEmpty()) {
			query.add(Restrictions.like("login", login, MatchMode.ANYWHERE));
		}
		
		if (null != nome && !nome.isEmpty()) {
			query.add(Restrictions.like("nome", nome, MatchMode.ANYWHERE));
		}
		
		query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return query.list();
	}
	
	@Override
	public void inativaUsuarios()
	{
		Query query = this.getSession().createSQLQuery(
				"UPDATE "
				+ "usuario u set u.ativo = 0 "
				+ "WHERE "
				+ "u.codigo IN "
				+ "(	SELECT c.usuario_codigo FROM"
				+ "			cliente c WHERE "
				+ "			c.status = 'INATIVO')");	
		
		query.executeUpdate();
	}
	
	@Override
	public Usuario buscarPeloLoginESenha(String login, String senha) {
		
		Criteria criteria = this.getSession().createCriteria(Usuario.class);
		criteria.setFetchMode("permissoes", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("login", login));
		criteria.add(Restrictions.eq("senha", senha));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.setCacheable(true);
		return (Usuario) criteria.uniqueResult();
	}
	
	@Override
	public void reativaUsuarios() 
	{
		Query query = this.getSession().createSQLQuery(
				"UPDATE "
				+ "usuario u set u.ativo = 1 "
				+ "WHERE "
				+ "u.codigo IN "
				+ "(	SELECT c.usuario_codigo FROM"
				+ "			cliente c WHERE "
				+ "			c.status = 'ATIVO')");	
		
		query.executeUpdate();
		
	}
}
