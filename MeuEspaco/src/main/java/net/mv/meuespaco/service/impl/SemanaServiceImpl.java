package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.time.LocalDate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.SemanaDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.service.SemanaService;

@Stateless
public class SemanaServiceImpl extends SimpleServiceLayerImpl<Semana, Long> implements Serializable, SemanaService {

	private static final long serialVersionUID = -1229158073565377063L;

	@Inject
	private SemanaDAO semanaDAO;
	
	@Override
	public void validaInsercao(Semana semana) throws RegraDeNegocioException {
		
		semana.valida();
	}

	@Override
	public void validaExclusao(Semana semana) throws RegraDeNegocioException {
		
		if (this.semanaTemRegiao(semana)) {
			throw new RegraDeNegocioException("A semana possui regiões.");
		}
	}
	
	@Override
	public GenericDAO getDAO() {
		return semanaDAO;
	}

	@Override
	public boolean semanaTemRegiao(Semana semana) {
		return this.qtdDeRegioesDaSemana(semana) > 0;
	}
	
	@Override
	public Long qtdDeRegioesDaSemana(Semana semana) {
		return semanaDAO.buscarQtdDeRegioesDaSemana(semana);
	}

}
