package net.mv.meuespaco.service.impl;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.FamiliaDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Familia;
import net.mv.meuespaco.service.FamiliaService;

/**
 * Implementação da camada Service da entidade Familia.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class FamiliaServiceImpl extends SimpleServiceLayerImpl<Familia, Long> implements FamiliaService, Serializable {

	private static final long serialVersionUID = 5551285081960469845L;
	
	@Inject
	private FamiliaDAO familiaDAO;
	
	@Override
	public void validaInsercao(Familia familia) throws RegraDeNegocioException {
		
		familia.valida();
	}

	@Override
	public void validaExclusao(Familia familia) throws RegraDeNegocioException {
		// TODO Auto-generated method stub
	}

	@Override
	public GenericDAO getDAO() {
		return familiaDAO;
	}

}
