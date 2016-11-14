package net.mv.meuespaco.util;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import net.mv.meuespaco.annotations.EstadoDeNavegacaoConsignadoAnnotation;

/**
 * Instância do estado de navegação para Consignado.
 * 
 * @author sidronio
 * @created 14/11/2016
 */
@SessionScoped
@EstadoDeNavegacaoConsignadoAnnotation
public class EstadoDeNavegacaoConsignado extends EstadoDeNavegacao implements Serializable {

	private static final long serialVersionUID = -914586002393551452L;

}
