package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.email.UsuarioEmail;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.UsuarioService;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class EmailController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private UsuarioEmail usuarioEmail;
	
	@Inject
	private NavegacaoController navegacaoController;
	
	private Usuario usuario;
	
	private boolean codigoEnviado;
	
	private Integer codigo;
	
	private String senha;
	
	@PostConstruct
	public void inicializar() {
		codigoEnviado = false;
		usuario = new Usuario();
	}
	
	public void enviarCodigo() {
		try {
			usuarioService.verificarEmailTrocarSenha(usuario);
			codigoEnviado = true;
			
			String email = usuario.getEmail();
			Integer codigoGerado = gerarCodigoAleatorio();
			
			usuario.setCodigoSenhaGerado(codigoGerado);
			usuarioEmail.assuntoEmail(email, codigoGerado);
			
			FacesMessages.adicionarMensagem("updateUsuarioForm:msg", FacesMessage.SEVERITY_INFO, "Código enviado com sucesso !",
					null);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("updateUsuarioForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
	}
	
	private Integer gerarCodigoAleatorio() {
		Random random = new Random();
		Integer valor = random.nextInt(50) + 1; // 1 até 50
		return valor;
	}
	
	public void atualizarSenha() {
		if(codigo.equals(usuario.getCodigoSenhaGerado())) {
			Usuario usuarioEncontrado = usuarioService.getEmailUsuario(usuario.getEmail());
			usuarioEncontrado.setSenha(senha);
			
			usuarioService.atualizarSenhaUsuario(usuarioEncontrado);
			
			FacesMessages.adicionarMensagem("loginUsuarioForm:msg", FacesMessage.SEVERITY_INFO, "Senha atualizada sucesso !",
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.login();
		} else {
			FacesMessages.adicionarMensagem("updateUsuarioForm:msg", FacesMessage.SEVERITY_ERROR, "Código incorreto !",
					null);
		}
	}
	
	
}
