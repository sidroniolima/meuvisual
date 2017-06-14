package net.mv.meuespaco.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroPontuacao;
import net.mv.meuespaco.model.integracao.Pontuacao;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface Service da entidade Pontuacao. 
 * 
 * @author sidronio
 * @created 25/04/2017
 */
public interface PontuacaoService extends SimpleServiceLayer<Pontuacao, Long>
{
	/**
	 * Lista a pontuação do cliente logado.
	 * 
	 * @param paginator
	 * @return lista de pontuação.
	 */
	public List<Pontuacao> pontuacaoDoClienteLogado(Paginator paginator);
	
	/**
	 * Total de pontos acumulados do cliente logado.
	 * 
	 * @return pontos acumulados.
	 */
	public Long pontosAcumuladosDoClienteLogado();
	
	/**
	 * Importa a pontuação do arquivo gerado do ERP.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	public void atualizaPontuacaoDoErp() throws MalformedURLException, IOException;

	/**
	 * Filtra os registros pelo filtro específico.
	 * 
	 * @param filtro
	 * @param paginator
	 * @return lista de registros ordenados e paginados.
	 */
	public List<Pontuacao> filtraPeloModoEspecifico(FiltroPontuacao filtro, Paginator paginator);
	
	/**
	 * Calcula o saldo de pontos do cliente logado pelo total 
	 * acumulado menos seus resgates.
	 * 
	 * @return
	 */
	public Long saldoDePontosDoClienteLogado();
	
	/**
	 * Calcula os pontos resgatados do cliente.
	 * 
	 * @return
	 */
	public Long pontosResgatadosDoClienteLogado();
}
