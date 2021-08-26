package br.com.gabrielferreira.repositorio;
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

import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.search.UsuarioSearch;
public class UsuarioRepositorio extends AbstractConsultaRepositorio<Usuario>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
		
	@Override
	public TypedQuery<Usuario> getListagem(Usuario search) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		
		List<Predicate> predicatesFiltros = criarFiltroUsuario(search, criteriaBuilder, root);
		
		criteriaQuery.where((Predicate[])predicatesFiltros.toArray(new Predicate[0]));
		
		TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
		return typedQuery;
	}

	@Override
	public List<Usuario> filtrar(Usuario search, int primeiroResultado, int quantidadeMaxima) {
		TypedQuery<Usuario> typedQuery = getListagem(search);
		List<Usuario> usuarios = typedQuery.setFirstResult(primeiroResultado).setMaxResults(quantidadeMaxima).getResultList();
		return usuarios;
	}

	@Override
	public Integer quantidadeRegistro(Usuario search) {
		TypedQuery<Usuario> typedQuery = getListagem(search);
		List<Usuario> usuarios = typedQuery.getResultList();
		return usuarios.size();
	}
	
	
	private List<Predicate> criarFiltroUsuario(Usuario search, CriteriaBuilder criteriaBuilder
			, Root<Usuario> root){
		
		List<Predicate> predicates = new ArrayList<>();
		
		UsuarioSearch usuarioSearch = (UsuarioSearch) search;
		
		if(StringUtils.isNotBlank(usuarioSearch.getNome())) {
			Predicate predicateNome = criteriaBuilder.like(root.get("nome"), "%" + usuarioSearch.getNome() + "%");
			predicates.add(predicateNome);
		}
		
		if(StringUtils.isNotBlank(usuarioSearch.getCpf())) {
			Predicate predicateCpf = criteriaBuilder.like(root.get("cpf"), "%" + usuarioSearch.getCpf() + "%");
			predicates.add(predicateCpf);
		}
		
		if(StringUtils.isNotBlank(usuarioSearch.getEmail())) {
			Predicate predicateEmail = criteriaBuilder.like(root.get("email"), "%" + usuarioSearch.getEmail() + "%");
			predicates.add(predicateEmail);
		}
			
		return predicates;
	}
	
	public UsuarioRepositorio() {}
	
	public void inserir(Usuario usuario) {
		entityManager.persist(usuario);
	}
	
	public void remover(Usuario usuario) {
		usuario = procurarPorId(usuario.getId());
		entityManager.remove(usuario);
	}
	
	public void atualizar(Usuario usuario) {
		entityManager.merge(usuario);
	}
	
	public Usuario procurarPorId(Integer id) {
		return entityManager.find(Usuario.class, id);
	}
	
	public Usuario procurarPorEmail(String email) {
		String jpql = "SELECT u FROM Usuario u where u.email = :email";
		TypedQuery<Usuario> query = entityManager.createQuery(jpql,Usuario.class);
		query.setParameter("email", email);
		Usuario usuario = query.getSingleResult();
		return usuario;
	}
	
	public boolean verificarCpf(String cpf) {
		String jpql = "SELECT u FROM Usuario u where u.cpf = :cpf";
		TypedQuery<Usuario> query = entityManager.createQuery(jpql,Usuario.class);
		query.setParameter("cpf", cpf);
		
		List<Usuario> usuarios = query.getResultList();
		
		return !usuarios.isEmpty()?true:false;
	}
	
	public boolean verificarEmail(String email) {
		String jpql = "SELECT u FROM Usuario u where u.email = :email";
		TypedQuery<Usuario> query = entityManager.createQuery(jpql,Usuario.class);
		query.setParameter("email", email);
		
		List<Usuario> usuarios = query.getResultList();
		
		return !usuarios.isEmpty()?true:false;
	}
	
	public boolean verificarEmailAtualizado(String email, Integer id) {
		String jpql = "SELECT u FROM Usuario u where u.email = :email and u.id <> :id";
		TypedQuery<Usuario> query = entityManager.createQuery(jpql,Usuario.class);
		query.setParameter("email", email);
		query.setParameter("id", id);
		
		List<Usuario> usuarios = query.getResultList();
		
		return !usuarios.isEmpty()?true:false;
	}
	
	public boolean verificarCpfAtualizado(String cpf, Integer id) {
		String jpql = "SELECT u FROM Usuario u where u.cpf = :cpf and u.id <> :id";
		TypedQuery<Usuario> query = entityManager.createQuery(jpql,Usuario.class);
		query.setParameter("cpf", cpf);
		query.setParameter("id", id);
		
		List<Usuario> usuarios = query.getResultList();
		
		return !usuarios.isEmpty()?true:false;
	}
	
	public List<Usuario> verificarEmailAndSenha(String email, String senha){
		String jpql = "SELECT u FROM Usuario u where u.email = :email and u.senha = :senha";
		TypedQuery<Usuario> query = entityManager.createQuery(jpql,Usuario.class);
		query.setParameter("email", email);
		query.setParameter("senha", senha);
		
		List<Usuario> usuarios = query.getResultList();
		return usuarios;
	}
	
}
