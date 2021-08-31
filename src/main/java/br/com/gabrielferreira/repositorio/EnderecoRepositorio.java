package br.com.gabrielferreira.repositorio;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.gabrielferreira.entidade.Endereco;
public class EnderecoRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public EnderecoRepositorio() {}
	
	public void inserir(Endereco endereco) {
		entityManager.persist(endereco);
	}
	
	public void atualizar(Endereco endereco) {
		entityManager.merge(endereco);
	}
	public void remover(Endereco endereco) {
		endereco = procurarPorId(endereco.getId());
		entityManager.remove(endereco);
	}
	
	public Endereco procurarPorId(Integer id) {
		return entityManager.find(Endereco.class, id);
	}
	
	public Endereco procurarPorIdByUsuario(Integer idUsuario) {
		String jpql = "SELECT e FROM Endereco e join e.usuario u where u.id = :idUsuario";
		TypedQuery<Endereco> query = entityManager.createQuery(jpql,Endereco.class);
		query.setParameter("idUsuario", idUsuario);
		
		try {
			Endereco endereco = query.getSingleResult();
			return endereco;
		} catch (NoResultException e) {
			return null;
		}
	}

}
