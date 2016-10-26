package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.util.Arrays;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.RegiaoDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.service.RegiaoService;

/**
 * Implementação da camada Service para a entidade Região.
 * 
 * @author Sidronio
 * 12/11/2015
 */
@Stateless
public class RegiaoServiceImpl extends SimpleServiceLayerImpl<Regiao, Long> implements RegiaoService, Serializable{

	private static final long serialVersionUID = -1901196889106986905L;
	
	@Inject
	private RegiaoDAO regiaoDAO;
	
	@Override
	public void validaInsercao(Regiao regiao) throws RegraDeNegocioException {
		regiao.valida();
	}

	@Override
	public void validaExclusao(Regiao regiao) throws RegraDeNegocioException {
		
	}

	@Override
	public GenericDAO getDAO() {
		return regiaoDAO;
	}

	@Override
	public Regiao buscarPeloCodigoComSemana(Long codigo) {
		
		return regiaoDAO.buscarPeloCodigoComRelacionamento(
				codigo,
				Arrays.asList("semana"));
	}

}
