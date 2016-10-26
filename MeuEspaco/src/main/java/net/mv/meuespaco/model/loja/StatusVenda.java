package net.mv.meuespaco.model.loja;

/**
 * Status da venda.
 * 
 * @author Sidronio
 * @created 23/08/2016
 */
public enum StatusVenda {

	AGUARDANDO_PAGAMENTO("Aguardando pagamento"),
	PAGAMENTO_CONFIRMADO("Pagamento confirmado"),
	EM_SEPARACAO("Em separação"),
	FINALIZADA("Finalizada");
	
	private String descricao;
	
	private StatusVenda(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
