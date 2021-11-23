package br.com.gabrielferreira.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.gabrielferreira.entidade.Usuario;

public class LoginJSF {

	public static Usuario getRecuperarUsuarioLogada() {
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(false);
		Usuario usuario = (Usuario) httpSession.getAttribute("usuarioLogado");
		return usuario;
	}
}
