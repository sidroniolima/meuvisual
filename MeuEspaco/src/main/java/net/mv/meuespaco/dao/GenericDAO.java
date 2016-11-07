package net.mv.meuespaco.dao;

import java.io.Serializable;
import java.util.List;

import net.mv.meuespaco.util.Paginator;

public interface GenericDAO<T, ID extends Serializable> {
	
	/**
	 * Salva uma instância.
	 * 
	 * @param entidade
	 * @return a entidade criada.
	 */
	public T salvar(T entidade);
	
	/**
	 * Exclui um registro pelo identificado.
	 * 
	 * @param id
	 */
	public void excluir(ID id);
	
	/**
	 * Busca um registro pelo código.
	 * 
	 * @param id
	 * @return
	 */
	public T buscarPeloCodigo(ID id);
	
	/**
	 * Busca um registro pelo código com 
	 * relacionamentos.
	 * 
	 * @param id
	 * @return
	 */
	public T buscarPeloCodigoComRelacionamento(
			ID id, 
			List<String> relacionamentos);
	
	/**
	 * Filtra os registros da entidade de acordo com o filtro.
	 * 
	 * @param entidade			
	 * @param propriedades
	 * @param relacionamentos
	 * @param ordenacao
	 * @param aliases
	 * @return
	 */
	public List<T> filtrar(
			T entidade, 
			List<String> propriedades, 
			List<String> relacionamentos, 
			List<String> ordenacao, 
			List<String> aliases);
	
	/**
	 * Lista os registros de forma paginada, isto é, definindo 
	 * o primeiro e quantidade de registros selecionados.
	 * 
	 * @param firstResult
	 * @param numberPerPage
	 * @param ordenacao
	 * @param relacionamentos
	 * @param aliases
	 * @return
	 */
	public List<T> listarComPaginacao(
			Paginator paginator,
			List<String> ordenacao, 
			List<String> relacionamentos,
			List<String> aliases);
	
}
