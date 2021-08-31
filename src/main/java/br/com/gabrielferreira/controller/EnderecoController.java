package br.com.gabrielferreira.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Endereco;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.EnderecoService;
import br.com.gabrielferreira.utils.FacesMessages;
import br.com.gabrielferreira.utils.SessionUtil;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class EnderecoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EnderecoService enderecoService;
	
	@Getter
	@Setter
	private Endereco endereco;
	
	@Getter
	@Setter
	private boolean disabledCampos;
	
	@Getter
	@Setter
	private boolean consultaCep;
	
	@PostConstruct
	public void inicializar() {
		endereco = new Endereco();
		disabledCampos = true;
		consultaCep = false;
	}
	
	public void consultarCep() {
		try {
			String cep = endereco.getCep();
			Endereco novoEndereco = enderecoService.verificarEndereco(cep);
			endereco = getEndereco(novoEndereco,endereco.getId());
			disabledCampos = false;
			consultaCep = true;
		} catch (RegraDeNegocioException | IOException e) {
			FacesMessages.adicionarMensagem("cadastroEnderecoForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
	}

	public void inserirOuAtualizarEndereco() {
		
		if(endereco.getId() == null) {
			inserirEndereco(endereco);
		} else {
			atualizarEndereco(endereco);
		}
	}
	
	public void inserirEndereco(Endereco endereco) {
		Usuario usuario = (Usuario) SessionUtil.getParam("usuario");
		enderecoService.inserirEnderecoAndUsuario(endereco, usuario);
		FacesMessages.adicionarMensagem("cadastroEnderecoForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
				null);
	}
	
	public void atualizarEndereco(Endereco endereco) {
		Usuario usuario = (Usuario) SessionUtil.getParam("usuario");
		endereco.setUsuario(usuario);
		enderecoService.atualizar(endereco);
		FacesMessages.adicionarMensagem("cadastroEnderecoForm:msg", FacesMessage.SEVERITY_INFO, "Atualizado com sucesso !",
				null);
	}
	
	public void carregarDados() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigoEndereco");
		if(id != null) {
			if(!consultaCep) {
				endereco = enderecoService.getDetalhe(Integer.parseInt(id));
			}
		}
	}
	
	private Endereco getEndereco(Endereco novoEndereco, Integer id) {
		Endereco endereco = new Endereco();
		endereco.setBairro(novoEndereco.getBairro());
		endereco.setComplemento(novoEndereco.getComplemento());
		endereco.setDdd(novoEndereco.getDdd());
		endereco.setIbge(novoEndereco.getIbge());
		endereco.setLocalidade(novoEndereco.getLocalidade());
		endereco.setLogradouro(novoEndereco.getLogradouro());
		endereco.setNumero(novoEndereco.getNumero());
		endereco.setUf(novoEndereco.getUf());
		endereco.setCep(novoEndereco.getCep());
		endereco.setId(id);
		return endereco;
	}

}
