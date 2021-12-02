package br.com.gabrielferreira.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.gabrielferreira.entidade.ArquivoUpload;
import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.enums.TipoArquivo;
import br.com.gabrielferreira.entidade.to.UsuarioLoteTo;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class UsuarioLoteTxtController extends AbstractControllerLote{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	public void uploadArquivo() {
		if (arquivoUploadDados.getContentType().equals("text/plain") && usuarios.isEmpty()) {
			try {
				byte[] bytesArquivo = toByteArray(arquivoUploadDados.getInputStream());

				arquivoUpload.setArquivo(bytesArquivo);
				arquivoUpload.setTipoArquivo(TipoArquivo.TXT);

				usuarioLote.clear();
				usuarioLoteIncorretos.clear();
				usuarioLoteCorretos.clear();

				usuarioLote = usuarioService.verificarArquivoTxtUpload(arquivoUploadDados);
				usuarioLoteIncorretos = usuarioLote.stream().filter(u -> !u.getUsuarioLoteErrosTos().isEmpty())
						.collect(Collectors.toList());
				usuarioLoteCorretos = usuarioLote.stream().filter(u -> u.getUsuarioLoteErrosTos().isEmpty())
						.collect(Collectors.toList());

				for (UsuarioLoteTo usuarioLote : usuarioLoteCorretos) {
					Usuario usuario = getUsuarioLoteToUsuario(usuarioLote);
					usuarios.add(usuario);
				}

			} catch (Exception e) {
				FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm", FacesMessage.SEVERITY_ERROR,
						"Erro ao importar, verifique o arquivo !", null);
			}

		} else {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm", FacesMessage.SEVERITY_ERROR,
					"Não é possível inserir em lote !", null);
		}
	}
	
	@Override
	public void cadastrarUsuarios() {
		salvarUsuarios();
		
		if(!usuarioLoteCorretos.isEmpty() && usuarioLoteIncorretos.isEmpty()) {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrados com sucesso !",
					null);
		} else if(!usuarioLoteCorretos.isEmpty() && !usuarioLoteIncorretos.isEmpty()) {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm:msg", FacesMessage.SEVERITY_WARN, "Alguns foram cadastros e outros não, baixe o arquivo para verificar os erros !",
					null);
		} else {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm:msg", FacesMessage.SEVERITY_ERROR, "Baixe o arquivo para verificar os erros !",
					null);
		}
		
		usuarios = new ArrayList<>();
		arquivoUpload = new ArquivoUpload();
	}
	
	@Override
	public void salvarUsuarios() {
		if(!usuarios.isEmpty()) {
			usuarioService.inserirUsuarioLote(usuarios);
			arqService.inserir(arquivoUpload);
		}
	}
	
	public StreamedContent getModeloArquivoDados() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		try {
			String caminhoTipo = facesContext.getExternalContext().getRealPath("/resources/modelo-txt/modelo-usuario.txt");
			InputStream inputStream = new FileInputStream(caminhoTipo);
			modeloArquivoDados = new DefaultStreamedContent(inputStream, "application/txt","modelo-usuario.txt");
		} catch (Exception e) {
			FacesMessages.adicionarMensagem("cadastroUsuarioLoteForm:msg", FacesMessage.SEVERITY_ERROR, "Erro ao gerar relatório !",
					null);
		}
		
		return modeloArquivoDados;
	}
	
	@Override
	public void gerarRelatorioUsuario() {
		if(!usuarioLoteIncorretos.isEmpty()) {
			usuarioService.gerarRelatorioErrosUsuario(usuarioLoteIncorretos);
		}
	}
	
	public void limpar() {
		iniciar();
	}
	
	private Usuario getUsuarioLoteToUsuario(UsuarioLoteTo usuarioLote){
		Usuario usuario = new Usuario();
		Perfil perfil = new Perfil();
		usuario.setNome(usuarioLote.getNome());
		usuario.setEmail(usuarioLote.getEmail());
		usuario.setCpf(usuarioLote.getCpf());
		usuario.setDataNascimento(usuarioLote.getDataNascimento());
		
		perfil.setId(usuarioLote.getIdPerfil());
		usuario.setPerfil(perfil);
		return usuario;
	}

}
