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
	
	public void atualizar(Saldo saldo) {
		entityManager.merge(saldo);
	}
	
	public TypedQuery<Saldo> getListagem(Integer idUsuario){
		String jpql = "SELECT s FROM Saldo s JOIN FETCH s.usuario u where u.id = :idUsuario";
		TypedQuery<Saldo> query = entityManager.createQuery(jpql,Saldo.class);
		query.setParameter("idUsuario", idUsuario);
		return query;
	}
	
	public List<Saldo> getSaldosByUsuario(Integer idUsuario,int primeiroResultado, int quantidadeMaxima) {
		TypedQuery<Saldo> query = getListagem(idUsuario);
		List<Saldo> saldos = query.setFirstResult(primeiroResultado).setMaxResults(quantidadeMaxima).getResultList();
		return saldos;
	}
	
	public Integer quantidadeRegistro(Integer idUsuario) {
		TypedQuery<Saldo> typedQuery = getListagem(idUsuario);
		List<Saldo> saldos = typedQuery.getResultList();
		return saldos.size();
	}
	
	public void remover(Saldo saldo) {
		saldo = procurarPorId(saldo.getId());
		entityManager.remove(saldo);
	}
	
	public Saldo procurarPorId(Integer id) {
		return entityManager.find(Saldo.class, id);
	}

}
