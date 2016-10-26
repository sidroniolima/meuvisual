package net.mv.meuespaco.model.loja;

import net.mv.meuespaco.exception.DocumentoInvalidoException;

public class Rg extends Documento {

	/**
	 * Construtor padrão. 
	 */
	public Rg() {	}
	
	/**
	 * Constrói o Rg com valor.
	 * 
	 * @param valor Número do RG.
	 */
	public Rg(String valor) {
		super(valor);
	}
	
	@Override
	public void valida() throws DocumentoInvalidoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TipoDocumento tipo() {
		// TODO Auto-generated method stub
		return null;
	}

}
