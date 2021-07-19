package br.com.gabrielferreira.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "tab_cliente")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"saldos","pedidos"})
public class Cliente implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(name = "nome")
	private String nome;
	
	@Email(message = "E-mail inválido")
	@Column(name = "email")
	private String email;
	
	@CPF(message = "CPF inválido")
	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;
	
	@OneToMany(mappedBy = "cliente",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private List<Saldo> saldos = new ArrayList<Saldo>();
	
	@OneToMany(mappedBy = "cliente",fetch = FetchType.LAZY)
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	public BigDecimal getSaldoTotal() {
		BigDecimal valor = new BigDecimal("0.0");
		for(Saldo saldo : saldos) {
			valor = valor.add(saldo.getDeposito());
		}
		return valor;
	}

}
