package net.mv.meuespaco.service;

import net.mv.meuespaco.model.Semana;

/**
 * Inteface da camada Service para a entidade Semana 
 * 
 * @author Sidronio
 * 12/11/2015
 */
public interface SemanaService extends SimpleServiceLayer<Semana, Long>{

	/**
	 * Verifica se uma semana tem alguma região.
	 * 
	 * @param semana
	 * @return
	 */
	public boolean semanaTemRegiao(Semana semana);
	
	/**
	 * Informa a quantidade de regiões de uma semana.
	 * 
	 * @param semana
	 * @return
	 */
	public Long qtdDeRegioesDaSemana(Semana semana);
	
}
