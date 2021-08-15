package br.com.gabrielferreira.repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.gabrielferreira.entidade.Perfil;
public class PerfilRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public PerfilRepositorio() {}
	
	public List<Perfil> listaPerfis() {
		String jpql = "SELECT p FROM Perfil p";
		TypedQuery<Perfil> query = entityManager.createQuery(jpql,Perfil.class);
		
		List<Perfil> perfis = query.getResultList();
		return perfis;
	}
	
	public Perfil consultarPorId(Integer id) {
		return entityManager.find(Perfil.class, id);
	}

}
