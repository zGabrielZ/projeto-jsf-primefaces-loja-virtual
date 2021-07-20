package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Cliente;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.repositorio.ClienteRepositorio;
import br.com.gabrielferreira.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.utils.Transacional;

public class SaldoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClienteRepositorio clienteRepositorio;
	
	@Inject
	private SaldoRepositorio saldoRepositorio;
	
	@Transacional
	public void inserir(Saldo saldo) {
		saldoRepositorio.inserir(saldo);
	}
	
	@Transacional
	public void inserirSaldoAndCliente(Saldo saldo, Cliente cliente) {
		saldo.setCliente(cliente);
		saldoRepositorio.inserir(saldo);
		
		cliente.getSaldos().add(saldo);
		clienteRepositorio.atualizar(cliente);
		
	}
	
	@Transacional
	public void removerSaldo(Saldo saldo) {
		saldoRepositorio.remover(saldo);
	}
	
	public List<Saldo> getSaldosByCliente(Integer id){
		return saldoRepositorio.getSaldosByCliente(id);
	}

}
