package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.service.ProdutoService;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ProdutoDetalheController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoService produtoService;
	
	@Getter
	@Setter
	private Produto produto;
	
	@PostConstruct
	public void inicializar() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		produto = produtoService.procurarPorId(Integer.parseInt(id));
	}
}
