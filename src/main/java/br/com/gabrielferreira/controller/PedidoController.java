package br.com.gabrielferreira.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.gabrielferreira.datatablelazy.LazyDataTableModelProduto;
import br.com.gabrielferreira.entidade.Itens;
import br.com.gabrielferreira.entidade.Parcela;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.service.ItensService;
import br.com.gabrielferreira.service.ParcelaService;
import br.com.gabrielferreira.service.PedidoService;
import br.com.gabrielferreira.utils.FacesMessages;
import br.com.gabrielferreira.utils.LoginJSF;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
public class PedidoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PedidoService pedidoService;
	
	@Inject
	private ItensService itensService;
	
	@Inject
	private ParcelaService parcelaService;
	
	@Inject
	private LazyDataTableModelProduto<Produto> produtos;
	
	private Produto produtoSelecionado;
	
	private Itens itensSelecionado;
	
	private List<Itens> pedidos;

	private List<Parcela> parcelas;
	
	private int quantidadeParcelas;
	
	@PostConstruct
	public void inicializar() {
		pedidos = new ArrayList<Itens>();
		parcelas = new ArrayList<>();
	}
	
	public void consultarProdutos() {
		produtos.setProdutoSearch(null);
		produtos.load(0,5,null,null,null);
	}
	
	public void addCarrinho() {
		try {
			BigDecimal valorNovo = getValorTotalPedido().add(produtoSelecionado.getValor());
			itensService.adicionarCarrinhoItens(produtoSelecionado,pedidos,parcelas,quantidadeParcelas,valorNovo);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("consultaPedidosForm:msg", FacesMessage.SEVERITY_ERROR,
					e.getMessage(), null);
		}
	}
	
	public void gerarParcelas() {
		try {
			parcelaService.gerarParcelasPedido(parcelas, quantidadeParcelas, getValorTotalPedido());
			FacesMessages.adicionarMensagem("consultaPedidosForm:msg", FacesMessage.SEVERITY_INFO,
					"Parcelas geradas com sucesso !!", null);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("consultaPedidosForm:msg", FacesMessage.SEVERITY_ERROR,
					e.getMessage(), null);
		}
	}
	
	public void excluirItemPedido() {
		try {
			BigDecimal valorNovo = getValorTotalPedido().subtract(itensSelecionado.getSubTotal());
			itensService.removerCarrinhoItens(itensSelecionado, pedidos, parcelas, quantidadeParcelas, valorNovo);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("consultaPedidosForm:msg", FacesMessage.SEVERITY_ERROR,
					e.getMessage(), null);
		}
	}
	
	public BigDecimal getValorTotalPedido() {
		return pedidoService.valorTotalPedido(pedidos);
	}
	
	public BigDecimal getSaldoAtual() {
		Usuario usuario = LoginJSF.getRecuperarUsuarioLogada();
		return usuario.getSaldoTotal();
	}
	
	public void comprarPedido() {
		try {
			pedidoService.inserirPedido(parcelas, pedidos);
			FacesMessages.adicionarMensagem("consultaPedidosForm:msg", FacesMessage.SEVERITY_INFO,
					"Compra feita com sucesso !!", null);
		} catch (RegraDeNegocioException e) {
			FacesMessages.adicionarMensagem("consultaPedidosForm:msg", FacesMessage.SEVERITY_ERROR,
					e.getMessage(), null);
		}
	}
}
