package net.mv.meuespaco.exception;

public class ValorInsuficienteParaEscolhaException extends RegraDeNegocioException
{
	private static final long serialVersionUID = -5297854853451616309L;
	
	public ValorInsuficienteParaEscolhaException() {	}

	public ValorInsuficienteParaEscolhaException(String message) 
	{
		super(message);
	}
}
