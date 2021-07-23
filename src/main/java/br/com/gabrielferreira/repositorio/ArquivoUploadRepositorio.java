package br.com.gabrielferreira.repositorio;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.gabrielferreira.entidade.ArquivoUpload;
public class ArquivoUploadRepositorio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	public ArquivoUploadRepositorio() {}
	
	public void inserir(ArquivoUpload arquivoUpload) {
		entityManager.persist(arquivoUpload);
	}

}
