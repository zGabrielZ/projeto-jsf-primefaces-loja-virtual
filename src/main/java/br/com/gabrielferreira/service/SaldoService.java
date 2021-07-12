package br.com.gabrielferreira.service;

import java.io.Serializable;
import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.utils.Transacional;

public class SaldoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SaldoRepositorio saldoRepositorio;
	
	@Transacional
	public void inserir(Saldo saldo) {
		saldoRepositorio.inserir(saldo);
	}

}
