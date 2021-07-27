package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.search.CategoriaSearch;
import br.com.gabrielferreira.service.CategoriaService;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class CategoriaController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CategoriaService categoriaService;
	
	@Getter
	@Setter
	private List<Categoria> categorias;
	
	@Getter
	@Setter
	private CategoriaSearch categoriaSearch;
	
	@PostConstruct
	public void inicializar() {
		categoriaSearch = new CategoriaSearch();
		categorias = new ArrayList<Categoria>();
	}
	
	public void consultarCategoria() {
		categorias = categoriaService.getFiltrar(categoriaSearch);
	}
	
	public void limparFormularioCategoria() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("frmConsulta:nome");
		htmlInputTextNome.setSubmittedValue("");
		categoriaSearch = new CategoriaSearch();
	}

}
