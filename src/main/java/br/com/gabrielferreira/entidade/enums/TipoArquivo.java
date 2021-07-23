package br.com.gabrielferreira.entidade.enums;

public enum TipoArquivo {

	EXCEL(1,"Excel"),
	TXT(2,"Txt");
	
	private int codigo;
	private String descricao;
	
	TipoArquivo(int codigo,String descricao){
		this.codigo = codigo;
		this.descricao = descricao;
	}

	
	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
