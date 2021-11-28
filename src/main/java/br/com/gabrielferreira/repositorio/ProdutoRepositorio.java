package br.com.gabrielferreira.repositorio;

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
import br.com.gabrielferreira.repositorio.generico.AbstractConsultaRepositorio;
public class ProdutoRepositorio extends AbstractConsultaRepositorio<Produto>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	@Override
	public TypedQuery<Produto> getListagem(Produto search) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Produto> criteriaQuery = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> root = criteriaQuery.from(Produto.class);
		
		Join<Produto, Categoria> categoriaJoin = root.join("categoria");
		categoriaJoin.alias("c");
		
		List<Predicate> predicatesFiltros = criarFiltroProduto(search, criteriaBuilder, root);
		
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
		criteriaQuery.where((Predicate[])predicatesFiltros.toArray(new Predicate[0]));
		
		TypedQuery<Produto> typedQuery = entityManager.createQuery(criteriaQuery);
		
		return typedQuery;
	}

	@Override
	public List<Produto> filtrar(Produto search, int primeiroResultado, int quantidadeMaxima) {
		TypedQuery<Produto> typedQuery = getListagem(search);
		List<Produto> produtos = typedQuery.setFirstResult(primeiroResultado).setMaxResults(quantidadeMaxima).getResultList();
		return produtos;
	}

	@Override
	public Integer quantidadeRegistro(Produto search) {
		TypedQuery<Produto> typedQuery = getListagem(search);
		List<Produto> produtos = typedQuery.getResultList();
		return produtos.size();
	}
	
	private List<Predicate> criarFiltroProduto(Produto search, CriteriaBuilder criteriaBuilder
			, Root<Produto> root){
		
		List<Predicate> predicates = new ArrayList<>();
		
		ProdutoSearch produtoSearch = (ProdutoSearch) search;
		
		if(produtoSearch != null) {
			if(StringUtils.isNotBlank(produtoSearch.getNome())) {
				Predicate predicateNome = criteriaBuilder.like(root.get("nome"), "%" + produtoSearch.getNome() + "%");
				predicates.add(predicateNome);
			}
			
			if(produtoSearch.getDataProducao() != null) {
				Predicate predicateDataProducao = criteriaBuilder.greaterThanOrEqualTo(root.get("dataProducao"), produtoSearch.getDataProducao());
				predicates.add(predicateDataProducao);
			}
			
			if(produtoSearch.getEstoque() != null) {
				Predicate predicateEstoque = criteriaBuilder.greaterThanOrEqualTo(root.get("estoque"),produtoSearch.getEstoque());
				predicates.add(predicateEstoque);
			}
		}
			
		return predicates;
	}
	
	public List<Produto> getProdutos() {
		String jpql = "SELECT p FROM Produto p JOIN FETCH p.categoria c";
		TypedQuery<Produto> query = entityManager.createQuery(jpql,Produto.class);
		
		List<Produto> produtos = query.getResultList();
		return produtos;
	}
	
	public List<Produto> procurarPorIdCategoria(Integer idCategoria) {
		String jpql = "SELECT p FROM Produto p JOIN FETCH p.categoria c where c.id = :idCategoria";
		TypedQuery<Produto> query = entityManager.createQuery(jpql,Produto.class);
		query.setParameter("idCategoria", idCategoria);
		
		List<Produto> produtos = query.getResultList();
		return produtos;
	}
	
	public boolean verificarNomeProduto(String nome) {
		String jpql = "SELECT p FROM Produto p where p.nome = :nome";
		TypedQuery<Produto> query = entityManager.createQuery(jpql,Produto.class);
		query.setParameter("nome", nome);
		
		List<Produto> produtos = query.getResultList();
		
		return !produtos.isEmpty() ? true : false;
	}
	
	public boolean verificarNomeProdutoAtualizado(String nome, Integer id) {
		String jpql = "SELECT p FROM Produto p where p.nome = :nome and p.id <> :id";
		TypedQuery<Produto> query = entityManager.createQuery(jpql,Produto.class);
		query.setParameter("nome", nome);
		query.setParameter("id", id);
		
		List<Produto> produtos = query.getResultList();
		
		return !produtos.isEmpty() ? true : false;
	}
	

}
