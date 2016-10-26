package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Abstração da camada Dao da entidade Grade.
 * 
 * @author Sidronio
 * 21/12/2015
 */
public interface GradeDAO extends GenericDAO<Grade, Long> {
	
	/**
	 * Busca as grades de um produto.
	 * 
	 * @param produto Produto da pesquisa.
	 * @return Lista de grades do produto.
	 */
	public List<Grade> buscarGradesPorProduto(Produto produto);
	
	/**
	 * Altera a grade do produto via Update por Sql.
	 * @param produto produto para alteração.
	 * @param novoTipo novo tipo de grade.
	 */
	int alteraGradeDoProduto(Produto produto, TipoGrade novoTipo);
}
