package br.com.gabrielferreira.repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.gabrielferreira.entidade.Itens;
import br.com.gabrielferreira.repositorio.generico.RepositorioGenerico;
public class ItensRepositorio extends RepositorioGenerico<Itens>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public void inserirItens(List<Itens> itens) {
		for(Itens item : itens) {
			entityManager.persist(item);
		}
	}
	
	public TypedQuery<Itens> getListagem(Integer idUsuario) {
		String jpql = "SELECT i FROM Itens i JOIN i.pedido p JOIN p.usuario u JOIN i.produto pro where u.id = :idUsuario order by p.codigoPedido asc";
		TypedQuery<Itens> query = entityManager.createQuery(jpql,Itens.class);
		query.setParameter("idUsuario", idUsuario);
		return query;
	}
	
	public List<Itens> getItensByUsuario(Integer idUsuario,int primeiroResultado, int quantidadeMaxima) {
		TypedQuery<Itens> query = getListagem(idUsuario);
		List<Itens> itens = query.setFirstResult(primeiroResultado).setMaxResults(quantidadeMaxima).getResultList();
		return itens;
	}
	
	public Integer quantidadeRegistro(Integer idUsuario) {
		TypedQuery<Itens> typedQuery = getListagem(idUsuario);
		List<Itens> itens = typedQuery.getResultList();
		return itens.size();
	}

}
