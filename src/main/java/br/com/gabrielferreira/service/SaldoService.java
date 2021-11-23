package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.repositorio.SaldoRepositorio;
import br.com.gabrielferreira.repositorio.UsuarioRepositorio;

public class SaldoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioRepositorio usuarioRepositorio;
	
	@Inject
	private SaldoRepositorio saldoRepositorio;
	
	public void inserir(Saldo saldo) {
		saldoRepositorio.inserir(saldo);
	}
	
	public Saldo atualizar(Saldo saldo) {
		return saldoRepositorio.atualizar(saldo);
	}
	
	public void inserirSaldoAndUsuario(Saldo saldo, Usuario usuario) {
		saldo.setUsuario(usuario);
		saldoRepositorio.inserir(saldo);
		
		usuario.getSaldos().add(saldo);
		usuarioRepositorio.atualizar(usuario);
		
	}
	
	public void removerSaldo(Saldo saldo) {
		saldoRepositorio.deletarPorId(Saldo.class, saldo.getId());
	}
	
	public List<Saldo> getSaldosByUsuario(Integer id, int primeiroResultado, int quantidadeMaxima){
		return saldoRepositorio.getSaldosByUsuario(id, primeiroResultado, quantidadeMaxima);
	}
	
	public Integer quantidadeRegistro(Integer idUsuario) {
		return saldoRepositorio.quantidadeRegistro(idUsuario);
	}
	
	public Saldo getDetalhe(Integer id) {
		return saldoRepositorio.pesquisarPorId(id, Saldo.class);
	}


}
