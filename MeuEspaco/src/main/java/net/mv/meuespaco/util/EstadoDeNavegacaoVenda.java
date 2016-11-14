package net.mv.meuespaco.util;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import net.mv.meuespaco.annotations.EstadoDeNavegacaoVendaAnnotation;

/**
 * Instância do estado de navegação para Venda.
 * 
 * @author sidronio
 * @created 14/11/2016
 */
@SessionScoped
@EstadoDeNavegacaoVendaAnnotation
public class EstadoDeNavegacaoVenda extends EstadoDeNavegacao implements Serializable 
{
	private static final long serialVersionUID = -8145152987723997595L;

}
