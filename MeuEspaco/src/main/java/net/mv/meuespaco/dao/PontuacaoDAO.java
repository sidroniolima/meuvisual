package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroPontuacao;
import net.mv.meuespaco.model.integracao.Pontuacao;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface DAO da entidade Pontuacao. 
 * 
 * @author sidronio
 * @created 25/04/2017
 */
public interface PontuacaoDAO extends GenericDAO<Pontuacao, Long>
{

	/**
	 * Lista a pontuação do cliente.
	 * 
	 * @param codigoCliente
	 * @return pontuacao do cliente
	 */
	public List<Pontuacao> buscarPeloCodigoCliente(String codigoCliente, Paginator paginator);
	
	/**
	 * Soma dos pontos acumulados.
	 * 
	 * @param codigoCliente
	 * @return soma dos pontos.
	 */
	public Long buscarSomaDosPontosAcumuladosPeloCodigoCliente(String codigoCliente);

	/**
	 * Efetua um trucante para atualização dos dados importados.
	 */
	public void removerRegistros();
	
	/**
	 * Filtra os registros de pontuação pelo filtro específico e retorna 
	 * de forma paginada.
	 * 
	 * @param filtro
	 * @param paginator
	 * @return lista de pontuação.
	 */
	public List<Pontuacao> filtrarPeloModoEspecifico(FiltroPontuacao filtro, Paginator paginator);
}
