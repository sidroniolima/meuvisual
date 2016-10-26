package net.mv.meuespaco.service.impl;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.AlmoxarifadoDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.estoque.Almoxarifado;
import net.mv.meuespaco.service.AlmoxarifadoService;

/**
 * Implementação da camada Service de Almoxarifado.
 * 
 * @author Sidronio
 * 15/12/2015
 */
@Stateless
public class AlmoxarifadoServiceImpl extends SimpleServiceLayerImpl<Almoxarifado, Long> implements AlmoxarifadoService, Serializable{

	private static final long serialVersionUID = -8762391085147598313L;
	
	@Inject
	private AlmoxarifadoDAO almDAO;

	@Override
	public void validaInsercao(Almoxarifado alm) throws RegraDeNegocioException {
		
	}

	@Override
	public void validaExclusao(Almoxarifado alm) throws RegraDeNegocioException {
		
		if (alm.getCodigo() == 1L) {
			throw new RegraDeNegocioException("Não é possível excluir o almoxarifado principal.");
		}
	}

	@Override
	public GenericDAO getDAO() {
		return almDAO;
	}
	
	@Override
	public Almoxarifado almoxarifadoPrincipal() {
		return this.almDAO.buscarPeloCodigo(1L);
	}

}
