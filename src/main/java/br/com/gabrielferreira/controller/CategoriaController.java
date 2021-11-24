package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
@Getter
@Setter
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
	
	@Inject
	private LazyDataTableModelCategoria<Categoria> categorias;
	
	private String tituloCadastroCategoria;
	
	private List<Produto> produtos;
	
	private CategoriaSearch categoriaSearch;
	
	private Categoria categoria;
	
	@PostConstruct
	public void inicializar() {
		categoriaSearch = new CategoriaSearch();
		produtos = new ArrayList<Produto>();
		categoria = new Categoria();
		verificarParametro();
	}
	
	private void verificarParametro() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idCodigoConsultaProdutos = params.get("codigoConsultaProdutos");
		String idCodigoAtualizarCategoria = params.get("codigoAtualizarCategoria");
		
		if(idCodigoConsultaProdutos != null) {
			produtos = produtoService.procurarPorIdCategoria(Integer.parseInt(idCodigoConsultaProdutos));
			tituloCadastroCategoria = categoriaService.procurarPorIdCategoria(Integer.parseInt(idCodigoConsultaProdutos)).getNome();
		}
		
		if(idCodigoAtualizarCategoria != null) {
			categoria = categoriaService.procurarPorIdCategoria(Integer.parseInt(idCodigoAtualizarCategoria));
		}
		
	}
	
	public void consultarCategoria() {
		categorias.setCategoriaSearch(categoriaSearch);
		categorias.load(0,5,null,null,null);
	}
	
	public void inserirOuAtualizarCategoria() {
		
		if(categoria.getId() == null) {
			inserirCategoria();
			limparFormularioCategoriaCadastro();
		} else {
			atualizarCategoria(categoria);
			limparFormularioCategoria();
		}
	}
	
	public void inserirCategoria() {
		try {
			categoriaService.inserir(categoria);
			FacesMessages.adicionarMensagem("cadastroCategoriaForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
					null);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("cadastroCategoriaForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
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
	
	public List<Categoria> getListaCategorias(){
		return categoriaService.listaCategorias();
	}
	
	public void limparFormularioCategoria() {
		categoriaSearch = new CategoriaSearch();
		consultarCategoria();
	}
	
	public void limparFormularioCategoriaCadastro() {
		categoria = new Categoria();
	}

}
