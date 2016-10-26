package net.mv.meuespaco.service;

import java.io.Serializable;
import java.util.List;

import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface para a camada Service com os métodos básicos.
 * 
 * @author Sidronio
 * 
 * 05/11/2015
 * 
 * @param <T>
 * @param <ID>
 */
public interface SimpleServiceLayer<T, ID extends Serializable> {

	/**
	 * Salva uma instância.
	 * 
	 * @param entidade
	 * @throws RegraDeNegocioException
	 */
	public void salva(T entidade) throws RegraDeNegocioException;
	
	/**
	 * Exclui uma instância pelo código.
	 * 
	 * @param id
	 * @throws RegraDeNegocioException
	 */
	public void exclui(ID id) throws RegraDeNegocioException, DeleteException;
	
	/**
	 * Retorna um registro salvo pelo Código.
	 * 
	 * @param id
	 * @return
	 */
	public T buscaPeloCodigo(ID id);
	
	/**
	 * Retorna todos os registros.
	 * 
	 * @return
	 */
	public List<T> buscaTodas();
	
	/**
	 * Valida a inserção de registros.
	 * 
	 * @param entidade
	 * @throws RegraDeNegocioException
	 */
	public void validaInsercao(T entidade) throws RegraDeNegocioException;
	
	/**
	 * Valida a exclusão.
	 * 
	 * @param entidade
	 * @throws RegraDeNegocioException
	 */
	public void validaExclusao(T entidade) throws RegraDeNegocioException;
	
	/**
	 * Retornar registros da entidade obedecendo os critéiros de  
	 * primeiro registro e quantidade deles.
	 * 
	 * @param paginator	Primeiro registro da pesquisa e números de registros por pesquisa.
	 * @param ordenacao		Campos para ordenação.
	 * @param relacionamentos	Relacionamentos
	 * 
	 * @return	Lista de registros de forma paginada.
	 */
	public List<T> listarComPaginacao(
			Paginator paginator, 
			List<String> ordenacao, 
			List<String> relacionamentos,
			List<String> aliases);
	
}
