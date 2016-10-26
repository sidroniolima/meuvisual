package net.mv.meuespaco.util;

/**
 * Classe auxiliar que encapsula os valores da 
 * quantidade e valor permitidos para escolha. 
 * 
 * @author Sidronio
 * 12/05/2016
 */
public class ValoresDoErp {
	
	private float qtdPermitida;
	private float valorPermitido;
	
	public ValoresDoErp(float qtdPermitida, float valorPermitido) {
		this.qtdPermitida = qtdPermitida;
		this.valorPermitido = valorPermitido;
	}
	
	public float getQtdPermitida() {
		return qtdPermitida;
	}
	public void setQtdPermitida(float qtdPermitida) {
		this.qtdPermitida = qtdPermitida;
	}

	public float getValorPermitido() {
		return valorPermitido;
	}
	public void setValorPermitido(float valorPermitido) {
		this.valorPermitido = valorPermitido;
	}
	
}