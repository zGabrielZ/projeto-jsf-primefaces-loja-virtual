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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "tab_usuario")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"saldos","pedidos","perfil"})
public class Usuario implements Serializable{

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
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "data_nascimento")
	private LocalDate dataNascimento;
	
	@Column(name = "senha")
	private String senha;
	
	@ManyToOne
	@JoinColumn(name = "perfil_id")
	private Perfil perfil;
	
	@OneToMany(mappedBy = "usuario",fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private List<Saldo> saldos = new ArrayList<Saldo>();
	
	@OneToMany(mappedBy = "usuario",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	@OneToOne(cascade = CascadeType.ALL,mappedBy = "usuario")
	private Endereco endereco;
	
	@Transient
	private Integer codigoUsuario;
	
	@Transient
	private boolean codigoErro;
	
	public BigDecimal getSaldoTotal() {
		BigDecimal valor = new BigDecimal("0.0");
		for(Saldo saldo : saldos) {
			valor = valor.add(saldo.getDeposito());
		}
		return valor;
	}

}
