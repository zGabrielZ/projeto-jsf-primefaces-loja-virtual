package br.com.gabrielferreira.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.gabrielferreira.entidade.ArquivoUpload;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.enums.TipoArquivo;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.ArquivoUploadService;
import br.com.gabrielferreira.service.UsuarioService;
import br.com.gabrielferreira.utils.FacesMessages;
import br.com.gabrielferreira.utils.validation.UsuarioLoteValidacao;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
public class UsuarioLoteController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	@Inject
	private ArquivoUploadService arquivoUploadService;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Setter
	private StreamedContent modeloArquivoExcel;
	
	@Setter
	private StreamedContent modeloArquivoTxt;
	
	@Setter
	private StreamedContent erros;
	
	@Getter
	@Setter
	private ArquivoUpload arquivoUpload;
	
	@Getter
	@Setter
	private Part arquivoUploadExcelTxt;
	
	@Getter
	@Setter
	private List<Usuario> usuarios;
	
	@Getter
	@Setter
	private List<UsuarioLoteValidacao> usuarioLoteValidacoes;
	
	@Getter
	@Setter
	private List<String> mensagens;
	
	@PostConstruct
	public void inicializar() {
		arquivoUpload = new ArquivoUpload();
		usuarios = new ArrayList<>();
		usuarioLoteValidacoes = new ArrayList<>();
		mensagens = new ArrayList<>();
	}

	// Segunda versao implementação do upload arquivo excel
	public void uploadArquivoExcel() throws IOException, ParseException {
	
		// Caso o tipo do arquivo for xlsx
		if(arquivoUploadExcelTxt.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				&& usuarios.isEmpty()) {
			
			byte[] arquivoByte = toByteArrays(arquivoUploadExcelTxt.getInputStream());
			arquivoUpload.setArquivo(arquivoByte);
			arquivoUpload.setTipoArquivo(TipoArquivo.EXCEL);
			usuarioService.verificarArquivoExcel(arquivoUploadExcelTxt,usuarios);
			
		} else {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteExcelForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível inserir em lote !",
					null);
		}
		
	}
	
	// Segunda versao implementação do upload arquivo txt
	public void uploadArquivoTxt() throws IOException {
		// Caso o tipo do arquivo for txt 
		if(arquivoUploadExcelTxt.getContentType().equals("text/plain") && usuarios.isEmpty()) {
			
			byte[] arquivoByte = toByteArrays(arquivoUploadExcelTxt.getInputStream());
			arquivoUpload.setArquivo(arquivoByte);
			arquivoUpload.setTipoArquivo(TipoArquivo.TXT);
			try {
				usuarioService.verificarArquivoTxt(arquivoUploadExcelTxt,usuarios,dtf);
			} catch (Exception e) {
				FacesMessages.adicionarMensagem("cadastroUsuarioLoteTxtForm:msg", FacesMessage.SEVERITY_ERROR, "Erro ao importar, verifique o arquivo !",
						null);
			}
			
		} else {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteTxtForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível inserir em lote !",
					null);
		}
	}
	
	public void cadastrarUsuarios() throws RegraDeNegocioException, ParseException {
		List<UsuarioLoteValidacao> validacaos = usuarioService.validacaoUsuariosLote(usuarios, usuarioLoteValidacoes);
		Map<Integer, List<String>> agrupaValidacoes = getValidacoes(validacaos);
		List<Usuario> listaUsuarios = adicionandoErros(agrupaValidacoes);
		// Remover da lista os que tem o codigo de erro como verdadeiro
		listaUsuarios.removeIf(u -> u.isCodigoErro());
		
		// Salvar o arquivo e tambem o usuario
		if(!listaUsuarios.isEmpty()) {
			arquivoUploadService.inserir(arquivoUpload);
			usuarioService.inserirUsuarioLote(listaUsuarios);
		}
		
		if(!listaUsuarios.isEmpty() && mensagens.isEmpty()) {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrados com sucesso !",
					null);
		} else if(!listaUsuarios.isEmpty() && !mensagens.isEmpty()) {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm:msg", FacesMessage.SEVERITY_WARN, "Alguns foram cadastros e outros não, baixe o arquivo para verificar os erros !",
					null);
		} else {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm:msg", FacesMessage.SEVERITY_ERROR, "Baixe o arquivo para verificar os erros !",
					null);
		}
		
		arquivoUpload = new ArquivoUpload();
		usuarios = new ArrayList<>();
	}
	
	private List<Usuario> adicionandoErros(Map<Integer, List<String>> agrupaValidacoes){
		if (!agrupaValidacoes.isEmpty()) {
			for (Entry<Integer, List<String>> entry : agrupaValidacoes.entrySet()) {
				for (Usuario usuario : usuarios) {
					if (usuario.getCodigoUsuario().equals(entry.getKey())) {
						// Seta o usuario como erro e tambem adicionar na lista de mensagens
						usuario.setCodigoErro(true);
						StringBuilder sb = new StringBuilder();
						sb.append("Código do usuário : " + entry.getKey() + "\n");
						sb.append("Lista de erros abaixo : \n");
						sb.append(entry.getValue());
						mensagens.add(sb.toString());
					}
				}
			}
		}
		return usuarios;
	}
	
	private Map<Integer, List<String>> getValidacoes(List<UsuarioLoteValidacao> validacoes){
		
		Map<Integer, List<String>> map = new TreeMap<>();
		
		for(UsuarioLoteValidacao lote : validacoes) {
			if(!map.containsKey(lote.getCodigoUsuario())) {
				map.put(lote.getCodigoUsuario(), new ArrayList<String>());
			}
			map.get(lote.getCodigoUsuario()).add(lote.getErros());
		}
		
		return map;
	}

	public StreamedContent getModeloArquivoExcel() throws FileNotFoundException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		String caminhoTipo = facesContext.getExternalContext().getRealPath("/resources/modelo-excel/modelo-usuario.xlsx");
		InputStream inputStream = new FileInputStream(caminhoTipo);
		modeloArquivoExcel = new DefaultStreamedContent(inputStream, "application/xlsx","modelo-usuario.xlsx");
		return modeloArquivoExcel;
	}
	
	public StreamedContent getModeloArquivoTxt() throws FileNotFoundException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		String caminhoTipo = facesContext.getExternalContext().getRealPath("/resources/modelo-txt/modelo-usuario.txt");
		InputStream inputStream = new FileInputStream(caminhoTipo);
		modeloArquivoTxt = new DefaultStreamedContent(inputStream, "application/txt","modelo-usuario.txt");
		return modeloArquivoTxt;
	}
	
	public StreamedContent getErros() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		String caminhoTipo = facesContext.getExternalContext().getRealPath("/resources/erros-lote/erros-lote-usuarios.txt");
		File file = new File(caminhoTipo);
		
		if(!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write("Lista de erros abaixo, por favor verifique eles. \n");
		for(String msg : mensagens) {
			fileWriter.write("\n" + msg + "\n");
		}
		fileWriter.flush();
		fileWriter.close();
		
		InputStream inputStream = new FileInputStream(caminhoTipo);
		erros = new DefaultStreamedContent(inputStream, "application/txt","erros-lote-usuarios.txt");
		
		return erros;
	}
	
	public byte[] toByteArrays(InputStream inputStream) throws IOException{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int reads = inputStream.read();
		while (reads != -1) {
			byteArrayOutputStream.write(reads);
			reads = inputStream.read();
		}
		return byteArrayOutputStream.toByteArray();
	}

}
