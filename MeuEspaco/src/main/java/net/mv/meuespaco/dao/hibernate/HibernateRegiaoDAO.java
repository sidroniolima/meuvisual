package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;

import javax.ejb.Stateless;

import net.mv.meuespaco.dao.RegiaoDAO;
import net.mv.meuespaco.model.Regiao;

/**
 * Implementação estendida da camada DAO da Entidade Região. 
 * 
 * @author Sidronio
 * 05/11/2015
 */
@Stateless
public class HibernateRegiaoDAO extends HibernateGenericDAO<Regiao, Long> implements RegiaoDAO, Serializable{

	private static final long serialVersionUID = 4040242637239513034L;

}
