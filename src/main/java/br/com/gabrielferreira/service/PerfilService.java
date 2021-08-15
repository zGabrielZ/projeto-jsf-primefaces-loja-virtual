package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.repositorio.PerfilRepositorio;

public class PerfilService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PerfilRepositorio perfilRepositorio;
	
	public List<Perfil> listaPerfis(){
		return perfilRepositorio.listaPerfis();
	}
	
	public Perfil procurarPorId(Integer id) {
		return perfilRepositorio.consultarPorId(id);
	}

}
