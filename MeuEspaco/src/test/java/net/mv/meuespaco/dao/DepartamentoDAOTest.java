package net.mv.meuespaco.dao;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DepartamentoDAOTest 
{
	protected static EntityManagerFactory emf;
	protected static EntityManager em;
 
	@BeforeClass
	public static void createEntityManagerFactory() {
	   
		try 
		{
			emf = Persistence.createEntityManagerFactory("meuespaco-persistence-unit-test");
			System.out.println("EMf " + emf);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@AfterClass
	public static void closeEntityManagerFactory() {
		 emf.close();
	}
	
	@Before
	public void beginTransaction() 
	{
		try
		{
		    em = emf.createEntityManager();
		    em.getTransaction().begin();
		} catch (Exception e) 
		{
			System.out.println(e);
		}
	}
	
	@After
	public void rollbackTransaction() {   
	    if (em.getTransaction().isActive()) {
	        em.getTransaction().rollback();
	    }
	
	    if (em.isOpen()) {
	        em.close();
	    }
	}
	
	@Test
	public void deveCriarOEntityManager()
	{
		System.out.println(em);
		assertTrue("Em is not null", this.em != null);
	}
}
