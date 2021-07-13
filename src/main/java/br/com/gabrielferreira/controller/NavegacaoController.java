package br.com.gabrielferreira.controller;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class NavegacaoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	public void consultaCliente() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/cliente/consulta/ConsultaCliente.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void cadastrarCliente() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/cliente/cadastro/CadastroCliente.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void home() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/HomePrincipal.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
}
