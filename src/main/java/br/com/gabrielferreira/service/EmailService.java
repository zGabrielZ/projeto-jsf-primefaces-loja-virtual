package br.com.gabrielferreira.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.gabrielferreira.config.EmailConfig;

public class EmailService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EmailConfig emailConfig;
	
	public void enviarCodigo(String destinatario, String assunto, int codigoGerado) {
		emailConfig.enviarEmail(destinatario, assunto);
	}

}
