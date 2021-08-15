package br.com.gabrielferreira.repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.search.CategoriaSearch;

public class CategoriaRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public CategoriaRepositorio() {}
	
	public List<Categoria> filtrar(CategoriaSearch categoriaSearch){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Categoria> criteriaQuery = criteriaBuilder.createQuery(Categoria.class);
		Root<Categoria> root = criteriaQuery.from(Categoria.class);
		
		List<Predicate> predicatesFiltros = criarFiltroCategoria(categoriaSearch, criteriaBuilder, root);
		
		criteriaQuery.where((Predicate[])predicatesFiltros.toArray(new Predicate[0]));
		
		TypedQuery<Categoria> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Categoria> categorias = typedQuery.getResultList();
		return categorias;
	}
	
	private List<Predicate> criarFiltroCategoria(CategoriaSearch categoriaSearch, CriteriaBuilder criteriaBuilder
			, Root<Categoria> root){
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(StringUtils.isNotBlank(categoriaSearch.getNome())) {
			Predicate predicateNome = criteriaBuilder.like(root.get("nome"), "%" + categoriaSearch.getNome() + "%");
			predicates.add(predicateNome);
		}
			
		return predicates;
	}
	
	public void inserir(Categoria categoria) {
		entityManager.persist(categoria);
	}
	
	public void atualizar(Categoria categoria) {
		entityManager.merge(categoria);
	}
	
	public Categoria procurarPorIdCategoria(Integer id) {
		return entityManager.find(Categoria.class, id);
	}
	
	public List<Categoria> procurarPorId(Integer id) {
		String jpql = "SELECT c FROM Categoria c JOIN FETCH c.produtos p where c.id = :id";
		TypedQuery<Categoria> query = entityManager.createQuery(jpql,Categoria.class);
		query.setParameter("id", id);
		
		List<Categoria> categorias = query.getResultList();
		return categorias;
	}
	
	public List<Categoria> listaCategorias(){
		String jpql = "SELECT c FROM Categoria c";
		TypedQuery<Categoria> query = entityManager.createQuery(jpql,Categoria.class);
		List<Categoria> categorias = query.getResultList();
		return categorias;
	}
	
	public boolean verificarNomeCategoria(String nome) {
		String jpql = "SELECT c FROM Categoria c where c.nome = :nome";
		TypedQuery<Categoria> query = entityManager.createQuery(jpql,Categoria.class);
		query.setParameter("nome", nome);
		
		List<Categoria> categorias = query.getResultList();
		
		return !categorias.isEmpty()?true:false;
	}
	
	public boolean verificarNomeCategoriaAtualizado(String nome, Integer id) {
		String jpql = "SELECT c FROM Categoria c where c.nome = :nome and c.id <> :id";
		TypedQuery<Categoria> query = entityManager.createQuery(jpql,Categoria.class);
		query.setParameter("nome", nome);
		query.setParameter("id", id);
		
		List<Categoria> categorias = query.getResultList();
		
		return !categorias.isEmpty()?true:false;
	}

}
