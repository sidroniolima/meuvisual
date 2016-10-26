package net.mv.meuespaco.exception;

public class IntegracaoException extends Exception {

	private static final long serialVersionUID = -195531033589082680L;

	public IntegracaoException() {	}
	
	public IntegracaoException(String message)
	{
		super(message);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
