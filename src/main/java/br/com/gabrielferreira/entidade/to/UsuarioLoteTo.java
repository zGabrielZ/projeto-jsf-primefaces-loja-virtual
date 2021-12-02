package br.com.gabrielferreira.entidade.to;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class UsuarioLoteTo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nome;
	private String email;
	private String cpf;
	private LocalDate dataNascimento;
	private Integer idPerfil;
	
	private Integer codigoUsuario;
	
	private List<UsuarioLoteErrosTo> usuarioLoteErrosTos = new ArrayList<UsuarioLoteErrosTo>();
	
	public String getPerfil() {
		if(idPerfil != null) {
			if(idPerfil.equals(1)) {
				return "Administrador";
			} else if(idPerfil.equals(2)) {
				return "Funcionário";
			} else if(idPerfil.equals(3)) {
				return "Cliente";
			} else {
				return "Valor inexistente";
			}
		}
		return null;
	}
}
