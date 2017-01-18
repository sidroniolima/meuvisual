package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cupom;
import net.mv.meuespaco.service.CupomService;

/**
 * Implementação da camada Service da entidade Cupom.
 * 
 * @author Sidronio
 * @created 10/09/2016
 */
@Stateless
public class CupomServiceImpl implements CupomService, Serializable {

	private static final long serialVersionUID = 3455854080890459604L;

	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	public CupomServiceImpl() {	}

	public CupomServiceImpl(Cliente clienteLogado) {
		this.clienteLogado = clienteLogado;
	}

	@Override
	public boolean validaCupom(Cupom cupom) throws RegraDeNegocioException {
		
		return true;
	}

	@Override
	public BigDecimal descontoDoCupom(String codigoCupom) throws RegraDeNegocioException 
	{

		Optional<Cupom> cupom = Optional.ofNullable(this.buscaCupom(codigoCupom));
		
		if (cupom.isPresent())
		{
			return cupom.get().getDesconto();
		}
		
		throw new RegraDeNegocioException("O Cupom não existe.");
	}

	@Override
	public Cupom buscaCupom(String codigoCupom) {
		
		if (ehCupomGenerico(codigoCupom))
		{
			return this.criaCupomGenerico(codigoCupom);
		}
		
		return null;
	}
	
	@Override
	public Cupom criaCupomGenerico(String codigoCupom) {
		return new Cupom(codigoCupom, "Cupom genérico", LocalDateTime.now(), new BigDecimal(42));
	}

	@Override
	public boolean ehCupomGenerico(String codigo) {
		return this.getClienteLogado().getSenha().equals(codigo);
	}

	@Override
	public Cliente getClienteLogado() {
		return this.clienteLogado;
	}
}
