package br.com.gabrielferreira.repositorio.generico;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Named
public class RepositorioGenerico<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public void inserir(T entidade) {
		entityManager.getTransaction().begin();
		entityManager.persist(entidade);
		entityManager.getTransaction().commit();
	}
	
	public T atualizar(T entidade) {
		entityManager.getTransaction().begin();
		T novaEntidade = entityManager.merge(entidade);
		entityManager.getTransaction().commit();
		return novaEntidade;
	}
	
	public T pesquisarPorId(Integer id, Class<T> clazz) {
		return entityManager.find(clazz, id);
	}
	
	public void deletarPorId(Class<T> clazz, Integer id) {
		entityManager.getTransaction().begin();
		T entidade = pesquisarPorId(id, clazz);
		entityManager.remove(entidade);
		entityManager.getTransaction().commit();
	}
	
	public List<T> listagem(Class<T> clazz){
		String jpql = "SELECT t FROM " + clazz.getSimpleName() + " t order by t.id desc";
		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
		List<T> resultados = query.getResultList();
		return resultados;
	}
	
	public T verificarNulo(TypedQuery<T> query) {
		try {
			T entidade = query.getSingleResult();
			return entidade;
		} catch (NoResultException e) {
			return null;
		}
	}

}
