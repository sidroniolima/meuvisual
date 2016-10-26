package net.mv.meuespaco.model;

/**
 * Classificação dos produtos pelo tipo: fashion ou tradicional.
 * 
 * @author Sidronio
 * 02/12/2015
 */
public enum TipoProduto {

	FASHION("Fashion"),
	TRADICIONAL("Tradicional"),
	APOIO("Apoio");
	
	private String descricao;

	private TipoProduto(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public TipoProduto[] getTipos() {
		return TipoProduto.values();
	}
}
