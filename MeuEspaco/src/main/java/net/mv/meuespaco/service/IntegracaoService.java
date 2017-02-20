package net.mv.meuespaco.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import net.mv.meuespaco.model.integracao.ClientesDoErp;

/**
 * Abstração para o serviço de integração, encapsulamento este 
 * comportamento nesta interface.
 * 
 * @author sidronio
 * @created 16/02/17
 */
public interface IntegracaoService {

	/**
	 * Lê o arquivo de clientes do Erp, cria e retorna os registros 
	 * dos clientes.
	 * 
	 * @return registros dos clientes do formato Erp.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public List<ClientesDoErp> listaClientesDoErp() throws MalformedURLException, IOException;
	
}
