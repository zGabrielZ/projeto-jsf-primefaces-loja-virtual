package br.com.gabrielferreira.repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gabrielferreira.entidade.Itens;
public class ItensRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public ItensRepositorio() {}
	
	public void inserir(Itens itens) {
		entityManager.persist(itens);
	}
	public void inserirItens(List<Itens> itens) {
		for(Itens item : itens) {
			entityManager.persist(item);
		}
	}
	
	public Itens procurarPorId(Integer id) {
		return entityManager.find(Itens.class, id);
	}

}
