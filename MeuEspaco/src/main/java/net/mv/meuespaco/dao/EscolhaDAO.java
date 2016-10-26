package net.mv.meuespaco.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroEscolha;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.relatorio.RelatorioEscolha;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface da camada DAO da entidade Escolha;
 * 
 * @author Sidronio
 * 08/01/2016
 */
/**
 * @author Sidronio
 *
 */
public interface EscolhaDAO extends GenericDAO<Escolha, Long> {

	/**
	 * Filtra os registros de escolha pelo modo específico, 
	 * utilizando o código, data inicial e final, cliente ou 
	 * status.
	 * 
	 * @param filtro Filtro da pesquisa.
	 * @param paginator Primeiro registro e quantidade de registros por página.
	 * @return Lista de registros paginados.
	 */
	List<Escolha> filtrarPeloModoEspecifico(FiltroEscolha filtro, Paginator paginator);
	
	/**
	 * Calcula a quantidade de peças escolhidas por período  
	 * não incluindo produtos que não são descontáveis.
	 * 
	 * @param inicio Início do perído para pesquisa.
	 * @param fim Fim do perído para pesquisa.
	 * @param cliente Cliente para pesquisa.
	 * @return Quantidade de peças.
	 */
	public BigDecimal calcularQtdDePecasEscolhidasPorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim);

	/**
	 * Buscar a escolha do cliente aberta.
	 * 
	 * @param cliente Cliente para pesquisa
	 * @return Escolha aberta.
	 */
	public Escolha buscarAbertaDoCliente(Cliente cliente);

	/**
	 * Buscar escolha pelo código com itens ordenado pela localização 
	 * da grade.
	 * 
	 * @param codigoEscolha
	 * @return
	 */
	public Escolha buscarPeloCodigoComItensOrdenadoPelaLocalizacao(Long codigo);

	/**
	 * Gera as informações da Escolha, junto às do cliente, 
	 * região e itens.
	 * 
	 * @param codigo Código da escolha.
	 * @return Informações da escolha.
	 */
	public List<RelatorioEscolha> gerarInformacoesDoRelatorio(Long codigo);

	/**
	 * Busca uma escolha pelo código com seus itens e relacionamentos 
	 * de produto, subgrupo e produto para visualização.
	 * 
	 * @param codigo Código da escolha.
	 * @return Escolha com seus itens e demais informações.
	 */
	Escolha buscarComRelacionamentos(Long codigo);

	/**
	 * Busca uma escolha pelo cliente e período.
	 * 
	 * @param cliente para busca. 
	 * @param dataInicial para busca.
	 * @param dataFinal para busca.
	 * @return escolha com seus itens.
	 */
	Escolha buscarEscolhaPorClienteEPeriodo(Cliente cliente, LocalDate dataInicial, LocalDate dataFinal);

	/**
	 * Atualiza escolhas como enviadas cuja data de envio seja 
	 * menor que a data atual.
	 */
	void atualizaStatusEDataDeEnvioDeEscolhasVencidas();
}
