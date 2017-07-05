package net.mv.meuespaco.model.estoque;

public enum OrigemMovimento {

	ORDEM_DE_PRODUCAO("Ordem de produção"),
	ENTRADA("Entrada"),
	VENDA("Venda"),
	DEFEITO("Defeito"),
	TROCA("Troca"), 
	SEPARACAO("Separação"),
	AJUSTE("Ajuste"), 
	COMPRA("Compra"),
	REQUISICAO("Requisição"),
	ESCOLHA("Escolha"),
	ESTORNO_ESCOLHA("Estorno de escolha"),
	RESGATE_BRINDE("Resgate de brinde"),
	ESTORNO_RESGATE_BRINDE("Estorno de resgate de brinde"),
	ESTORNA_VENDA("Estorna venda");
	
	private OrigemMovimento(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}
}
