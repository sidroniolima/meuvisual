package net.mv.meuespaco.service;

import java.io.IOException;
import java.time.LocalDate;

import net.mv.meuespaco.model.integracao.Credito;
import net.mv.meuespaco.model.integracao.ListagemCreditos;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Interface Service da classe Credito.
 * 
 * @author sidronio
 * @created 02/01/2017
 */
public interface CreditoService extends SimpleServiceLayer<Credito, Long> 
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
	
	/**
	 * Le o arquivo de Creditos, gera os registros e os inclui.
	 * 
	 * @throws IOException 
	 */
	public void atualizaCreditosDoERP() throws IOException;
}
