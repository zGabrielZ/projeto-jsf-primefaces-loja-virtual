package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.repositorio.UsuarioRepositorio;
import br.com.gabrielferreira.utils.Transacional;

public class SaldoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioRepositorio usuarioRepositorio;
	
	@Inject
	private SaldoRepositorio saldoRepositorio;
	
	@Transacional
	public void inserir(Saldo saldo) {
		saldoRepositorio.inserir(saldo);
	}
	
	@Transacional
	public void atualizar(Saldo saldo) {
		saldoRepositorio.atualizar(saldo);
	}
	
	@Transacional
	public void inserirSaldoAndUsuario(Saldo saldo, Usuario usuario) {
		saldo.setUsuario(usuario);
		saldoRepositorio.inserir(saldo);
		
		usuario.getSaldos().add(saldo);
		usuarioRepositorio.atualizar(usuario);
		
	}
	
	@Transacional
	public void removerSaldo(Saldo saldo) {
		saldoRepositorio.remover(saldo);
	}
	
	public List<Saldo> getSaldosByUsuario(Integer id, int primeiroResultado, int quantidadeMaxima){
		return saldoRepositorio.getSaldosByUsuario(id, primeiroResultado, quantidadeMaxima);
	}
	
	public Integer quantidadeRegistro(Integer idUsuario) {
		return saldoRepositorio.quantidadeRegistro(idUsuario);
	}
	
	public Saldo getDetalhe(Integer id) {
		return saldoRepositorio.procurarPorId(id);
	}


}
