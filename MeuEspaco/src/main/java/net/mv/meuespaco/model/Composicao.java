package net.mv.meuespaco.model;

public enum Composicao {

	PR("Prata"),
	DOURADO("Dourado"),
	BP("Banho de prata"),
	OUTROS("Outros");
	
	private String descricao;

	private Composicao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
