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

import br.com.gabrielferreira.entidade.Cliente;
import br.com.gabrielferreira.entidade.search.ClienteSearch;
public class ClienteRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public List<Cliente> filtrar(ClienteSearch clienteSearch){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
		Root<Cliente> root = criteriaQuery.from(Cliente.class);
		
		List<Predicate> predicatesFiltros = criarFiltroCliente(clienteSearch, criteriaBuilder, root);
		
		criteriaQuery.where((Predicate[])predicatesFiltros.toArray(new Predicate[0]));
		
		TypedQuery<Cliente> typedQuery = entityManager.createQuery(criteriaQuery);

		List<Cliente> clientes = typedQuery.getResultList();
		return clientes;
	}
	
	private List<Predicate> criarFiltroCliente(ClienteSearch clienteSearch, CriteriaBuilder criteriaBuilder
			, Root<Cliente> root){
		
		List<Predicate> predicates = new ArrayList<>();
		
		if(StringUtils.isNotBlank(clienteSearch.getNome())) {
			Predicate predicateNome = criteriaBuilder.like(root.get("nome"), "%" + clienteSearch.getNome() + "%");
			predicates.add(predicateNome);
		}
		
		if(StringUtils.isNotBlank(clienteSearch.getCpf())) {
			Predicate predicateCpf = criteriaBuilder.like(root.get("cpf"), "%" + clienteSearch.getCpf() + "%");
			predicates.add(predicateCpf);
		}
		
		if(StringUtils.isNotBlank(clienteSearch.getEmail())) {
			Predicate predicateEmail = criteriaBuilder.like(root.get("email"), "%" + clienteSearch.getEmail() + "%");
			predicates.add(predicateEmail);
		}
			
		return predicates;
	}
	
	public ClienteRepositorio() {}
	
	public void inserir(Cliente cliente) {
		entityManager.persist(cliente);
	}
	
	public void remover(Cliente cliente) {
		cliente = procurarPorId(cliente.getId());
		entityManager.remove(cliente);
	}
	
	public void atualizar(Cliente cliente) {
		entityManager.merge(cliente);
	}
	
	public Cliente procurarPorId(Integer id) {
		return entityManager.find(Cliente.class, id);
	}
	
	public boolean verificarCpf(String cpf) {
		String jpql = "SELECT c FROM Cliente c where c.cpf = :cpf";
		TypedQuery<Cliente> query = entityManager.createQuery(jpql,Cliente.class);
		query.setParameter("cpf", cpf);
		
		List<Cliente> clientes = query.getResultList();
		
		return !clientes.isEmpty()?true:false;
	}
	
	public boolean verificarEmail(String email) {
		String jpql = "SELECT c FROM Cliente c where c.email = :email";
		TypedQuery<Cliente> query = entityManager.createQuery(jpql,Cliente.class);
		query.setParameter("email", email);
		
		List<Cliente> clientes = query.getResultList();
		
		return !clientes.isEmpty()?true:false;
	}
	

}
