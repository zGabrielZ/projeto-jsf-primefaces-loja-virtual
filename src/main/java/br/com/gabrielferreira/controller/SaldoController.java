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
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
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
	
	@Getter
	@Setter
	private String tituloCadastroSaldo;
	
	@Getter
	@Setter
	private Saldo saldo;
	
	@Inject
	@Getter
	private LazyDataTableModelSaldo<Saldo> saldos;
	
	@Getter
	@Setter
	private Usuario usuario;
	
	@Getter
	@Setter
	private Saldo saldoSelecionado;
	
	@PostConstruct
	public void inicializar() {
		tituloCadastroSaldo = "Cadastro Saldo";
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		if(id != null) {
			usuario = usuarioService.getDetalhe(Integer.parseInt(id));
			saldos.setIdUsuario(Integer.parseInt(id));
			saldos.load(0, 5, null, null, null);
		}
		saldo = new Saldo();
	}
	
	public void inserirSaldo() {
		
		if(saldo.getId() == null) {
			inserirSaldo(usuario, saldo);
			saldo = new Saldo();
		} else {
			atualizarSaldo(saldo);
		}
		
	}
	
	private void inserirSaldo(Usuario usuario, Saldo saldo) {
		saldoService.inserirSaldoAndUsuario(saldo, usuario);
		FacesMessages.adicionarMensagem("consultaUsuariosForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
				null);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		navegacaoController.consultaUsuario();
	}
	
	private void atualizarSaldo(Saldo saldo) {
		saldoService.atualizar(saldo);
		FacesMessages.adicionarMensagem("consultaUsuariosForm:msg", FacesMessage.SEVERITY_INFO, "Atualizado com sucesso !",
				null);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		navegacaoController.consultaUsuario();
	}
	
	public void excluirSaldo() {
		try {
			Saldo saldo = saldoSelecionado;			
			saldoService.removerSaldo(saldo);
			FacesMessages.adicionarMensagem("consultaUsuariosForm:msg", FacesMessage.SEVERITY_INFO, "Removido com sucesso !",
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.consultaUsuario();
		} catch (Exception e) {
			FacesMessages.adicionarMensagem("consultaSaldosForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível excluir, pois tem entidades relacionada !",
					"Não é possível excluir !");
		}
	}
	
	public void carregarDados() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idUsuario = params.get("codigoUsuario");
		String idSaldo = params.get("codigoSaldo");
		if(idSaldo != null && idUsuario != null) {
			saldo = saldoService.getDetalhe(Integer.parseInt(idSaldo));
			tituloCadastroSaldo = "Atualizar Saldo";
		}
	}
	
	public String selecionarSaldoAtualizar(Saldo saldo) {
		this.saldo = saldo;
		return "/saldo/cadastro/CadastroSaldo?faces-redirect=true&codigoUsuario="+usuario.getId()+"&codigoSaldo="+this.saldo.getId();
	}

}
