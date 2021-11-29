package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Itens;
import br.com.gabrielferreira.entidade.Parcela;
import br.com.gabrielferreira.entidade.Pedido;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.entidade.Usuario;
import br.com.gabrielferreira.entidade.to.PedidoTo;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.EnderecoRepositorio;
import br.com.gabrielferreira.repositorio.PedidoRepositorio;
import br.com.gabrielferreira.utils.LoginJSF;

public class PedidoService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PedidoRepositorio pedidoRepositorio;
	
	@Inject
	private EnderecoRepositorio enderecoRepositorio;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private ItensService itensService;
	
	@Inject
	private UsuarioService usuarioService;
	
	public void remover(Pedido pedido) {
		pedidoRepositorio.deletarPorId(Pedido.class, pedido.getId());
	}
	
	public Pedido procurarPorId(Integer id) {
		return pedidoRepositorio.pesquisarPorId(id, Pedido.class);
	}
	
	public void inserirPedido(List<Parcela> parcelas,List<Itens> itens) throws RegraDeNegocioException {
		Usuario usuario = LoginJSF.getRecuperarUsuarioLogada();
		usuario.setPedidos(pedidoRepositorio.getPedidoByUsuarioId(usuario.getId()));
		verificarEstoqueProduto(itens);
		verificarSaldoAtualComPrimeiraParcela(usuario.getSaldoTotal(), parcelas);
		verificarEndereco(usuario);
		comprarPedido(parcelas,itens,usuario);
	}

	private void comprarPedido(List<Parcela> parcelas, List<Itens> itens, Usuario usuario) throws RegraDeNegocioException {
		Pedido pedido = new Pedido();
		pedido.setId(null);
		pedido.setCodigoPedido(getGerarNumeroAleatorio());
		pedido.setDataPedido(LocalDateTime.now());
		pedido.setUsuario(usuario);
		pedido.getParcelas().addAll(parcelas);
		
		for(Parcela p : parcelas) {
			p.setPedido(pedido);
		}
		
		pedidoRepositorio.inserir(pedido);
		
		for(Itens i : itens) {
			Produto produto = produtoService.procurarPorId(i.getProduto().getId());
			int estoqueAntigo = produto.getEstoque();
			int estoqueNovo = estoqueAntigo - i.getQuantidadeCompra();
			verificarProdutoEstoqueNovo(estoqueNovo,i.getProduto());
			
			produto.setEstoque(estoqueNovo);
			produto.getItens().add(i);
			
			i.setProduto(produto);
			i.setPedido(pedido);
			
		}
		
		usuario.getPedidos().add(pedido); 
		usuarioService.atualizarUsuario(usuario);
		
		itensService.inserir(itens);
		for(Itens i : itens) {
			produtoService.atualizar(i.getProduto());
		}
		
		pedido.getItens().addAll(itens);
		pedidoRepositorio.atualizar(pedido);
		
		// Apos a compra, é necessario limpar a parcela e itens
		parcelas.clear();
		itens.clear();
	}
	
	private Integer getGerarNumeroAleatorio() {
		Random numeroAleatorio = new Random();
		int valor = numeroAleatorio.nextInt(100) + 1;
		return valor;
	}
	
	private void verificarProdutoEstoqueNovo(int estoqueNovo, Produto produto) throws RegraDeNegocioException {
		if(estoqueNovo < 0) {
			throw new RegraDeNegocioException("Não é possível fazer a compra, pois o estoque do novo produto " + produto.getNome() + " acabou");
		}
	}
	
	private void verificarEstoqueProduto(List<Itens> itens) throws RegraDeNegocioException {
		for(Itens i : itens) {
			if(i.getProduto().getEstoque() < 0) {
				throw new RegraDeNegocioException("Não é possível fazer a compra, pois o estoque do produto " + i.getProduto().getNome() + " acabou");
			}
		}
	}
	
	private void verificarEndereco(Usuario usuario) throws RegraDeNegocioException {
		if(enderecoRepositorio.procurarPorIdByUsuario(usuario.getId()) == null) {
			throw new RegraDeNegocioException("Para continuar a compra, é necessário informar o endereço");
		}
	}

	private void verificarSaldoAtualComPrimeiraParcela(BigDecimal saldoAtual, List<Parcela> parcelas) throws RegraDeNegocioException {
		for(Parcela p : parcelas) {
			if(p.getDataParcela().equals(LocalDate.now())) {
				if(p.getValorParcela().compareTo(saldoAtual) > 0) {
					throw new RegraDeNegocioException("Não é possível comprar, pois o valor da primeira parcela é maior do que seu saldo atual.");
				}
			}
		}
	}
	
	public BigDecimal valorTotalPedido(List<Itens> itens) {
		BigDecimal valorTotal = BigDecimal.ZERO;
		for(Itens i : itens) {
			valorTotal = valorTotal.add(i.getSubTotal());
		}
		return valorTotal;
	}
	
	
	public List<Pedido> getItens(Integer idUsuario){
		return pedidoRepositorio.getListagem(idUsuario);
	}
	
	public List<PedidoTo> getValorTotalDeCadaPedidoCadastrado(Integer idUsuario) {
		List<PedidoTo> pedidos = new ArrayList<>();
		for(Pedido p : getItens(idUsuario)) {
			PedidoTo pedidoTo = new PedidoTo();
			pedidoTo.setCodigoPedido(p.getCodigoPedido());
			pedidoTo.setValorTotal(p.getValorTotalPedido());
			pedidos.add(pedidoTo);
		}
		return pedidos;
	}
 
}
