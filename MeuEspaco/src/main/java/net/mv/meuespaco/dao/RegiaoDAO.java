package net.mv.meuespaco.dao;

import net.mv.meuespaco.model.Regiao;

/**
 * Interface DAO da Entidade Regiao.
 * 
 * @author Sidronio
 * 05/11/2015
 */
public interface RegiaoDAO extends GenericDAO<Regiao, Long>{

	/**
	 * Busca uma região pelo código interno.
	 * 
	 * @param codigoInterno pesquisa.
	 * @return região.
	 */ 
	Regiao buscarPeloCodigoInterno(String codigoInterno);

}
