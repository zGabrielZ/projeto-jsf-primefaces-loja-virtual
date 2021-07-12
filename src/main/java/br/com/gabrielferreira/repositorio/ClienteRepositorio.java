package br.com.gabrielferreira.repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.com.gabrielferreira.entidade.Cliente;

public class ClienteRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
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
