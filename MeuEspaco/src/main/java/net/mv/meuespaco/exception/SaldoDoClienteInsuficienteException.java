package net.mv.meuespaco.exception;

public class SaldoDoClienteInsuficienteException extends RegraDeNegocioException
{
	private static final long serialVersionUID = -5297854853451616309L;
	
	public SaldoDoClienteInsuficienteException() {	}

	public SaldoDoClienteInsuficienteException(String message) 
	{
		super(message);
	}
}
