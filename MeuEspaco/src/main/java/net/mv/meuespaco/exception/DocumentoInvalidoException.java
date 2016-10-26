package net.mv.meuespaco.exception;

/**
 * Exceção lançada na validação do documento.
 * 
 * @author Sidronio
 * 22/12/2015
 */
public class DocumentoInvalidoException extends RegraDeNegocioException {

	private static final long serialVersionUID = 7033401701475246300L;

	/**
	 * Default constructor.
	 */
	public DocumentoInvalidoException() {	}
	
	/**
	 * Exceção com mensagem.
	 * 
	 * @param message Mensagem da exceção.
	 */
	public DocumentoInvalidoException(String message) {
		super(message);
	}
	
}
