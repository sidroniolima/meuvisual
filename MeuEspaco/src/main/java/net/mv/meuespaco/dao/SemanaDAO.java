package net.mv.meuespaco.dao;

import net.mv.meuespaco.model.Semana;

/**
 * Interface de acesso DAO para a entidade Semana.
 * 
 * @author Sidronio
 * 22/05/2015
 */
public interface SemanaDAO extends GenericDAO<Semana, Long>{

	/**
	 * Fornece a quantidade de regiões que estão na semana 
	 * pesquisada.
	 * 
	 * @param semana
	 * @return
	 */
	public Long buscarQtdDeRegioesDaSemana(Semana semana);
	
}
