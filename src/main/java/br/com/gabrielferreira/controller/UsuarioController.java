package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.datatablelazy.LazyDataTableModelUsuario;
import br.com.gabrielferreira.entidade.Perfil;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.search.UsuarioSearch;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.UsuarioService;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class UsuarioController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private NavegacaoController navegacaoController;
	
	@Inject
	private LazyDataTableModelUsuario<Usuario> usuarios;
	
	private String tituloCadastroUsuario;
	
	private Usuario usuario;
	
	private Usuario usuarioDetalhe;
	
	private Saldo saldo;
	
	private boolean desejoSaldo;
	
	private boolean saldoNaoAtualizar;

	private List<Saldo> saldos;
	
	private UsuarioSearch usuarioSearch;
	
	private Saldo saldoSelecionado;
	
	private Usuario usuarioSelecionado;
	
	@PostConstruct
	public void inicializar() {
		tituloCadastroUsuario = "Cadastro de Usuário";
		usuarioSearch = new UsuarioSearch();
		usuario = new Usuario();
		saldo = new Saldo();
		saldos = new ArrayList<Saldo>();
		desejoSaldo = false;
		verificarParametro();
	}
	
	public void verificarParametro() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String idDetalheUsuario = params.get("codigoDetalheUsuario");
		if(idDetalheUsuario != null) {
			usuarioDetalhe = usuarioService.getDetalhe(Integer.parseInt(idDetalheUsuario));
		}
	}
	
	public void consultarUsuario() {
		usuarios.setUsuarioSearch(usuarioSearch);
		usuarios.load(0, 5,null,null,null);
	}
	
	public void removerSaldo() {
		saldos.remove(saldoSelecionado);
	}
	
	public void addSaldo() {
		saldos.add(saldo);
		saldo = new Saldo();
	}
	
	public void inserirOuAtualizarUsuario() {
		
		if(usuario.getId() == null) {
			boolean inserir = inserirUsuario(usuario);
			if(inserir) {
				usuario = new Usuario();
			}
		} else {
			atualizarUsuario(usuario);
		}
		desejoSaldo = false;
	}
	
	public boolean inserirUsuario(Usuario usuario) {
		boolean inserir = true;
		try {
			usuarioService.inserir(usuario,saldos);
			FacesMessages.adicionarMensagem("cadastroUsuarioForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
					null);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("cadastroUsuarioForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
			inserir = false;
		}
		return inserir;
	}
	
	public void atualizarUsuario(Usuario usuario) {
		try {
			usuarioService.atualizarUsuario(usuario);
			FacesMessages.adicionarMensagem("consultaUsuariosForm:msg", FacesMessage.SEVERITY_INFO, "Atualizado com sucesso !",
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.consultaUsuario();
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("cadastroUsuarioForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
	}
	
	public void excluirUsuario() {
		try {
			Usuario usuario = usuarioSelecionado;			
			usuarioService.removerUsuario(usuario);
			consultarUsuario();
			FacesMessages.adicionarMensagem("consultaUsuariosForm:msg", FacesMessage.SEVERITY_INFO, "Removido com sucesso !",
					null);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("consultaUsuariosForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					"Não é possível excluir !");
		} catch (Exception e) {
			FacesMessages.adicionarMensagem("consultaUsuariosForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível excluir, pois tem entidades relacionada !",
					"Não é possível excluir !");
		}
	}
	
	public void inserirUsuarioCliente() {
		Perfil perfil = new Perfil();
		perfil.setId(3);
		try {
			usuario.setPerfil(perfil);
			usuarioService.inserir(usuario, saldos);
			FacesMessages.adicionarMensagem("loginForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
					null);
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
			navegacaoController.login();
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("cadastroUsuarioForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
	}
	
	public void limparPesquisa() {
		usuarioSearch = new UsuarioSearch();
		consultarUsuario();
	}
	
	public String selecionarUsuarioSaldo(Usuario usuario) {
		this.usuario = usuario;
		return "/saldo/cadastro/CadastroSaldo?faces-redirect=true&codigo="+this.usuario.getId();
	}
	
	public String selecionarUsuarioConsultaSaldo(Usuario usuario) {
		this.usuario = usuario;
		return "/saldo/consulta/ConsultaSaldo?faces-redirect=true&codigo="+this.usuario.getId();
	}
	
	public String selecionarUsuarioAtualizar(Usuario usuario) {
		this.usuario = usuario;
		return "/usuario/cadastro/CadastroUsuario?faces-redirect=true&codigo="+this.usuario.getId();
	}
	
	public void carregarDados() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		if(id != null) {
			usuario = usuarioService.getDetalhe(Integer.parseInt(id));
			saldoNaoAtualizar = true;
			tituloCadastroUsuario = "Atualizar Usuário";
		}
	}
	
	public void novo() {
		usuario = new Usuario();
	}
	
	public void limparFormularioUsuario() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("cadastroUsuarioForm:nome");
		HtmlInputText htmlInputTextEmail = (HtmlInputText) uiViewRoot.findComponent("cadastroUsuarioForm:email");
		HtmlInputText htmlInputTextCpf = (HtmlInputText) uiViewRoot.findComponent("cadastroUsuarioForm:cpf");
		HtmlInputText htmlInputTextDataNascimento = (HtmlInputText) uiViewRoot.findComponent("cadastroUsuarioForm:data");
		HtmlSelectBooleanCheckbox htmlSelectBooleanCheckboxSaldo = (HtmlSelectBooleanCheckbox) uiViewRoot.findComponent("cadastroUsuarioForm:saldo");
		HtmlSelectOneMenu htmlSelectOneMenuPefil = (HtmlSelectOneMenu) uiViewRoot.findComponent("cadastroUsuarioForm:perfil");
		htmlInputTextNome.setSubmittedValue("");
		htmlInputTextEmail.setSubmittedValue("");
		htmlInputTextCpf.setSubmittedValue("");
		htmlInputTextDataNascimento.setSubmittedValue("");
		htmlSelectBooleanCheckboxSaldo.setSubmittedValue("");
		htmlSelectOneMenuPefil.setSubmittedValue("");
		saldos.clear();
		saldo = new Saldo();
		usuario = new Usuario();
		desejoSaldo = false;
	}

}
