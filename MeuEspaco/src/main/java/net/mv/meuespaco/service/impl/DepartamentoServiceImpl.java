package net.mv.meuespaco.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.DepartamentoDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.service.DepartamentoService;

/**
 * Implementação da camada Service da entidade Departamento.
 * 
 * @author sidronio
 *
 */
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
	public GenericDAO<Departamento, Long> getDAO() {
		return depDAO;
	}

	@Override
	public List<Departamento> listaAtivos() 
	{
		Departamento dep = new Departamento();
		dep.setAtivo(true);
		return depDAO.filtrar(dep, Arrays.asList("ativo"), null, Arrays.asList("codigo"), null);
	}

}
