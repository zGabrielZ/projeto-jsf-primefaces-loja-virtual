package br.com.gabrielferreira.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.datatablelazy.LazyDataTableModelParcela;
import br.com.gabrielferreira.entidade.Parcela;
import lombok.Getter;

@Named
@ViewScoped
public class ParcelaController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	@Getter
	private LazyDataTableModelParcela<Parcela> parcelas;
	
	public void consultarParcelas() {
		parcelas.load(0,5,null,null,null);
	}
}
