package br.com.gabrielferreira.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.service.UsuarioService;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
public class LoginController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	private Usuario usuario;
	
	@PostConstruct
	private void inicializar() {
		usuario = new Usuario();
	}
	
	public String logar() {
		Usuario usuarioLogado = usuarioService.getVerificarEmailSenhaLogin(usuario.getEmail(), usuario.getSenha());
		if(usuarioLogado != null) {
			HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			this.usuario = usuarioLogado;
			httpSession.setAttribute("usuarioLogado", usuarioLogado);
			return "/HomePrincipal.xhtml?faces-redirect=true";
			
		}
		FacesMessages.adicionarMensagem("loginForm:msg", FacesMessage.SEVERITY_ERROR, "Usuário e/ou senha inválidos !",
				null);
		return null;
	}
	
	public String logout() {
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		httpSession.removeAttribute("usuarioLogado");
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/login/Login.xhtml?faces-redirect=true";
	}
}
