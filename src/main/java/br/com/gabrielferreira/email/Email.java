package br.com.gabrielferreira.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nomeRemetente;
	private String titulo;
	private String assunto;
	private List<String> destinatarios = new ArrayList<String>();

}
