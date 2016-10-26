package net.mv.meuespaco.model.loja;

public enum StatusEscolha {
	
	PARCIAL("Parcial"),
	ENVIADA("Enviada"),
	EM_SEPARACAO("Em separação"),
	ATENDIDA("Atendido"), 
	FINALIZADA("Finalizada");

	private String descricao;
	
	private StatusEscolha() {	}

	private StatusEscolha(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
}
