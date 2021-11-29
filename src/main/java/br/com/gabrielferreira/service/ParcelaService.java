package br.com.gabrielferreira.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import br.com.gabrielferreira.entidade.Parcela;
import br.com.gabrielferreira.exceptions.RegraDeNegocioException;
import br.com.gabrielferreira.repositorio.ParcelaRepositorio;

public class ParcelaService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ParcelaRepositorio parcelaRepositorio;
	
	public void inserir(Parcela parcela) {
		parcelaRepositorio.inserir(parcela);
	}
	
	public void inserir(List<Parcela> parcelas) {
		parcelaRepositorio.inserirParcelas(parcelas);
	}
	
	public Parcela procurarPorId(Integer id) {
		return parcelaRepositorio.pesquisarPorId(id, Parcela.class);
	}
	
	public List<Parcela> getListagem(){
		return parcelaRepositorio.getParcelas();
	}
	
	public void gerarParcelasPedido(List<Parcela> parcelas, int quantidadeParcelas,BigDecimal valorTotalPedido) throws RegraDeNegocioException {
		verificarParcelas(parcelas);
		checarQuantidadeParcelas(quantidadeParcelas);
		gerandoParcelamentos(valorTotalPedido, quantidadeParcelas, parcelas);
	}
	
	private void verificarParcelas(List<Parcela> parcelas) {
		if(!parcelas.isEmpty()) {
			parcelas.clear();
		}
	}
	
	private void checarQuantidadeParcelas(int quantidadeParcelas) throws RegraDeNegocioException {
		if(quantidadeParcelas < 1) {
			throw new RegraDeNegocioException("Não é possível gerar parcelas com o valor 0.");
		}
	}
	
	private void gerandoParcelamentos(BigDecimal valorTotalPedido,int quantidadeParcelas,List<Parcela> parcelas) {
		LocalDate dataParcelaAtual = LocalDate.now();
		for(int i = 1; i <= quantidadeParcelas; i++) {
			Parcela parcela = new Parcela();
			BigDecimal valorParcelado = valorTotalPedido.divide(new BigDecimal(quantidadeParcelas),RoundingMode.HALF_UP);
			parcela.setDataParcela(dataParcelaAtual);
			dataParcelaAtual = dataParcelaAtual.plusMonths(1);
			parcela.setValorParcela(valorParcelado);
			parcelas.add(parcela);
		}
	}
	
	public List<Parcela> getParcelas(Integer idUsuario, int primeiroResultado, int quantidadeMaxima){
		return parcelaRepositorio.getParcelasByUsuario(idUsuario, primeiroResultado, quantidadeMaxima);
	}
	
	public Integer quantidadeRegistro(Integer idUsuario) {
		return parcelaRepositorio.quantidadeRegistro(idUsuario);
	}
 
}
