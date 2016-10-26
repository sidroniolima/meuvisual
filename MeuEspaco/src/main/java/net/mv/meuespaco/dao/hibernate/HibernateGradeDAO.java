package net.mv.meuespaco.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;

import net.mv.meuespaco.dao.GradeDAO;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Implementação da camada Dao da entidade Grade.
 * 
 * @author Sidronio
 * 21/12/2015
 */
public class HibernateGradeDAO extends HibernateGenericDAO<Grade, Long> implements GradeDAO{
	
	private static final String sqlAlteraGrade = new StringBuilder()
			.append("UPDATE grade ")
			.append("SET tipo_grade = :novo_tipo ")
			.append("WHERE ")
			.append("produto_codigo = :produto_codigo AND ")
			.append("tipo_grade = 'UNICA';")
			.toString();

	@SuppressWarnings("unchecked")
	@Override
	public List<Grade> buscarGradesPorProduto(Produto produto) {
		
		Criteria criteria = this.getSession().createCriteria(Grade.class);
		criteria.add(Restrictions.eq("produto", produto));
		
		return criteria.list();
	}
	
	@Override
	public int alteraGradeDoProduto(Produto produto, TipoGrade novoTipo) {
		
		SQLQuery query = this.getSession().createSQLQuery(sqlAlteraGrade);
		query.setParameter("novo_tipo", novoTipo.toString());
		query.setParameter("produto_codigo", produto.getCodigo());
		
		return query.executeUpdate();
	}
}
