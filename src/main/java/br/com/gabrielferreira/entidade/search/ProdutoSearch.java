package br.com.gabrielferreira.entidade.search;

import java.time.LocalDate;

import br.com.gabrielferreira.entidade.Produto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoSearch extends Produto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nome;
	private Integer estoque;
	private LocalDate dataProducao;
}
