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
	
	public void consultaParcelas() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/pedido/consulta/Parcelas.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void consultaPedidoConfirmados() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/pedido/consulta/PedidosConfirmados.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void consultaPedido() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/pedido/consulta/Pedidos.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void login() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/login/Login.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void cadastrarUsuarioLogin() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/login/CadastroLogin.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void atualizarSenha() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	    	 externalContext.redirect(externalContext.getRequestContextPath()
		                + "/login/AtualizarLoginSenha.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void cadastroProduto() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/produto/cadastro/CadastroProduto.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void consultaProduto() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/produto/consulta/ConsultaProduto.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void consultaCategoria() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/categoria/consulta/ConsultaCategoria.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void cadastrarCategoria() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/categoria/cadastro/CadastroCategoria.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void consultaUsuario() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/usuario/consulta/ConsultaUsuario.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void cadastrarUsuario() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/usuario/cadastro/CadastroUsuario.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void cadastrarUsuarioLoteTxt() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/usuario/lote/CadastroUsuarioLoteTxt.xhtml");
	    } catch (IOException e) {
	          e.printStackTrace();
	    }
	}
	
	public void cadastrareUsuarioLoteExcel() {
	    ExternalContext externalContext = getExternalContext();
	    try {
	          externalContext.redirect(externalContext.getRequestContextPath()
	                + "/usuario/lote/CadastroUsuarioLoteExcel.xhtml");
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
