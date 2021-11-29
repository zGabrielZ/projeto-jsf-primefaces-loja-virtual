package br.com.gabrielferreira.repositorio;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.com.gabrielferreira.entidade.Pedido;
import br.com.gabrielferreira.repositorio.generico.RepositorioGenerico;
public class PedidoRepositorio extends RepositorioGenerico<Pedido>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
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
		List<Pedido> pedidos = verificarNuloLista(query);
		return pedidos;
	}

}
