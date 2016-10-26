package net.mv.meuespaco.dao.hibernate;

import javax.ejb.Stateless;

import net.mv.meuespaco.dao.FamiliaDAO;
import net.mv.meuespaco.model.Familia;

/**
 * Implementação da interface DAO da entidade Familia.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Stateless
public class HibernateFamiliaDAO extends HibernateGenericDAO<Familia, Long> implements FamiliaDAO {

}
