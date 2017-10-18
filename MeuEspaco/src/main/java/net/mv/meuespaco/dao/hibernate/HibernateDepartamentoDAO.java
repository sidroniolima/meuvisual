package net.mv.meuespaco.dao.hibernate;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;

import net.mv.meuespaco.dao.DepartamentoDAO;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.consulta.MenuPorDepartamento;
import net.mv.meuespaco.model.loja.Departamento;

public class HibernateDepartamentoDAO extends HibernateGenericDAO<Departamento, Long> implements DepartamentoDAO 
{
	@Override
	public List<MenuPorDepartamento> listarAtivosPorFinalidadeComGrupoESubgrupo(Finalidade finalidade) 
	{
		String sql =
		"SELECT " 
		+ "d.codigo as 'depCodigo', "
		+ "d.descricao as 'depDescricao',"
		+ "g.codigo as 'gruCodigo', "
		+ "g.descricao as 'gruDescricao', "
		+ "s.codigo as 'subCodigo', "
		+ "s.descricao as 'subDescricao' "
		
		+ "FROM "
		+ "departamento d, "
		+ "produto p, "
		+ "grupo g, "
		+ "sub_grupo s "
		
		+ "WHERE "
		+ "p.departamento_codigo = d.codigo AND "
		+ "p.subgrupo_codigo = s.codigo		AND "
		+ "s.grupo_codigo = g.codigo		AND "
		+ "p.ativo = 1						AND "
		+ "d.ativo = 1 "
		
		+ "GROUP BY "
		+ "d.codigo, "
		+ "d.descricao,"
		+ "g.codigo,"
		+ "g.descricao, "
		+ "s.codigo,"
		+ "s.descricao "
		
		+ "ORDER BY "
		+ "1,2,3,4,5,6";
		
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setResultTransformer(new AliasToBeanResultTransformer(MenuPorDepartamento.class));
		
		List<MenuPorDepartamento> lista = query.list();
		return lista;
	}
}
