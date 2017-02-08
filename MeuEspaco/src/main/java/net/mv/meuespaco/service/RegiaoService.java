package net.mv.meuespaco.service;

import net.mv.meuespaco.model.Regiao;

/**
 * Interface da camada Service para a entidade Região.
 * 
 * @author Sidronio
 * 10/11/2015
 */
public interface RegiaoService extends SimpleServiceLayer<Regiao, Long>{

	/**
	 * Retorna uma instância de região relacionado 
	 * a sua semana.
	 * 
	 * @param paramCodigo
	 * @return
	 */
	public Regiao buscarPeloCodigoComSemana(Long paramCodigo);

	/**
	 * Retorna uma região pelo código interno.
	 * 
	 * @param string pesquisa
	 * @return região.
	 */
	public Regiao buscaPeloCodigoInterno(String string);

}
