package br.com.gabrielferreira.repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.entidade.search.ProdutoSearch;
public class ProdutoRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public List<Produto> filtrar(ProdutoSearch produtoSearch){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		Join<Produto, Categoria> categoriaJoin = root.join("categoria");
		categoriaJoin.alias("c");
		
		List<Predicate> predicatesFiltros = criarFiltroProduto(produtoSearch, criteriaBuilder, root);
		
		criteriaQuery.where((Predicate[])predicatesFiltros.toArray(new Predicate[0]));
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Produto> produtos = typedQuery.getResultList();
		return produtos;
	}
	
	private List<Predicate> criarFiltroProduto(ProdutoSearch produtoSearch, CriteriaBuilder criteriaBuilder
			, Root<Produto> root){
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(StringUtils.isNotBlank(produtoSearch.getNome())) {
			Predicate predicateNome = criteriaBuilder.like(root.get("nome"), "%" + produtoSearch.getNome() + "%");
			predicates.add(predicateNome);
		}
		
		if(produtoSearch.getDataProducao() != null) {
			Predicate predicateDataProducao = criteriaBuilder.equal(root.get("dataProducao"), produtoSearch.getDataProducao());
			predicates.add(predicateDataProducao);
		}
		
		if(produtoSearch.getEstoque() != null) {
			Predicate predicateEstoque = criteriaBuilder.equal(root.get("estoque"),produtoSearch.getEstoque());
			predicates.add(predicateEstoque);
		}
			
		return predicates;
	}
	
	public ProdutoRepositorio() {}
	
	public void inserir(Produto produto) {
		entityManager.persist(produto);
	}
	
	public void remover(Produto produto) {
		produto = procurarPorId(produto.getId());
		entityManager.remove(produto);
	}
	
	public void atualizar(Produto produto) {
		entityManager.merge(produto);
	}
	
	public Produto procurarPorId(Integer id) {
		return entityManager.find(Produto.class, id);
	}
	
	public boolean verificarNomeProduto(String nome) {
		String jpql = "SELECT p FROM Produto p where p.nome = :nome";
		TypedQuery<Produto> query = entityManager.createQuery(jpql,Produto.class);
		query.setParameter("nome", nome);
		
		List<Produto> produtos = query.getResultList();
		
		return !produtos.isEmpty()?true:false;
	}
	
	public boolean verificarNomeProdutoAtualizado(String nome, Integer id) {
		String jpql = "SELECT p FROM Produto p where p.nome = :nome and p.id <> :id";
		TypedQuery<Produto> query = entityManager.createQuery(jpql,Produto.class);
		query.setParameter("nome", nome);
		query.setParameter("id", id);
		
		List<Produto> produtos = query.getResultList();
		
		return !produtos.isEmpty()?true:false;
	}
	

}
