/**
 * 
 */
package net.mv.meuespaco.exception;

/**
 * Lançada quando uma regra de negócio não 
 * é respeitada
 * 
 * @author Sidronio
 * 28/09/2015
 */
public class RegraDeNegocioException extends Exception {

	private static final long serialVersionUID = -5580404604241541769L;

	public RegraDeNegocioException() {	}
	
	public RegraDeNegocioException(String message) {
		super(message);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
