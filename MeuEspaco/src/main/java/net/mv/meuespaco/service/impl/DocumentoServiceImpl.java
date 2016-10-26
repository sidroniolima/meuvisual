package net.mv.meuespaco.service.impl;

import java.io.Serializable;

import javax.inject.Inject;

import net.mv.meuespaco.dao.DocumentoDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Documento;
import net.mv.meuespaco.service.DocumentoService;

/**
 * Implementação da camada Service para a entidade Documento.
 * 
 * @author Sidronio
 * 22/12/2015
 */
public class DocumentoServiceImpl extends SimpleServiceLayerImpl<Documento, String> implements DocumentoService, Serializable {

	private static final long serialVersionUID = 5009655595619116267L;
	
	@Inject
	private DocumentoDAO documentoDAO;
	
	@Override
	public void validaInsercao(Documento doc) throws RegraDeNegocioException {
		doc.valida();
	}

	@Override
	public void validaExclusao(Documento doc) throws RegraDeNegocioException {
		
	}

	@Override
	public GenericDAO getDAO() {
		return documentoDAO;
	}

}
