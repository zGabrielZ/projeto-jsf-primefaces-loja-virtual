package br.com.gabrielferreira.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import org.primefaces.model.StreamedContent;

import br.com.gabrielferreira.entidade.ArquivoUpload;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.to.UsuarioLoteTo;
import br.com.gabrielferreira.service.ArquivoUploadService;
import br.com.gabrielferreira.service.UsuarioService;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public abstract class AbstractControllerLote implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	protected ArquivoUploadService arqService;
	
	@Inject
	protected UsuarioService usuarioService;
	
	protected StreamedContent modeloArquivoDados;
	
	protected Part arquivoUploadDados;
	
	protected ArquivoUpload arquivoUpload;
	
	protected List<Usuario> usuarios;
	
	protected List<UsuarioLoteTo> usuarioLote;
	
	protected List<UsuarioLoteTo> usuarioLoteIncorretos;
	
	protected List<UsuarioLoteTo> usuarioLoteCorretos;
	
	@PostConstruct
	public void iniciar() {
		arquivoUpload = new ArquivoUpload();
		usuarios = new ArrayList<Usuario>();
		usuarioLote = new ArrayList<>();
		usuarioLoteIncorretos = new ArrayList<>();
		usuarioLoteCorretos = new ArrayList<>();
	}
	
	protected byte[] toByteArray(InputStream inputStream) throws IOException{
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int reads = inputStream.read();
		while (reads != -1) {
			byteArrayOutputStream.write(reads);
			reads = inputStream.read();
		}
		return byteArrayOutputStream.toByteArray();
	}
	
	public abstract void uploadArquivo();
	
	public abstract void cadastrarUsuarios();
	
	public abstract void salvarUsuarios();
	
	public abstract void gerarRelatorioUsuario();

}
