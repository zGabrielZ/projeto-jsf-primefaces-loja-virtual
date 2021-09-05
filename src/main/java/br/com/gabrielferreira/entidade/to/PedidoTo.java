package br.com.gabrielferreira.entidade.to;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class PedidoTo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer codigoPedido;
	private BigDecimal valorTotal;

}
