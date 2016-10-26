package net.mv.meuespaco.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.DepartamentoDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.service.DepartamentoService;

@Stateless
public class DepartamentoServiceImpl extends SimpleServiceLayerImpl<Departamento, Long> implements DepartamentoService {

	@Inject
	private DepartamentoDAO depDAO;
	
	@Override
	public void validaInsercao(Departamento dep) throws RegraDeNegocioException {
		dep.valida();
	}

	@Override
	public void validaExclusao(Departamento dep) throws RegraDeNegocioException {
		dep.valida();
	}

	@Override
	public GenericDAO getDAO() {
		return depDAO;
	}

}
