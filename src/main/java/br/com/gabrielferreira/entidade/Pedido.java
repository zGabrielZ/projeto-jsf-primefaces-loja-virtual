package br.com.gabrielferreira.entidade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "tab_pedido")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"usuario","parcelas","itens"})
public class Pedido implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "data_pedido")
	private LocalDateTime dataPedido;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Usuario usuario;
	
	@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
	private List<Parcela> parcelas = new ArrayList<Parcela>();
	
	@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
	private List<Itens> itens = new ArrayList<Itens>();

}
