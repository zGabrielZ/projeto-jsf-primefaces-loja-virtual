package br.com.gabrielferreira.repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

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
	
	public List<Saldo> getSaldosByCliente(Integer idCliente) {
		String jpql = "SELECT s FROM Saldo s JOIN FETCH s.cliente c where c.id = :idCliente";
		TypedQuery<Saldo> query = entityManager.createQuery(jpql,Saldo.class);
		query.setParameter("idCliente", idCliente);
		
		List<Saldo> saldos = query.getResultList();
		return saldos;
	}
	
	public void remover(Saldo saldo) {
		saldo = procurarPorId(saldo.getId());
		entityManager.remove(saldo);
	}
	
	public Saldo procurarPorId(Integer id) {
		return entityManager.find(Saldo.class, id);
	}

}
