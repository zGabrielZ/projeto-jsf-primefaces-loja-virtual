package br.com.gabrielferreira.entidade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "tab_produto")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = {"categoria"})
public class Produto implements Serializable{

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
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "estoque")
	private Integer estoque;
	
	@Column(name = "data_producao")
	private LocalDate dataProducao;
	
	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private Categoria categoria;
	
	@Lob
	@Column(name = "caminho_imagem")
	private String caminhoImagem;
	
	@Lob
	private byte[] byteImagem;

	@OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
	private List<Itens> itens = new ArrayList<Itens>();
 
}
