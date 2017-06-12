package net.mv.meuespaco.model;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.util.Encryptor;

/**
 * Abstração das entidades do modelo com métodos padrão.
 * 
 * @author Sidronio
 * 17/11/2015
 */
public abstract class EntidadeModel {

	/**
	 * Valida a entidade de acordo com específicas regras de 
	 * negócio, que se não atendidas é gerada uma exceção.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public abstract void valida() throws RegraDeNegocioException; 
	
	/**
	 * Retorna o código formatado com seis dígitos, 
	 * sendo preenchido com zeros a esquerda.
	 * 
	 * @return
	 */
	public String codigoFormatado() {
		
		if (null == this.getCodigo()) {
			return "";
		}
		
		return String.format("%06d", this.getCodigo());
	}
	
	/**
	 * Getter do código, utilizado na formatação.
	 * 
	 * @return
	 */
	public abstract Long getCodigo();


	/**
	 * Criptografa o código formatado.
	 * 
	 * @return código encriptografado.
	 */
	public String codigoEncoded()
	{
		return Encryptor.encrypt(this.codigoFormatado());
	}
}
