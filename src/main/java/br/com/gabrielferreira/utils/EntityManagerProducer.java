package br.com.gabrielferreira.utils;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerProducer {

	private EntityManagerFactory entityManagerFactory;

	public EntityManagerProducer() {
		this.entityManagerFactory = Persistence.createEntityManagerFactory("Projeto-Loja");
	}
	
	@Produces
	@RequestScoped
	public EntityManager createEntityManager() {
		return this.entityManagerFactory.createEntityManager();
	}
	
	public void closeEntityManager(@Disposes EntityManager entityManager) {
		entityManager.close();
	}
}
