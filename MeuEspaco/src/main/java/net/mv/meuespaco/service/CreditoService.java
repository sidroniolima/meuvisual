package net.mv.meuespaco.service;

import java.time.LocalDate;

import net.mv.meuespaco.integracao.ListagemCreditos;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Interface Service da classe Credito.
 * 
 * @author sidronio
 * @created 02/01/2017
 */
public interface CreditoService 
{
	/**
	 * Lista os créditos gerados e importados do ERP para o cliente e período.
	 * 
	 * @param cliente
	 * @param inicio
	 * @param fim
	 * @return listagem de creditos.
	 */
	public ListagemCreditos listagemDeCreditoDoClientePorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim);
}
