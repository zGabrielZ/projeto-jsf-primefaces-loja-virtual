package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.search.CategoriaSearch;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.CategoriaRepositorio;

public class CategoriaService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoriaRepositorio categoriaRepositorio;
	
	public void inserir(Categoria categoria) throws RegraDeNegocioException {
		verificarNomeCategoria(categoria.getNome());
		categoriaRepositorio.inserir(categoria);
	}
	
	public Categoria atualizar(Categoria categoria) throws RegraDeNegocioException {
		verificarNomeCategoriaAtualizado(categoria.getNome(), categoria.getId());
		return categoriaRepositorio.atualizar(categoria);
	}
	
	public List<Categoria> getFiltrar(CategoriaSearch categoriaSearch, int primeiroResultado, int quantidadeMaxima){
		return categoriaRepositorio.filtrar(categoriaSearch, primeiroResultado, quantidadeMaxima);
	}
	
	public Integer quantidadeRegistro(CategoriaSearch categoriaSearch) {
		return categoriaRepositorio.quantidadeRegistro(categoriaSearch);
	}
	
	public List<Categoria> getCategoriaById(Integer id){
		return categoriaRepositorio.procurarPorId(id);
	}
	
	public List<Categoria> listaCategorias(){
		return categoriaRepositorio.listaCategorias();
	}
	
	public Categoria procurarPorIdCategoria(Integer id) {
		return categoriaRepositorio.pesquisarPorId(id, Categoria.class);
	}
	
	public void verificarNomeCategoria(String nome) throws RegraDeNegocioException {
		if(categoriaRepositorio.verificarNomeCategoria(nome)) {
			throw new RegraDeNegocioException("Não é possível inserir este nome, pois já está cadastrado.");
		}
	}
	
	public void verificarNomeCategoriaAtualizado(String nome, Integer id) throws RegraDeNegocioException {
		if(categoriaRepositorio.verificarNomeCategoriaAtualizado(nome, id)) {
			throw new RegraDeNegocioException("Não é possível atualizar este nome, pois já está cadastrado.");
		}
	}

}
