package net.mv.meuespaco.service;

import java.util.List;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Abstração da camada Service da entidade Grade.
 * 
 * @author Sidronio
 * 21/12/2015
 */
public interface GradeService extends SimpleServiceLayer<Grade, Long>{

	/**
	 * Busca as grades de um produto.
	 * 
	 * @param produto Produto da pesquisa.
	 * @return Lista de grades do produto.
	 */
	public List<Grade> buscaGradesPorProduto(Produto produto);

	/**
	 * Requisita a alteração no DAO do tipo de grade do produto.
	 * 
	 * @param produto para alteração.
	 * @param novoTipo para alteração.
	 * @throws RegraDeNegocioException caso não seja efetuada o Update.
	 */
	public void alteraGradeDoProduto(Produto produto, TipoGrade novoTipo) throws RegraDeNegocioException;
	
}
