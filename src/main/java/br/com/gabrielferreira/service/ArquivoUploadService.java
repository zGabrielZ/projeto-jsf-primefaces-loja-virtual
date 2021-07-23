package br.com.gabrielferreira.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.ArquivoUpload;
import br.com.gabrielferreira.repositorio.ArquivoUploadRepositorio;
import br.com.gabrielferreira.utils.Transacional;

public class ArquivoUploadService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ArquivoUploadRepositorio arquivoUploadRepositorio;
	
	@Transacional
	public void inserirExcel(ArquivoUpload arquivoUpload) {
		arquivoUploadRepositorio.inserir(arquivoUpload);
	}
	
	@Transacional
	public void inserirTxt(ArquivoUpload arquivoUpload) {
		arquivoUploadRepositorio.inserir(arquivoUpload);
	}

}
