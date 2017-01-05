package net.mv.meuespaco.integracao;

/**
 * Classe dos créditos para gerar o valor. 
 * 
 * @author sidronio
 * @created 02/01/2017	
 */
public enum ClasseCredito {

	COM("Comissão", true),
	DBT("Débito", false),
	CDT("Crédito", true);
	
	private ClasseCredito(String descricao, boolean soma) 
	{
		this.descricao = descricao;
		this.soma = soma;
	}
	
	private String descricao;
	private boolean soma;
	
	public String getDescricao() {
		return descricao;
	}

	public boolean isSoma() {
		return soma;
	}
	
}
