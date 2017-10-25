package net.mv.meuespaco.exception;

public class QtdInsuficienteParaEscolhaException extends RegraDeNegocioException
{
	private static final long serialVersionUID = -5297854853451616309L;
	
	public QtdInsuficienteParaEscolhaException() {	}

	public QtdInsuficienteParaEscolhaException(String message) 
	{
		super(message);
	}
}
