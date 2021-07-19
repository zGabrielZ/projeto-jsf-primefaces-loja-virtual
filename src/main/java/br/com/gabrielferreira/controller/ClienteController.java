package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Cliente;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.entidade.search.ClienteSearch;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.ClienteService;
import br.com.gabrielferreira.utils.FacesMessages;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class ClienteController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ClienteService clienteService;
	
	@Getter
	@Setter
	private Cliente cliente;
	
	@Getter
	@Setter
	private Saldo saldo;
	
	@Getter
	@Setter
	private boolean desejoSaldo;
	
	@Getter
	@Setter
	private List<Saldo> saldos;
	
	@Getter
	@Setter
	private List<Cliente> clientes;
	
	@Getter
	@Setter
	private ClienteSearch clienteSearch;
	
	@Getter
	@Setter
	private Saldo saldoSelecionado;
	
	@Getter
	@Setter
	private Cliente clienteSelecionado;
	
	@PostConstruct
	public void inicializar() {
		clienteSearch = new ClienteSearch();
		cliente = new Cliente();
		saldo = new Saldo();
		saldos = new ArrayList<Saldo>();
		desejoSaldo = false;
	}
	
	public void consultarCliente() {
		clientes = clienteService.getFiltrar(clienteSearch);
	}
	
	public void removerSaldo() {
		saldos.remove(saldoSelecionado);
	}
	
	public void addSaldo() {
		saldos.add(saldo);
		saldo = new Saldo();
	}
	
	public void inserirOuAtualizarCliente() {
		
		if(cliente.getId() == null) {
			inserirCliente(cliente);
			cliente = new Cliente();
		} else {
			atualizarCliente(cliente);
		}
		desejoSaldo = false;
	}
	
	public void inserirCliente(Cliente cliente) {
		try {
			clienteService.inserir(cliente,saldos);
			FacesMessages.adicionarMensagem("cadastroCliente:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
					null);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("cadastroClienteForm:msg", FacesMessage.SEVERITY_ERROR, e.getMessage(),
					null);
		}
	}
	
	public void atualizarCliente(Cliente cliente) {
		
	}
	
	public void excluirCliente() {
		try {
			Cliente cliente = clienteSelecionado;			
			clienteService.removerCliente(cliente);
			consultarCliente();
			FacesMessages.adicionarMensagem("consultaClientesForm:msg", FacesMessage.SEVERITY_INFO, "Removido com sucesso !",
					null);
		} catch (Exception e) {
			FacesMessages.adicionarMensagem("consultaClientesForm:msg", FacesMessage.SEVERITY_ERROR, "Não é possível excluir, pois tem entidades relacionada !",
					"Não é possível excluir !");
		}
	}
	
	public void limparPesquisa() {
		clienteSearch = new ClienteSearch();
	}
	
	public String selecionarClienteSaldo(Cliente cliente) {
		this.cliente = cliente;
		return "/saldo/cadastro/CadastroSaldo?faces-redirect=true&codigo="+this.cliente.getId();
	}
	
	public String selecionarClienteConsultaSaldo(Cliente cliente) {
		this.cliente = cliente;
		return "/saldo/consulta/ConsultaSaldo?faces-redirect=true&codigo="+this.cliente.getId();
	}
	public String selecionarClienteDetalhe(Cliente cliente) {
		this.cliente = cliente;
		return "/cliente/detalhe/DetalheCliente?faces-redirect=true&codigo="+this.cliente.getId();
	}
	
	public void limparFormularioCliente() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		HtmlInputText htmlInputTextNome = (HtmlInputText) uiViewRoot.findComponent("cadastroClienteForm:nome");
		HtmlInputText htmlInputTextEmail = (HtmlInputText) uiViewRoot.findComponent("cadastroClienteForm:email");
		HtmlInputText htmlInputTextCpf = (HtmlInputText) uiViewRoot.findComponent("cadastroClienteForm:cpf");
		HtmlInputText htmlInputTextDataNascimento = (HtmlInputText) uiViewRoot.findComponent("cadastroClienteForm:data");
		HtmlSelectBooleanCheckbox htmlSelectBooleanCheckboxSaldo = (HtmlSelectBooleanCheckbox) uiViewRoot.findComponent("cadastroClienteForm:saldo");
		htmlInputTextNome.setSubmittedValue("");
		htmlInputTextEmail.setSubmittedValue("");
		htmlInputTextCpf.setSubmittedValue("");
		htmlInputTextDataNascimento.setSubmittedValue("");
		htmlSelectBooleanCheckboxSaldo.setSubmittedValue("");
		saldos.clear();
		saldo = new Saldo();
		cliente = new Cliente();
		desejoSaldo = false;
	}

}
