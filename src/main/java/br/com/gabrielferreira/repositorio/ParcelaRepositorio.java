package br.com.gabrielferreira.repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.gabrielferreira.entidade.Parcela;
import br.com.gabrielferreira.repositorio.generico.RepositorioGenerico;
public class ParcelaRepositorio extends RepositorioGenerico<Parcela>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public TypedQuery<Parcela> getListagem(Integer idUsuario) {
		String jpql = "SELECT par FROM Parcela par JOIN par.pedido p JOIN p.usuario u where u.id = :idUsuario order by p.codigoPedido asc";
		TypedQuery<Parcela> query = entityManager.createQuery(jpql,Parcela.class);
		query.setParameter("idUsuario", idUsuario);
		return query;
	}
	
	public List<Parcela> getParcelasByUsuario(Integer idUsuario,int primeiroResultado, int quantidadeMaxima) {
		TypedQuery<Parcela> query = getListagem(idUsuario);
		List<Parcela> parcelas = query.setFirstResult(primeiroResultado).setMaxResults(quantidadeMaxima).getResultList();
		return parcelas;
	}
	
	public Integer quantidadeRegistro(Integer idUsuario) {
		TypedQuery<Parcela> typedQuery = getListagem(idUsuario);
		List<Parcela> parcelas = typedQuery.getResultList();
		return parcelas.size();
	}
	
	public void inserirParcelas(List<Parcela> parcelas) {
		for(Parcela parcela : parcelas) {
			entityManager.persist(parcela);
		}
	}
	
	public List<Parcela> getParcelas() {
		String jpql = "SELECT p FROM Parcela p JOIN FETCH p.pedido ped";
		TypedQuery<Parcela> query = entityManager.createQuery(jpql,Parcela.class);
		
		List<Parcela> parcelas = query.getResultList();
		return parcelas;
	}	

}
