package net.mv.meuespaco.dao.hibernate;

import java.io.Serializable;

import javax.ejb.Stateless;

import net.mv.meuespaco.dao.AlmoxarifadoDAO;
import net.mv.meuespaco.model.estoque.Almoxarifado;

/**
 * Implementação da DAO para Almoxarifado.
 * 
 * @author Sidronio
 * 15/12/2015
 */
@Stateless
public class HibernateAlmoxarifadoDAO extends HibernateGenericDAO<Almoxarifado, Long> implements AlmoxarifadoDAO, Serializable{

	private static final long serialVersionUID = 5979101465666478468L;

}
