package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;

import javax.ejb.Stateless;

import net.mv.meuespaco.dao.DocumentoDAO;
import net.mv.meuespaco.model.loja.Documento;

/**
 * Implementação da camada DAO para a entidade Documento.
 * 
 * @author Sidronio
 * 22/12/2015
 */
@Stateless
public class HibernateDocumentoDAO extends HibernateGenericDAO<Documento, String> implements DocumentoDAO, Serializable {

	private static final long serialVersionUID = 7866020642493643639L;

}
