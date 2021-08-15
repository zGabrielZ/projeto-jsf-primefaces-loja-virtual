package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.service.UsuarioService;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class UsuarioDetalheController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Getter
	@Setter
	private Usuario usuario;
	
	@PostConstruct
	public void inicializar() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		usuario = usuarioService.getDetalhe(Integer.parseInt(id));
	}
}
