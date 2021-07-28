package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.search.CategoriaSearch;
import br.com.gabrielferreira.service.CategoriaService;
import br.com.gabrielferreira.utils.FacesMessages;
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
	
	@Inject
	private NavegacaoController navegacaoController;
	
	@Getter
	@Setter
	private List<Categoria> categorias;
	
	@Getter
	@Setter
	private CategoriaSearch categoriaSearch;
	
	@Getter
	@Setter
	private Categoria categoria;
	
	@PostConstruct
	public void inicializar() {
		categoriaSearch = new CategoriaSearch();
		categorias = new ArrayList<Categoria>();
		categoria = new Categoria();
	}
	
	public void consultarCategoria() {
		categorias = categoriaService.getFiltrar(categoriaSearch);
	}
	
	public void inserirOuAtualizarCategoria() {
		
		if(categoria.getId() == null) {
			inserirCategoria(categoria);
			categoria = new Categoria();
		} else {
			atualizarCategoria(categoria);
		}
	}
	
	public void inserirCategoria(Categoria categoria) {
		categoriaService.inserir(categoria);
		FacesMessages.adicionarMensagem("cadastroCategoriaForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
				null);
	}
	
	public void atualizarCategoria(Categoria categoria) {
		categoriaService.atualizar(categoria);
		FacesMessages.adicionarMensagem("cadastroCategoriaForm:msg", FacesMessage.SEVERITY_INFO, "Atualizado com sucesso !",
				null);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		navegacaoController.consultaCategoria();
	}
	
	public void carregar() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		if(id != null) {
			categoria = categoriaService.procurarPorIdCategoria(Integer.parseInt(id));
		}
	}
	
	public String selecionarCategoriaAtualizar(Categoria categoria) {
		this.categoria = categoria;
		return "/categoria/cadastro/CadastroCategoria?faces-redirect=true&codigo="+this.categoria.getId();
	}
	
	public void limparFormularioCategoria() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("frmConsulta:nome");
		htmlInputTextNome.setSubmittedValue("");
		categoriaSearch = new CategoriaSearch();
	}
	
	public void limparFormularioCategoriaCadastro() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("cadastroCategoriaForm:nome");
		htmlInputTextNome.setSubmittedValue("");
		categoriaSearch = new CategoriaSearch();
	}

}
