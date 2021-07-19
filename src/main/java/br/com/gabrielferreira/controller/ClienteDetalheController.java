package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Cliente;
import br.com.gabrielferreira.service.ClienteService;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ClienteDetalheController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClienteService clienteService;
	
	@Getter
	@Setter
	private Cliente cliente;
	
	@PostConstruct
	public void inicializar() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		cliente = clienteService.getDetalhe(Integer.parseInt(id));
	}
}
