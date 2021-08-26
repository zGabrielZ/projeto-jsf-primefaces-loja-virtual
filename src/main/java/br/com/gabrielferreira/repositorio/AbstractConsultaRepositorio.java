package br.com.gabrielferreira.repositorio;

import java.io.Serializable;
import java.util.List;
import javax.persistence.TypedQuery;

public abstract class AbstractConsultaRepositorio<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public abstract TypedQuery<T> getListagem(T search);
	
	public abstract List<T> filtrar(T search, int primeiroResultado, int quantidadeMaxima);
	
	public abstract Integer quantidadeRegistro(T search);
	
}
