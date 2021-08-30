package br.com.gabrielferreira.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import br.com.gabrielferreira.datatablelazy.LazyDataTableModelProduto;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.entidade.search.ProdutoSearch;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.ProdutoService;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ProdutoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private NavegacaoController navegacaoController;
	
	@Inject
	private ProdutoService produtoService;
	
	@Getter
	@Setter
	private String tituloCadastroProduto;
	
	@Getter
	@Setter
	private Produto produto;
	
	@Getter
	@Setter
	private ProdutoSearch produtoSearch;
	
	@Inject
	@Getter
	private LazyDataTableModelProduto<Produto> produtos;
	
	@Getter
	@Setter
	private Produto produtoSelecionado;
	
	@Getter
	@Setter
	private Part imagemUploadProduto;
	
	@PostConstruct
	public void inicializar() {
		tituloCadastroProduto = "Cadastro Produto";
		produto = new Produto();
		produtoSearch = new ProdutoSearch();
	}
	
	public void consultarProduto() {
		produtos.setProdutoSearch(produtoSearch);
		produtos.load(0, 5, null, null, null);
	}
	
	public void inserirOuAtualizarProduto() {
		if(produto.getId() == null) {
			boolean inserir = inserirProduto(produto);
			if(inserir) {
				produto = new Produto();
			}
		} else {
			atualizarProduto(produto);
		}
	}
	
	public boolean inserirProduto(Produto produto) {
		boolean inserir = true;
		try {
			uploadImagem(produto);
			produtoService.inserir(produto);
			FacesMessages.adicionarMensagem("cadastroProdutoForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
					null);
		} catch (RegraDeNegocioException  | IOException e) {
			FacesMessages.adicionarMensagem("cadastroProdutoForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
			inserir = false;
		}
		return inserir;
	}
	
	public void atualizarProduto(Produto produto) {
		try {
			uploadImagem(produto);
			produtoService.atualizar(produto);
			FacesMessages.adicionarMensagem("cadastroProdutoForm:msg", FacesMessage.SEVERITY_INFO, "Atualizado com sucesso !",
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.consultaProduto();
		} catch (RegraDeNegocioException | IOException e) {
			FacesMessages.adicionarMensagem("cadastroProdutoForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
	}
	
	public void uploadImagem(Produto produto) throws IOException {
		if(imagemUploadProduto.getContentType().equals("image/jpeg") || imagemUploadProduto.getContentType().equals("image/png")){
			String caminhoImagem = caminhoImagem(imagemUploadProduto,produto);
			produto.setCaminhoImagem(caminhoImagem);
		}
	}
	
	private String caminhoImagem(Part imagemUploadProduto, Produto produto) throws IOException {
		String nome = nomeArquivo(imagemUploadProduto);
		// Caminho completo da imagem
		String caminhoTipo = "C:\\Users\\Acer\\Desktop\\Curso Java Server Faces\\8.Projetos\\Projeto-Loja\\src\\main\\webapp\\resources\\imagem-produto\\"+nome;
		// Criar um espaço de memoria que vai salvar o conteudo da imagem 
		byte[] bytesImagens = new byte[(int) imagemUploadProduto.getSize()];
		produto.setByteImagem(bytesImagens);
		// le o conteudo da imagem e joga dentro do array 
		imagemUploadProduto.getInputStream().read(bytesImagens);
		// Criar uma referencia do arquivo
		File file = new File(caminhoTipo);
		// Criar o objeto que irá manipular o arquivo criado
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		// Escrever o conteudo da imagem dentro do servidor 
		fileOutputStream.write(bytesImagens);
		// Fechando 
		fileOutputStream.close();
		
		
		return caminhoTipo;
	}
	
	private String nomeArquivo(Part imagemUploadProduto) {
		for (String header : imagemUploadProduto.getHeader("content-disposition").split(";")) {
			if (header.trim().startsWith("filename")) {
				return header.substring(header.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
	
	public void excluirProduto() {
		try {
			Produto produto = produtoService.procurarPorId(produtoSelecionado.getId());			
			produtoService.remover(produto);
			consultarProduto();
			FacesMessages.adicionarMensagem("consultaProdutosForm:msg", FacesMessage.SEVERITY_INFO, "Removido com sucesso !",
					null);
		} catch (Exception e) {
			FacesMessages.adicionarMensagem("consultaProdutosForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível excluir, pois tem entidades relacionada !",
					"Não é possível excluir !");
		}
	}
	
	public void carregar() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		if(id != null) {
			tituloCadastroProduto = "Atualizar Produto";
			produto = produtoService.procurarPorId(Integer.parseInt(id));
		}
	}
	
	public String selecionarProdutoAtualizar(Produto produto) {
		this.produto = produto;
		return "/produto/cadastro/CadastroProduto?faces-redirect=true&codigo="+this.produto.getId();
	}
	
	public String selecionarProdutoDetalhe(Produto produto) {
		this.produto = produto;
		return "/produto/detalhe/DetalheProduto?faces-redirect=true&codigo="+this.produto.getId();
	}
	
	public void limparFormularioProdutoPesquisa() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("frmConsulta:nome");
		HtmlInputText htmlInputTextEstoque = (HtmlInputText) uiViewRoot.findComponent("frmConsulta:estoque");
		HtmlInputText htmlInputTextData = (HtmlInputText) uiViewRoot.findComponent("frmConsulta:data");
		htmlInputTextNome.setSubmittedValue("");
		htmlInputTextData.setSubmittedValue("");
		htmlInputTextEstoque.setSubmittedValue("");
		produtoSearch = new ProdutoSearch();
		consultarProduto();
	}
	
	public void limparFormularioProduto() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("cadastroProdutoForm:nome");
		HtmlInputText htmlInputTextValor = (HtmlInputText) uiViewRoot.findComponent("cadastroProdutoForm:valor");
		HtmlInputText htmlInputTextEstoque = (HtmlInputText) uiViewRoot.findComponent("cadastroProdutoForm:estoque");
		HtmlInputText htmlInputTextData = (HtmlInputText) uiViewRoot.findComponent("cadastroProdutoForm:data");
		HtmlSelectOneMenu htmlSelectOneMenuCategoria = (HtmlSelectOneMenu) uiViewRoot.findComponent("cadastroProdutoForm:categoria");
		htmlInputTextNome.setSubmittedValue("");
		htmlInputTextValor.setSubmittedValue("");
		htmlInputTextEstoque.setSubmittedValue("");
		htmlInputTextData.setSubmittedValue("");
		htmlSelectOneMenuCategoria.setSubmittedValue("");
		produto = new Produto();
	}

}
