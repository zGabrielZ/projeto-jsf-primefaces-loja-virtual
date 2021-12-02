package br.com.gabrielferreira.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.ArquivoUpload;
import br.com.gabrielferreira.repositorio.ArquivoUploadRepositorio;

public class ArquivoUploadService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ArquivoUploadRepositorio arquivoUploadRepositorio;
	
	public void inserir(ArquivoUpload arquivoUpload) {
		arquivoUploadRepositorio.inserir(arquivoUpload);
	}

}
