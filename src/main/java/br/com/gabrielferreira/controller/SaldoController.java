package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.datatablelazy.LazyDataTableModelSaldo;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.service.SaldoService;
import br.com.gabrielferreira.service.UsuarioService;
import br.com.gabrielferreira.utils.FacesMessages;
import br.com.gabrielferreira.utils.LoginJSF;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class SaldoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private SaldoService saldoService;
	
	@Inject
	private NavegacaoController navegacaoController;
	
	@Inject
	private LazyDataTableModelSaldo<Saldo> saldos;
	
	private Saldo saldo;
	
	private Usuario usuario;
	
	private Saldo saldoSelecionado;
	
	@PostConstruct
	public void inicializar() {
		saldo = new Saldo();
		verificarParametro();
	}
	
	private void verificarParametro() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idUsuarioCadastroSaldo = params.get("codigoUsuarioAdicionarSaldo");
		String idUsuarioConsultaSaldo = params.get("codigoUsuarioConsultarSaldo");
		String idCodigoSaldoAtualizar = params.get("codigoSaldoAtualizar");
		
		if(idUsuarioConsultaSaldo != null) {
			usuario = usuarioService.getDetalhe(Integer.parseInt(idUsuarioConsultaSaldo));
			saldos.setIdUsuario(Integer.parseInt(idUsuarioConsultaSaldo));
			saldos.load(0, 5, null, null, null);
		}
		
		if(idUsuarioCadastroSaldo != null) {
			usuario = usuarioService.getDetalhe(Integer.parseInt(idUsuarioCadastroSaldo));
		}
		
		if(idCodigoSaldoAtualizar != null) {
			saldo = saldoService.getDetalhe(Integer.parseInt(idCodigoSaldoAtualizar));
		}
	}
	
	public void inserirSaldo() {
		
		if(saldo.getId() == null) {
			inserirSaldoUsuario();
			novo();
		} else {
			atualizarSaldo(saldo);
		}
		
	}
	
	public void novo() {
		saldo = new Saldo();
	}
	
	private void inserirSaldoUsuario() {
		saldoService.inserirSaldoAndUsuario(saldo, usuario);
		String mensagem = "Cadastrado com sucesso";
		checarUsuarioLogado(mensagem);
	}
	
	private void atualizarSaldo(Saldo saldo) {
		saldoService.atualizar(saldo);
		String mensagem = "Atualizado com sucesso";
		checarUsuarioLogado(mensagem);
	}
	
	public void excluirSaldo() {
		try {
			Saldo saldo = saldoSelecionado;			
			saldoService.removerSaldo(saldo);
			String mensagem = "Deletado com sucesso";
			checarUsuarioLogado(mensagem);
		} catch (Exception e) {
			FacesMessages.adicionarMensagem("consultaSaldosForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível excluir, pois tem entidades relacionada !",
					"Não é possível excluir !");
		}
	}
	
	private void checarUsuarioLogado(String mensagem) {
		Usuario usuarioLogado = LoginJSF.getRecuperarUsuarioLogada();
		if(usuarioLogado.getPerfil().getId().equals(2) || usuarioLogado.getPerfil().getId().equals(3)) {
			FacesMessages.adicionarMensagem("frmHome:msg", FacesMessage.SEVERITY_INFO,mensagem + ", " + usuarioLogado.getNome(),
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.home();
		} else {
			FacesMessages.adicionarMensagem("consultaUsuariosForm:msg", FacesMessage.SEVERITY_INFO,mensagem + ", " + usuarioLogado.getNome(),
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.consultaUsuario();
		}
	}

}
