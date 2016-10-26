package net.mv.meuespaco.exception;

/**
 * Exceção associada às transações do Banco na 
 * exclusão de um registro.
 * 
 * @author Sidronio
 * 17/11/2015
 */
public class DeleteException extends Exception {

	private static final long serialVersionUID = -5827874763522857237L;

	public DeleteException(){ }
	
	public DeleteException(String message) {
		super(message);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}	
}
