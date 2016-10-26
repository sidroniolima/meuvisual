package net.mv.meuespaco.model.loja;

public enum StatusCliente {

	PRE_CADASTRO("Pré-cadastro"),
	INATIVO("Inativo"),
	ATIVO("Ativo");
	
	private String descricao;
	
	private StatusCliente() {	}

	private StatusCliente(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	
}
