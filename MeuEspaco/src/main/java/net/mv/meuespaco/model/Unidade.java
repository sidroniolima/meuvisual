package net.mv.meuespaco.model;

/**
 * Unidade de medida de produtos.
 * 
 * @author Sidronio
 * 21/12/2015
 */
public enum Unidade {

	PEÇA("Peça"),
	UNIDADE("Unidade");
	
	private String descricao;
	
	private Unidade(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
