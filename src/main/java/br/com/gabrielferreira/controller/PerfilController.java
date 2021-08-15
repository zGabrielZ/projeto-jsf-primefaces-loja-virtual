package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.service.PerfilService;
import lombok.Setter;

@Named
@ViewScoped
public class PerfilController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PerfilService perfilService;
	
	@Setter
	private List<Perfil> perfils;
	
	@PostConstruct
	public void inicializar() {
		perfils = new ArrayList<Perfil>();
	}
	
	public List<Perfil> getPerfils(){
		perfils = perfilService.listaPerfis();
		return perfils;
	}

}
