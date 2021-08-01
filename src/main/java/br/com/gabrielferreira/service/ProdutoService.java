package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.entidade.search.ProdutoSearch;
import br.com.gabrielferreira.repositorio.CategoriaRepositorio;
import br.com.gabrielferreira.repositorio.ProdutoRepositorio;
import br.com.gabrielferreira.utils.Transacional;

public class ProdutoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoRepositorio produtoRepositorio;
	
	@Inject
	private CategoriaRepositorio categoriaRepositorio;
	
	@Transacional
	public void inserir(Produto produto) {
		produtoRepositorio.inserir(produto);
		Categoria categoria = categoriaRepositorio.procurarPorIdCategoria(produto.getCategoria().getId());
		categoria.getProdutos().add(produto);
		categoriaRepositorio.atualizar(categoria);
	}
	
	@Transacional
	public void atualizar(Produto produto) {
		produtoRepositorio.atualizar(produto);
	}
	
	public Produto procurarPorId(Integer id) {
		return produtoRepositorio.procurarPorId(id);
	}
	
	@Transacional
	public void remover(Produto produto) {
		produtoRepositorio.remover(produto);
	}
	
	public List<Produto> getFiltrar(ProdutoSearch produtoSearch){
		return produtoRepositorio.filtrar(produtoSearch);
	}
 
}
