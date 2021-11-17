package br.com.gabrielferreira.utils;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class JPAHibernateUtil implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EntityManagerFactory emf;
	
	public JPAHibernateUtil() {
		emf = Persistence.createEntityManagerFactory("Projeto-Loja");
	}
	
	// Produzir esse entitymanager pra nao ser chamado manualmente
	// Prover a parte de persistencia
	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}


}
