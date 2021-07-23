package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Cliente;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.entidade.search.ClienteSearch;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.ClienteRepositorio;
import br.com.gabrielferreira.utils.Transacional;

public class ClienteService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private SaldoService saldoService;
	
	@Inject
	private ClienteRepositorio clienteRepositorio;
	
	@Transacional
	public void inserir(Cliente cliente, List<Saldo> saldos) throws RegraDeNegocioException {
		
		verificarEmailCliente(cliente.getEmail());
		verificarCpfCliente(cliente.getCpf());
		clienteRepositorio.inserir(cliente);
		
		if(!saldos.isEmpty()) {
			for(Saldo saldo : saldos) {
				
				saldo.setCliente(cliente);
				cliente.getSaldos().add(saldo);
				
				saldoService.inserir(saldo);
				atualizarClienteSaldo(cliente);
				
			}
		}
		
	}
	
	@Transacional
	public void inserirClienteLote(List<Cliente> clientes) {
		for(Cliente cliente : clientes) {
			clienteRepositorio.inserir(cliente);
		}
	}
	
	@Transacional
	public void atualizarCliente(Cliente cliente) throws RegraDeNegocioException {
		verificarEmailClienteAtualizado(cliente.getEmail(), cliente.getId());
		clienteRepositorio.atualizar(cliente);
	}
	
	@Transacional
	public void removerCliente(Cliente cliente) {
		clienteRepositorio.remover(cliente);
	}
	
	@Transacional
	public void atualizarClienteSaldo(Cliente cliente) {
		clienteRepositorio.atualizar(cliente);
	}
	
	public Cliente getDetalhe(Integer id) {
		return clienteRepositorio.procurarPorId(id);
	}
	
	public List<Cliente> getFiltrar(ClienteSearch clienteSearch){
		List<Cliente> clientes = clienteRepositorio.filtrar(clienteSearch);
		return clientes;
	}
	
	public void verificarCpfCliente(String cpf) throws RegraDeNegocioException {
		if(clienteRepositorio.verificarCpf(cpf)) {
			throw new RegraDeNegocioException("Não é possível cadastrar este cpf " + cpf + ", pois já está cadastrado.");
		}
	}
	
	public void verificarEmailCliente(String email) throws RegraDeNegocioException {
		if(clienteRepositorio.verificarEmail(email)) {
			throw new RegraDeNegocioException("Não é possível cadastrar este e-mail " + email + ", pois já está cadastrado.");
		}
	}
	
	public void verificarEmailClienteAtualizado(String email, Integer id) throws RegraDeNegocioException {
		if(clienteRepositorio.verificarEmailAtualizado(email, id)) {
			throw new RegraDeNegocioException("Não é possível atualizar este e-mail " + email + ", pois já está cadastrado.");
		}
	}
	

}
