package net.mv.meuespaco.model.estoque;

public enum TipoMovimento {

	ENTRADA("Entrada"),
	SAIDA("Saída"),
	TRASFERENCIA("Transferência");
	
	private String descricao;
	
	private TipoMovimento() {	}

	private TipoMovimento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static TipoMovimento[] getTipos() {
		return TipoMovimento.values();
	}
}
