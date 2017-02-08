package net.mv.meuespaco.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import net.mv.meuespaco.model.integracao.Equipe;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Abstração Service para Equipe.
 * 
 * @author sidronio
 * @created 06/02/2017
 */
/**
 * @author sidronio
 *
 */
/**
 * @author sidronio
 *
 */
public interface EquipeService extends SimpleServiceLayer<Equipe, Long>
{
	/**
	 * Importa equipes do arquivo de integração do ERP.
	 * @throws IOException 
	 */
	public void atualizaEquipesDoERP() throws IOException;

	/**
	 * Cria equipe com Cliente e Região.
	 * 
	 * @param params 
	 * 		0 - codigo do cliente
	 * 		1 - status da equipe
	 * 		2 - codigo da equipe
	 * 		3 - codigo da regiao
	 * 
	 * @return instância de equipe com relacionamentos.
	 */
	Optional<Equipe> build(String ...params);

	/**
	 * Exclui todos os registros para importação.
	 */
	public void removeRegistros();
	
	/**
	 * Lista a equipe de um cliente.
	 * 
	 * @param cliente
	 * @return
	 */
	public List<Equipe> listaEquipesDoCliente(Cliente cliente); 
}
