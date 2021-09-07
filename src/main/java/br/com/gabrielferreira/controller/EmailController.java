package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.Random;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.service.EmailService;
import br.com.gabrielferreira.service.UsuarioService;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EmailController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String ASSUNTO = "Atualização da senha";

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private EmailService emailService;
	
	@Inject
	private NavegacaoController navegacaoController;
	
	@Getter
	@Setter
	private String codigoEmail;
	
	@Getter
	@Setter
	private Integer codigoGerado;
	
	@Getter
	@Setter
	private String emailConsulta;
	
	@Getter
	@Setter
	private boolean passouEmailCorreto;
	
	@Getter
	@Setter
	private Usuario usuario;
	
	@Getter
	@Setter
	private String mensagem;
	
	public void inicializar() {
		usuario = new Usuario();
		passouEmailCorreto = false;
	}
	
	public void enviarEmail() {
		emailService.enviarCodigo(usuario.getEmail(),ASSUNTO,codigoGerado);
	}
	
	public void consultarEmail() {
		if(usuarioService.verificarEmailLogin(emailConsulta)) {
			passouEmailCorreto = true;
			usuario = usuarioService.procurarEmail(emailConsulta);
			codigoGerado = getGerarNumeroAleatorio();
			enviarEmail();
			System.out.println("Código gerado via email, " + codigoGerado);
			mensagem = "E-mail encontrado e o código gerado foi para o seu e-mail !";
		} else {
			mensagem = "E-mail não encontrado.";
		}
	}
	
	public void inserirSenhaUsuario() {
		if(!codigoEmail.equals(codigoGerado.toString())) {
			mensagem = "Código incorreto !!";
		} else {
			usuarioService.atualizarSenhaUsuario(usuario.getId(), usuario.getSenha());
			FacesMessages.adicionarMensagem("loginForm:msg", FacesMessage.SEVERITY_INFO, "Senha atualizada com sucesso !",
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.login();
		}
	}
	
	public Integer getGerarNumeroAleatorio() {
		Random numeroAleatorio = new Random();
		int valor = numeroAleatorio.nextInt(100) + 1;
		return valor;
	}
}
