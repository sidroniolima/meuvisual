package net.mv.meuespaco.model.loja;

import net.mv.meuespaco.exception.DocumentoInvalidoException;

public class Cpf extends Documento {
	
	/**
	 * Construtor padrão.
	 */
	public Cpf() {	}
	
	/**
	 * Construtor com valor.
	 * 
	 * @param valor
	 */
	public Cpf(String valor) {
		super(valor);
	}

	@Override
	public void valida() throws DocumentoInvalidoException {
		
	}

	@Override
	public TipoDocumento tipo() {
		return TipoDocumento.CPF;
	}

}
