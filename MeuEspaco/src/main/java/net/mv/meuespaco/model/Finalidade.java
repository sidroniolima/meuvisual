package net.mv.meuespaco.model;

public enum Finalidade {

	VENDA("Venda"),
	CONSIGNADO("Consignado");
	
	private String descricao;

	private Finalidade(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
