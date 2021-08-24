package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.service.UsuarioService;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
public class LoginController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Getter
	@Setter
	private Usuario usuario;
	
	@Getter
	@Setter
	private Perfil perfil;
	
	@Getter
	@Setter
	private boolean passou;
	
	@PostConstruct
	private void inicializar() {
		usuario = new Usuario();
		perfil = new Perfil();
		usuario.setPerfil(perfil);
		passou = false;
	}
	
	public String logar() {
		List<Usuario> usuarios = usuarioService.verificarEmailAndSenha(usuario.getEmail(), usuario.getSenha());
		if(!usuarios.isEmpty()) {
			passou = true;
			usuario.setNome(usuarios.get(0).getNome());
			usuario.setPerfil(usuarios.get(0).getPerfil());
			return "/HomePrincipal.xhtml?faces-redirect=true";
		}
		FacesMessages.adicionarMensagem("loginForm:msg", FacesMessage.SEVERITY_ERROR, "Usuário e/ou senha inválidos !",
				null);
		return null;
	}
	
	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		passou = false;
		return "/login/Login.xhtml?faces-redirect=true";
	}

}
