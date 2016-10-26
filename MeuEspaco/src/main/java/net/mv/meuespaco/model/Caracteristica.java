package net.mv.meuespaco.model;

public enum Caracteristica {

	COMPRIMENTO("Comprimento"),
	DIAMETRO("Diâmetro"),
	DETALHE("Detalhe");
	
	private String descricao;

	private Caracteristica(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
