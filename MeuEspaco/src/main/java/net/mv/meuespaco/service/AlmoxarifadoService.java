package net.mv.meuespaco.service;

import net.mv.meuespaco.model.estoque.Almoxarifado;

/**
 * Interface da camada Service de Almoxarifado.
 * 
 * @author Sidronio
 * 15/12/2015
 */
public interface AlmoxarifadoService extends SimpleServiceLayer<Almoxarifado, Long> {

	/**
	 * Retorna o almoxarifado principal.
	 * 
	 * @return Almoxarifado.
	 */
	public Almoxarifado almoxarifadoPrincipal();
	
}
