package br.com.gabrielferreira.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;

import com.google.gson.Gson;

import br.com.gabrielferreira.entidade.Endereco;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.EnderecoRepositorio;
import br.com.gabrielferreira.repositorio.UsuarioRepositorio;

public class EnderecoService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioRepositorio usuarioRepositorio;
	
	@Inject
	private EnderecoRepositorio enderecoRepositorio;
	
	public void inserir(Endereco endereco) {
		enderecoRepositorio.inserir(endereco);
	}
	
	public Endereco atualizar(Endereco endereco) {
		return enderecoRepositorio.atualizar(endereco);
	}
	
	public void inserirEnderecoAndUsuario(Endereco endereco, Usuario usuario) {
		endereco.setUsuario(usuario);
		enderecoRepositorio.inserir(endereco);
		
		usuario.setEndereco(endereco);
		usuarioRepositorio.atualizar(usuario);
	}
	
	public void removerEndereco(Endereco endereco) {
		enderecoRepositorio.deletarPorId(Endereco.class, endereco.getId());
	}
	
	public Endereco getDetalhe(Integer id) {
		return enderecoRepositorio.pesquisarPorId(id, Endereco.class);
	}
	
	public Endereco getEnderecoByIdUsuario(Integer idUsuario) {
		return enderecoRepositorio.procurarPorIdByUsuario(idUsuario);
	}
	
	public Endereco verificarEndereco(String cep) throws IOException, RegraDeNegocioException {
		String urlWebService = "https://viacep.com.br/ws/"+cep+"/json/"; 
		URL url = new URL(urlWebService);
		HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
		
		if(conexao.getResponseCode() != 200) {
			throw new RegraDeNegocioException("Não é possível seguir em frente com esse cep.");
		}
		
		BufferedReader rb = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String linha = null;
		while((linha = rb.readLine()) != null) {
			sb.append(linha);
		}
		
		if(sb.toString().contains("erro")) {
			throw new RegraDeNegocioException("Não é possível seguir em frente com esse cep, pois não é válido.");
		}
		
		Endereco endereco = new Gson().fromJson(sb.toString(), Endereco.class);
		rb.close();
		conexao.disconnect();
		
		return endereco;
	}


}
