package br.com.gabrielferreira.repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.repositorio.generico.RepositorioGenerico;
public class SaldoRepositorio extends RepositorioGenerico<Saldo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public TypedQuery<Saldo> getListagem(Integer idUsuario){
		String jpql = "SELECT s FROM Saldo s JOIN FETCH s.usuario u where u.id = :idUsuario order by s.id desc";
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

}
