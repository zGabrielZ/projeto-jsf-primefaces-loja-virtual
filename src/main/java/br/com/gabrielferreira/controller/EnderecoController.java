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
import br.com.gabrielferreira.utils.LoginJSF;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class EnderecoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EnderecoService enderecoService;

	private Endereco endereco;
	
	private boolean disabledCampos;
	
	private boolean consultaCep;
	
	@PostConstruct
	public void inicializar() {
		endereco = new Endereco();
		disabledCampos = true;
		consultaCep = false;
		verificarParametro();
	}
	
	private void verificarParametro() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idCodigoUsuario = params.get("codigoUsuarioEndereco");
		if(idCodigoUsuario != null) {
			if(!consultaCep) {
				Endereco enderecoConsulta = enderecoService.getEnderecoByIdUsuario(Integer.parseInt(idCodigoUsuario));
				if(enderecoConsulta != null) {
					this.endereco = enderecoConsulta;
				}
			}
		}
	}
	
	public void consultarCep() {
		try {
			String cep = endereco.getCep();
			Endereco novoEndereco = enderecoService.verificarEndereco(cep);
			endereco = getEnderecoConsulta(novoEndereco,endereco.getId());
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
		Usuario usuario = LoginJSF.getRecuperarUsuarioLogada();
		enderecoService.inserirEnderecoAndUsuario(endereco, usuario);
		FacesMessages.adicionarMensagem("cadastroEnderecoForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
				null);
	}
	
	public void atualizarEndereco(Endereco endereco) {
		Usuario usuario = LoginJSF.getRecuperarUsuarioLogada();
		endereco.setUsuario(usuario);
		enderecoService.atualizar(endereco);
		FacesMessages.adicionarMensagem("cadastroEnderecoForm:msg", FacesMessage.SEVERITY_INFO, "Atualizado com sucesso !",
				null);
	}
	
	private Endereco getEnderecoConsulta(Endereco novoEndereco, Integer id) {
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
