package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.entidade.Cliente;
import br.com.gabrielferreira.entidade.Saldo;
import br.com.gabrielferreira.service.ClienteService;
import br.com.gabrielferreira.service.SaldoService;
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
	private ClienteService clienteService;
	
	@Inject
	private SaldoService saldoService;
	
	@Getter
	@Setter
	private Saldo saldo;
	
	@Getter
	@Setter
	private List<Saldo> saldos;
	
	@Getter
	@Setter
	private Cliente cliente;
	
	@PostConstruct
	public void inicializar() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("codigo");
		cliente = clienteService.getDetalhe(Integer.parseInt(id));
		saldo = new Saldo();
		saldos = saldoService.getSaldosByCliente(Integer.parseInt(id));
	}
	
	public void inserirSaldo() {
		
		if(saldo.getId() == null) {
			inserirSaldo(cliente, saldo);
			saldo = new Saldo();
		} else {
			// atualizar
		}
		
	}
	
	private void inserirSaldo(Cliente cliente, Saldo saldo) {
		saldoService.inserirSaldoAndCliente(saldo, cliente);
		FacesMessages.adicionarMensagem("cadastroSaldoForm:msg", FacesMessage.SEVERITY_INFO, "Cadastrado com sucesso !",
				null);
	}

}
