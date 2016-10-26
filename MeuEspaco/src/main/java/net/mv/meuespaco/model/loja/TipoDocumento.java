package net.mv.meuespaco.model.loja;

/**
 * Tipos de documentos utilizados no cadastro de 
 * clientes. 
 * 
 * @author Sidronio
 * 22/12/2015
 */
public enum TipoDocumento {

	CPF("Cpf"),
	RG("Rg");
	
	private String descricao;
	
	private TipoDocumento() {	}

	private TipoDocumento(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
}
