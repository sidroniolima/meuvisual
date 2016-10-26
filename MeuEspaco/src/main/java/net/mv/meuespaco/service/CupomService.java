package net.mv.meuespaco.service;

import java.math.BigDecimal;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cupom;

/**
 * Abstração da camada service da entidade Cupom.
 * 
 * @author Sidronio
 * @created 10/09/2016
 */
public interface CupomService {

	/**
	 * Verifica se o cupom existe e é válido.
	 * 
	 * @param cupom
	 * @return
	 */
	public boolean validaCupom(Cupom cupom) throws RegraDeNegocioException;
	
	/**
	 * Informa o desconto do cupom.
	 * 
	 * @param cupom
	 * @return
	 * @throws RegraDeNegocioException 
	 */
	public BigDecimal descontoDoCupom(String cupom) throws RegraDeNegocioException;
	
	/**
	 * Busca um cupom pelo código.
	 * 
	 * @param cupom
	 * @return
	 */
	public Cupom buscaCupom(String cupom);
	
	/**
	 * Verifica se o cupom é o genérico, isto é, 
	 * tem o código do cliente.
	 * 
	 * @param codigo
	 * @return
	 */
	public boolean ehCupomGenerico(String codigo);
	
	/**
	 * Cria um cupom genérico pelo código.
	 * 
	 * @param codigoCupom
	 * @return
	 */
	public Cupom criaCupomGenerico(String codigoCupom);
	
	/**
	 * Retorna o cliente logado.
	 */
	public Cliente getClienteLogado();
}
