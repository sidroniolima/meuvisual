package net.mv.meuespaco.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroEscolha;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.relatorio.RelatorioEscolha;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface da camada Service da entidade Escolha;
 * 
 * @author Sidronio
 * 08/01/2016
 */
/**
 * @author sidronio
 *
 */
public interface EscolhaService extends SimpleServiceLayer<Escolha, Long> {

	/**
	 * Lista pedidos do cliente.
	 * 
	 * @param Cliente Cliente para pesquisa.
	 * @return Lista de Escolhas.
	 */
	public List<Escolha> listaEscolhasDoCliente(Cliente cliente);

	/**
	 * Cria um pedido utilizando o carrinho.
	 * 
	 * @param carrinho Produtos com quantidade e grade selecionados.
	 * 		  cliente Cliente do usuário logado.
	 * @return Escolha pedito criado.
	 * @throws RegraDeNegocioException 
	 */
	public void criaEscolhaPeloCarrinho(List<ItemCarrinho> carrinho, Cliente cliente) throws RegraDeNegocioException;

	/**
	 * Cria um pedido utilizando o carrinho propriamente.
	 * 
	 * @param carrinho do cliente.
	 * @return Escolha pedito criado.
	 * @throws RegraDeNegocioException 
	 */
	public void criaEscolhaPeloCarrinho(Carrinho carrinho, Cliente cliente) throws RegraDeNegocioException;

	
	/**
	 * Busca um pedido com seus itens.
	 * 
	 * @param codigoEscolha
	 * @return Escolha com itens.
	 */
	public Escolha buscaPeloCodigoComItens(Long codigoEscolha);

	/**
	 * Filtra de modo específico os registros de Escolha e 
	 * retorna os dados de forma paginada.
	 * 
	 * @param filtro Filtro de Escolha.
	 * @param paginator Paginator utilizado para paginar o resultado.
	 * @return Lista de Escolhas de acordo com o filtro utilizado.
	 */
	public List<Escolha> filtraPelaPesquisa(FiltroEscolha filtro, Paginator paginator);
	
	/**
	 * Informa a quantidade de peças escolhidas por período.
	 * 
	 * @param inicio Início do perído para pesquisa.
	 * @param fim Fim do perído para pesquisa.
	 * @param cliente Cliente para pesquisa.
	 * @return Quantidade de peças.
	 */
	public BigDecimal qtdDePecasEscolhidasPorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim);
	
	/**
	 * Busca a escolha aberta (atual) do cliente.
	 * 
	 * @param cliente Cliente para pesquisa.
	 * @return Escolha aberta.
	 */
	public Escolha buscaEscolhaAbertaDoCliente(Cliente cliente);

	/**
	 * Busca o registro de Escolha pelo código e retorna 
	 * suas informações junto a seus itens  ordenados pela localização.
	 *  
	 * @param codigo
	 * @return
	 */
	public Escolha buscarParaAtendimento(Long codigo);

	/**
	 * Gera as informações da Escolha para criação do 
	 * relatório.
	 * 
	 * @param codigo Código da requisição.
	 * @return Informações do relatório.
	 */
	public List<RelatorioEscolha> geraRelatorioDeEscolha(Long codigo);

	/**
	 * Verifica se o cliente possui escolha.
	 * 
	 * @param cliente Cliente para consulta.
	 * @return 
	 */
	public boolean clientePossuiEscolha(Cliente cliente);

	/**
	 * Busca uma escolha pelo seu código com seus itens e demais 
	 * relacionamentos.
	 * 
	 * @param paramCodigo Código da busca.
	 * @return Escolha com itens e relacionamentos.
	 */
	public Escolha buscaComRelacionamentos(Long paramCodigo);
	
	/**
	 * Busca uma escolha com seus itens do cliente em um determinado 
	 * período.
	 * 
	 * @param cliente da busca.
	 * @param dataInicial data inicial para busca.
	 * @param dataFinal data final para busca.
	 * @return escolha com seus itens.
	 */
	public Escolha buscaPeloClientePorPeriodo(Cliente cliente, LocalDate dataInicial, LocalDate dataFinal);

	/**
	 * Atualiza as escolhas que devem ser enviadas e as salva.
	 */
	public void enviaEscolhasJaVencidas();
	
	/**
	 * Informa o valor de peças descontáveis escolhidas do ciclo atual.
	 * 
	 * @param cliente Cliente para pesquisa.
	 * @return Valor das peças descontáveis.
	 * @throws RegraDeNegocioException caso não seja possível definir o ciclo ou
	 * o valor das peças. 
	 */
	public BigDecimal valorDePecasEscolhidasDescontaveisDoCicloAtual(Cliente cliente) throws RegraDeNegocioException;

	
	/**
	 * Busca escolha da semana do cliente pela data inicial e final.
	 * 
	 * @param cliente com semana.
	 * @return escolha única da semana.
	 * @throws RegraDeNegocioException caso não seja possível informar as 
	 * datas. Pode ocorre por conta de um cadastro sem região ou semana. 
	 */
	public Escolha buscarEscolhaDaSemanaAtual(Cliente cliente) throws RegraDeNegocioException;

	/**
	 * Informa a qtd de peças descontáveis escolhidas do ciclo atual.
	 * 
	 * @param cliente Cliente para pesquisa.
	 * @return qtd de peças descontáveis.
	 * @throws RegraDeNegocioException caso não seja possível informar as 
	 * datas. Pode ocorre por conta de um cadastro sem região ou semana. 
	 */
	public BigDecimal qtdDePecasEscolhidasDoCicloAtual(Cliente cliente) throws RegraDeNegocioException;
}
