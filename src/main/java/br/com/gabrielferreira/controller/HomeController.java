package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.service.ProdutoService;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class HomeController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoService produtoService;
	
	@Getter
	@Setter
	private List<Produto> produtos;
	
	@PostConstruct
	public void inicializar() {
		produtos = produtoService.getProdutos();
	}

}
