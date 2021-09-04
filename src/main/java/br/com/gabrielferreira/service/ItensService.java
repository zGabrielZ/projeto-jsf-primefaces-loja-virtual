package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Itens;
import br.com.gabrielferreira.entidade.Parcela;
import br.com.gabrielferreira.entidade.Produto;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.ItensRepositorio;
import br.com.gabrielferreira.utils.FacesMessages;
import br.com.gabrielferreira.utils.Transacional;

public class ItensService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ItensRepositorio itensRepositorio;
	
	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private ParcelaService parcelaService;
	
	private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	@Transacional
	public void inserir(Itens itens) {
		itensRepositorio.inserir(itens);
	}
	
	@Transacional
	public void inserir(List<Itens> itens) {
		itensRepositorio.inserirItens(itens);
	}
	
	public Itens procurarPorId(Integer id) {
		return itensRepositorio.procurarPorId(id);
	}
	
	public void adicionarCarrinhoItens(Produto produtoSelecionado, List<Itens> pedidos,List<Parcela> parcelas,int quantidadeParcelas,
			BigDecimal valorTotalPedido) throws RegraDeNegocioException {
		Integer diminuirEstoque = produtoSelecionado.getEstoque() - 1;
		List<Produto> listaProdutos = produtoService.getListagem();
		adicionarProdutoEmLista(listaProdutos, produtoSelecionado, diminuirEstoque,pedidos);
		verificarParcelas(parcelas, quantidadeParcelas, valorTotalPedido);
	}
	
	public void adicionarProdutoEmLista(List<Produto> listaProdutos, Produto produtoSelecionado, Integer diminuirEstoque,
			List<Itens> pedidos) {
		
		Itens itensCompra = new Itens();
		for (Produto p : listaProdutos) {
			if (p.getId().equals(produtoSelecionado.getId())) {
				
				Integer estoqueAntigo = p.getEstoque();
				itensCompra.setProduto(produtoSelecionado);
				itensCompra.setQuantidadeCompra(estoqueAntigo - diminuirEstoque);
				itensCompra.setDataCompra(LocalDateTime.now());
				
				Itens itemCompraQuantidade = verificarQuantidade(itensCompra,pedidos);
				if(!itemCompraQuantidade.isItemPedidoRepetido()) {
					pedidos.add(itensCompra);
				}
				
				FacesMessages.adicionarMensagem("consultaPedidosForm:msg", FacesMessage.SEVERITY_INFO,
						"Adicionado no carrinho o produto " + p.getNome() + ", Hora : " + df.format(itensCompra.getDataCompra()), null);
			}
		}
	}
	
	private Itens verificarQuantidade(Itens itemCompra, List<Itens> pedidos) {
		for(Itens i : pedidos) {
			if(i.getProduto().getNome().equals(itemCompra.getProduto().getNome())) {
				int quantidadeEstoqueAntigo = i.getQuantidadeCompra();
				i.setQuantidadeCompra(quantidadeEstoqueAntigo + itemCompra.getQuantidadeCompra());
				i.setDataCompra(itemCompra.getDataCompra());
				
				itemCompra.setItemPedidoRepetido(true);
				return itemCompra;
			}
		}
		return itemCompra;
	}
	
	public void removerCarrinhoItens(Itens itens,List<Itens> listaItens,List<Parcela> parcelas, int quantidadeParcelas, BigDecimal valorTotalPedido) throws RegraDeNegocioException {
		listaItens.remove(itens);
		verificarParcelas(parcelas, quantidadeParcelas, valorTotalPedido);
	}
	
	private void verificarParcelas(List<Parcela> parcelas, int quantidadeParcelas, BigDecimal valorTotalPedido) throws RegraDeNegocioException {
		if(!parcelas.isEmpty()) {
			parcelaService.gerarParcelasPedido(parcelas, quantidadeParcelas, valorTotalPedido);
		}
	}

 
}
