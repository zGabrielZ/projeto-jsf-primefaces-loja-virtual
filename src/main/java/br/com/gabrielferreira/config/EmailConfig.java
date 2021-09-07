package br.com.gabrielferreira.config;

import java.io.Serializable;

public class EmailConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void enviarEmail(String destinatario,String assunto) {
		System.out.println("Enviar email");
	}

}
