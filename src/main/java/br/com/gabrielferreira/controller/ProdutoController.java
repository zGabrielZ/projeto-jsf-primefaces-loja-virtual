package br.com.gabrielferreira.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
@Getter
@Setter
public class ProdutoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private NavegacaoController navegacaoController;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private LazyDataTableModelProduto<Produto> produtos;
	
	private Produto produto;

	private ProdutoSearch produtoSearch;
	
	private Produto produtoSelecionado;
	
	private Part imagemUploadProduto;
	
	@PostConstruct
	public void inicializar() {
		produto = new Produto();
		produtoSearch = new ProdutoSearch();
		verificarParametro();
	}
	
	private void verificarParametro() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idCodigoDetalheProduto = params.get("codigoDetalheProduto");
		String idCodigoAtualizarProduto = params.get("codigoAtualizarProduto");
		
		if(idCodigoDetalheProduto != null) {
			produto = produtoService.procurarPorId(Integer.parseInt(idCodigoDetalheProduto));
		}
		
		if(idCodigoAtualizarProduto != null) {
			produto = produtoService.procurarPorId(Integer.parseInt(idCodigoAtualizarProduto));
		}
	}
	
	public void consultarProduto() {
		produtos.setProdutoSearch(produtoSearch);
		produtos.load(0, 5, null, null, null);
	}
	
	public void inserirOuAtualizarProduto() {
		if(produto.getId() == null) {
			inserirProduto();
			limparFormularioProduto();
		} else {
			atualizarProduto();
			limparFormularioProduto();
		}
	}
	
	public void inserirProduto() {
		try {
			uploadImagem();
			produtoService.inserir(produto);
			FacesMessages.adicionarMensagem("cadastroProdutoForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
					null);
		} catch (RegraDeNegocioException  | IOException e) {
			FacesMessages.adicionarMensagem("cadastroProdutoForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
	}
	
	public void atualizarProduto() {
		try {
			if(imagemUploadProduto != null) {
				deletarImagemSalva(produto);
			}
			uploadImagem();
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
	
	public void uploadImagem() throws IOException {
		if(imagemUploadProduto != null) {
			if(imagemUploadProduto.getContentType().equals("image/jpeg") || imagemUploadProduto.getContentType().equals("image/png")
					|| imagemUploadProduto.getContentType().equals("image/jpg")){
				
				String caminhoImagem = caminhoImagem(imagemUploadProduto);
				String extensaoImagem = extensaoImagem(imagemUploadProduto);
				produto.setCaminhoImagem(caminhoImagem);
				produto.setExtensao(extensaoImagem);
				
				// Enviar a imagem processada para o caminho da pasta
				
				// Cria um espaço de memória que vai armazenar o conteúdo da imagem 
				byte[] bytesImagem = new byte[(int) imagemUploadProduto.getSize()];
				
				// Le o conteudo da imagem e joga dentro do array de bytes
				imagemUploadProduto.getInputStream().read(bytesImagem);
				
				// Cria uma referencia para o arquivo que será criado no lado do servidor
				File arquivo = new File(caminhoImagem);
				
				// Cria o objeto que irá manipular o arquivo criado
				FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
				
				// Escreve o conteudo da imagem (upload) dentro do arquivo servidor
				fileOutputStream.write(bytesImagem);
				
				fileOutputStream.close();
			}
		}
	}
	
	private String caminhoImagem(Part imagemUploadProduto) {
		String caminho = "C:\\Users\\Acer\\Desktop\\Curso Java Server Faces\\8.Projetos\\Projeto-Loja\\src\\main\\webapp\\resources\\imagem-produto";
		String nomeArquivo = nomeArquivo(imagemUploadProduto);
		return caminho + "\\" + nomeArquivo;
	}
		
	private String nomeArquivo(Part imagemUploadProduto) {
		for (String header : imagemUploadProduto.getHeader("content-disposition").split(";")) {
			if (header.trim().startsWith("filename")) {
				return header.substring(header.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
	
	private String extensaoImagem(Part imagemUpload) {
		String extensao = imagemUpload.getContentType().split("\\/")[1];
		return extensao;
	}
	
	public void excluirProduto() {
		try {
			Produto produto = produtoService.procurarPorId(produtoSelecionado.getId());			
			deletarImagemSalva(produto);
			produtoService.remover(produto);
			consultarProduto();
			FacesMessages.adicionarMensagem("consultaProdutosForm:msg", FacesMessage.SEVERITY_INFO, "Removido com sucesso !",
					null);
		} catch (Exception e) {
			FacesMessages.adicionarMensagem("consultaProdutosForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível excluir, pois tem entidades relacionada !",
					"Não é possível excluir !");
		}
	}
	
	public void deletarImagemSalva(Produto produto) {
		if(produto.getCaminhoImagem() != null) {
			File file = new File(produto.getCaminhoImagem());
			file.delete();
		}
	}
	
	public void limparFormularioProdutoPesquisa() {
		produtoSearch = new ProdutoSearch();
		consultarProduto();
	}
	
	public void limparFormularioProduto() {
		produto = new Produto();
	}

}
