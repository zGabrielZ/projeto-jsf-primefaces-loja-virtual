package br.com.gabrielferreira.repositorio;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.gabrielferreira.entidade.Endereco;
import br.com.gabrielferreira.repositorio.generico.RepositorioGenerico;
public class EnderecoRepositorio extends RepositorioGenerico<Endereco>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
		
	public Endereco procurarPorIdByUsuario(Integer idUsuario) {
		String jpql = "SELECT e FROM Endereco e join e.usuario u where u.id = :idUsuario";
		TypedQuery<Endereco> query = entityManager.createQuery(jpql,Endereco.class);
		query.setParameter("idUsuario", idUsuario);
		
		Endereco endereco = verificarNulo(query);
		return endereco;
	}

}
