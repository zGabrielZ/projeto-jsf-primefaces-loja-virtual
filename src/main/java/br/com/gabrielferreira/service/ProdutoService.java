package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.entidade.search.ProdutoSearch;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.CategoriaRepositorio;
import br.com.gabrielferreira.repositorio.ProdutoRepositorio;

public class ProdutoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoRepositorio produtoRepositorio;
	
	@Inject
	private CategoriaRepositorio categoriaRepositorio;
	
	public void inserir(Produto produto) throws RegraDeNegocioException {
		verificarNomeProduto(produto.getNome());
		produtoRepositorio.inserir(produto);
		Categoria categoria = categoriaRepositorio.pesquisarPorId(produto.getCategoria().getId(), Categoria.class);
		categoria.getProdutos().add(produto);
		categoriaRepositorio.atualizar(categoria);
	}
	
	public Produto atualizar(Produto produto) throws RegraDeNegocioException {
		verificarNomeProdutoAtualizado(produto.getNome(), produto.getId());
		return produtoRepositorio.atualizar(produto);
	}
	
	public Produto procurarPorId(Integer id) {
		return produtoRepositorio.pesquisarPorId(id, Produto.class);
	}
	
	public void remover(Produto produto) {
		produtoRepositorio.deletarPorId(Produto.class, produto.getId());
	}
	
	public List<Produto> getListagem(){
		return produtoRepositorio.getProdutos();
	}
	
	public List<Produto> getFiltrar(ProdutoSearch produtoSearch, int primeiroResultado, int quantidadeMaxima){
		return produtoRepositorio.filtrar(produtoSearch, primeiroResultado, quantidadeMaxima);
	}
	
	public Integer quantidadeRegistro(ProdutoSearch produtoSearch) {
		return produtoRepositorio.quantidadeRegistro(produtoSearch);
	}
	
	public List<Produto> procurarPorIdCategoria(Integer idCategoria){
		return produtoRepositorio.procurarPorIdCategoria(idCategoria);
	}
	
	public void verificarNomeProduto(String nome) throws RegraDeNegocioException {
		if(produtoRepositorio.verificarNomeProduto(nome)) {
			throw new RegraDeNegocioException("Não é possível inserir este nome, pois já está cadastrado.");
		}
	}
	
	public void verificarNomeProdutoAtualizado(String nome, Integer id) throws RegraDeNegocioException {
		if(produtoRepositorio.verificarNomeProdutoAtualizado(nome, id)) {
			throw new RegraDeNegocioException("Não é possível atualizar este nome, pois já está cadastrado.");
		}
	}
 
}
