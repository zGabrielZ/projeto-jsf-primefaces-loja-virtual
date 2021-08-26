package br.com.gabrielferreira.entidade.search;

import br.com.gabrielferreira.entidade.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaSearch extends Categoria{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nome;

}
