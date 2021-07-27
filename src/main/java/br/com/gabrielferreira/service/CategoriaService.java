package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.search.CategoriaSearch;
import br.com.gabrielferreira.repositorio.CategoriaRepositorio;

public class CategoriaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoriaRepositorio categoriaRepositorio;
	
	public List<Categoria> getFiltrar(CategoriaSearch categoriaSearch){
		return categoriaRepositorio.filtrar(categoriaSearch);
	}
	
	public List<Categoria> getCategoriaById(Integer id){
		return categoriaRepositorio.procurarPorId(id);
	}

}
