package net.mv.meuespaco.model;

public enum TipoGrade {

	TAMANHO("Tamanho"),
	COR("Cor"),
	COR_E_TAMANHO("Cor e tamanho"), 
	UNICA("Única"), 
	LETRA("Letra"), 
	MUSICAL("Musical");
	
	private String descricao;
	
	private TipoGrade(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public TipoGrade[] tipos() {
		return TipoGrade.values();
	}
	
}
