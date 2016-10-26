package net.mv.meuespaco.model.loja;

import javax.persistence.Column;
import javax.persistence.Id;

import net.mv.meuespaco.exception.DocumentoInvalidoException;

/**
 * Abstração de Documento.                     
 * 
 * @author Sidronio
 * 22/12/2015
 */
public abstract class Documento{

	@Id
	@Column(nullable=false)
	private String valor;
	
	/**
	 * Default constructor.
	 */
	public Documento() {	}
	
	/**
	 * Construtor com parâmetro.
	 * 
	 * @param valor Valor do documento.
	 */
	public Documento(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * Valida um documento com regras específicas definidas
	 * por tipo.
	 * 
	 * @throws DocumentoInvalidoException
	 */
	public abstract void valida() throws DocumentoInvalidoException;
	
	/**
	 * Informa o tipo de documento da implementação.
	 * 
	 * @return Tipo do documento.
	 */
	public abstract TipoDocumento tipo();

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Documento other = (Documento) obj;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}
	
}
