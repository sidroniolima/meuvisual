package net.mv.meuespaco.service.impl;

import java.util.List;

import javax.inject.Inject;

import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.GradeDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.service.GradeService;

/**
 * Implementação da camada Service da entidade Grade.
 * 
 * @author Sidronio
 * 21/12/2015
 */
public class GradeServiceImpl extends SimpleServiceLayerImpl<Grade, Long> implements GradeService {

	@Inject
	private GradeDAO gradeDAO;
	
	@Override
	public void validaInsercao(Grade entidade) throws RegraDeNegocioException {
		
	}

	@Override
	public void validaExclusao(Grade entidade) throws RegraDeNegocioException {
		
	}
	
	@Override
	public List<Grade> buscaGradesPorProduto(Produto produto) {
		return this.gradeDAO.buscarGradesPorProduto(produto);
	}
	
	@Override
	public void alteraGradeDoProduto(Produto produto, TipoGrade novoTipo) throws RegraDeNegocioException {
		if (this.gradeDAO.alteraGradeDoProduto(produto, novoTipo) == 0) {
			throw new RegraDeNegocioException("Não foi possível alterar a grade do produto.");
		}
	}

	@Override
	public GenericDAO getDAO() {
		return this.gradeDAO;
	}

}
