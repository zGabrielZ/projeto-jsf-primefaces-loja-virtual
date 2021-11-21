package br.com.gabrielferreira.email;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.gabrielferreira.email.config.EmailConfiguracao;

public class UsuarioEmail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmailConfiguracao emailConfiguracao;
	
	public void assuntoEmail(String emailDestino,Integer codigoGerado) {
		
		Email email = new Email();
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("<h1 style=\"text-align: center;\" > Troca de senha:</h1> <br/> ");
		stringBuilder.append("<p style=\"text-align: center;\" > E-mail : " + emailDestino +"</p> <br/> ");
		stringBuilder.append("<p style=\"text-align: center;\" > Código gerado : " + codigoGerado +"</p> <br/> ");
		
		email.getDestinatarios().add(emailDestino);
		email.setTitulo("Atualização da senha");
		email.setAssunto(stringBuilder.toString());
		emailConfiguracao.enviarEmail(email);
	}

}
