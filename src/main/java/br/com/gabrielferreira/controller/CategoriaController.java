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

import br.com.gabrielferreira.datatablelazy.LazyDataTableModelCategoria;
import br.com.gabrielferreira.entidade.Categoria;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.entidade.search.CategoriaSearch;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.CategoriaService;
import br.com.gabrielferreira.service.ProdutoService;
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
	private ProdutoService produtoService;
	
	@Inject
	private NavegacaoController navegacaoController;
	
	@Getter
	@Setter
	private String tituloCadastroCategoria;
	
	@Inject
	@Getter
	private LazyDataTableModelCategoria<Categoria> categorias;
	
	@Getter
	@Setter
	private List<Produto> produtos;
	
	@Getter
	@Setter
	private CategoriaSearch categoriaSearch;
	
	@Getter
	@Setter
	private Categoria categoria;
	
	@PostConstruct
	public void inicializar() {
		tituloCadastroCategoria = "Cadastro de categoria";
		categoriaSearch = new CategoriaSearch();
		produtos = new ArrayList<Produto>();
		categoria = new Categoria();
	}
	
	public void consultarCategoria() {
		categorias.setCategoriaSearch(categoriaSearch);
		categorias.load(0,5,null,null,null);
	}
	
	public void inserirOuAtualizarCategoria() {
		
		if(categoria.getId() == null) {
			boolean inserir = inserirCategoria(categoria);
			if(inserir) {
				categoria = new Categoria();
			}
		} else {
			atualizarCategoria(categoria);
		}
	}
	
	public boolean inserirCategoria(Categoria categoria) {
		boolean inserir = true;
		try {
			categoriaService.inserir(categoria);
			FacesMessages.adicionarMensagem("cadastroCategoriaForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
					null);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("cadastroCategoriaForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
			inserir = false;
		}
		return inserir;
	}
	
	public void atualizarCategoria(Categoria categoria) {
		try {
			categoriaService.atualizar(categoria);
			FacesMessages.adicionarMensagem("cadastroCategoriaForm:msg", FacesMessage.SEVERITY_INFO, "Atualizado com sucesso !",
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.consultaCategoria();
		} catch (Exception e) {
			FacesMessages.adicionarMensagem("cadastroCategoriaForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
	}
	
	public void carregar() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		if(id != null) {
			categoria = categoriaService.procurarPorIdCategoria(Integer.parseInt(id));
			tituloCadastroCategoria = "Atualizar categoria";
		}
	}
	
	public String selecionarCategoriaAtualizar(Categoria categoria) {
		this.categoria = categoria;
		return "/categoria/cadastro/CadastroCategoria?faces-redirect=true&codigo="+this.categoria.getId();
	}
	
	public String selecionarProdutos(Categoria categoria) {
		this.categoria = categoria;
		return "/categoria/consulta/Produtos?faces-redirect=true&codigoCategoria="+this.categoria.getId();
	}
	
	public void carregarProdutos() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigoCategoria");
		if(id != null) {
			produtos = produtoService.procurarPorIdCategoria(Integer.parseInt(id));
			Categoria categoria = categoriaService.procurarPorIdCategoria(Integer.parseInt(id));
			tituloCadastroCategoria = categoria.getNome();
		}
	}
	
	public List<Categoria> getListaCategorias(){
		return categoriaService.listaCategorias();
	}
	
	public void limparFormularioCategoria() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("frmConsulta:nome");
		htmlInputTextNome.setSubmittedValue("");
		categoriaSearch = new CategoriaSearch();
		consultarCategoria();
	}
	
	public void limparFormularioCategoriaCadastro() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("cadastroCategoriaForm:nome");
		htmlInputTextNome.setSubmittedValue("");
		categoriaSearch = new CategoriaSearch();
	}

}
