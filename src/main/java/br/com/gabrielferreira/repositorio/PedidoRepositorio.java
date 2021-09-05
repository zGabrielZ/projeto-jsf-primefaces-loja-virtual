package br.com.gabrielferreira.repositorio;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import br.com.gabrielferreira.entidade.Pedido;
public class PedidoRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public PedidoRepositorio() {}
	
	public void inserir(Pedido pedido) {
		entityManager.persist(pedido);
	}
	
	public void atualizar(Pedido pedido) {
		entityManager.merge(pedido);
	}
	
	public void remover(Pedido pedido) {
		pedido = procurarPorId(pedido.getId());
		entityManager.remove(pedido);
	}
	
	public Pedido procurarPorId(Integer id) {
		return entityManager.find(Pedido.class, id);
	}
	
	public List<Pedido> getListagem(Integer idUsuario) {
		String jpql = "SELECT p FROM Itens i JOIN i.pedido p JOIN p.usuario u JOIN i.produto pro where u.id = :idUsuario group by p.id order by p.codigoPedido asc";
		TypedQuery<Pedido> query = entityManager.createQuery(jpql,Pedido.class);
		query.setParameter("idUsuario", idUsuario);
		List<Pedido> pedidos = query.getResultList();
		return pedidos;
	}
	
	public List<Pedido> getPedidoByUsuarioId(Integer idUsuario) {
		String jpql = "SELECT p FROM Usuario u join u.pedidos p where u.id = :idUsuario";
		TypedQuery<Pedido> query = entityManager.createQuery(jpql,Pedido.class);
		query.setParameter("idUsuario", idUsuario);
		
		try {
			List<Pedido> pedidos = query.getResultList();
			return pedidos;
		} catch (NoResultException e) {
			return null;
		}
	}

}
