package net.mv.meuespaco.model.cielo;

/**
 * Tipos de pagamentos utilizados.
 * 
 * @author sidronio
 *
 */
public enum PaymentType {

	CreditCard("Crédito"),
	DebitCard("Débito");
	
	private String descricao;
	
	private PaymentType(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
}
