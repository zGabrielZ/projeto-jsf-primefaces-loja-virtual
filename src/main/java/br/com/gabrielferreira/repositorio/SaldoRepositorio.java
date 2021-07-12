package br.com.gabrielferreira.repositorio;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import br.com.gabrielferreira.entidade.Saldo;

public class SaldoRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public SaldoRepositorio() {}
	
	public void inserir(Saldo saldo) {
		entityManager.persist(saldo);
	}
	

}
