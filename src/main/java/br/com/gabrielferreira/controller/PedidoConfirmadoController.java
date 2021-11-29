package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.datatablelazy.LazyDataTableModelItens;
import br.com.gabrielferreira.entidade.Itens;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.to.PedidoTo;
import br.com.gabrielferreira.service.PedidoService;
import br.com.gabrielferreira.utils.LoginJSF;
import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
@Getter
@Setter
public class PedidoConfirmadoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoService pedidoService;
	
	@Inject
	private LazyDataTableModelItens<Itens> itens;

	private List<PedidoTo> pedidos;
	
	public void consultarPedidos() {
		itens.load(0,5,null,null,null);
	}
	
	public List<PedidoTo> getPedidos() {
		Usuario usuario = LoginJSF.getRecuperarUsuarioLogada();
		pedidos = pedidoService.getValorTotalDeCadaPedidoCadastrado(usuario.getId());
		return pedidos;
	}
}
