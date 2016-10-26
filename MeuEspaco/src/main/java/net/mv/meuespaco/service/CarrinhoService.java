package net.mv.meuespaco.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.ItemCarrinho;

/**
 * Camada Service com implementação das funções do carrinho.
 * 
 * @author Sidronio
 * 08/12/2015
 */
public interface CarrinhoService {

	/**
	 * Adiciona um produto ao carrinho validando a adição 
	 * e verificando a quantidade disponível do produto e 
	 * grade selecionada.
	 * 
	 * @param produto Produto selecionado.
	 * @param qtd Quantidade.
	 * @param gradeSelecionada Grade específica selecionada.
	 * @throws RegraDeNegocioException
	 */
	public void adicionaProduto(Produto produto, BigDecimal qtd, Grade gradeSelecionada) throws RegraDeNegocioException;
	
	/**
	 * Remove um item do carrinho.
	 * 
	 * @param item
	 */
	public void removeItem(ItemCarrinho item);
	
	/**
	 * Altera a quantidade do item carrinho.
	 * 
	 * @param item
	 * @param novaQtd
	 */
	public void alteraQtd(ItemCarrinho item, BigDecimal novaQtd);
	
	/**
	 * Esvazia o carrinho.
	 */
	public void esvazia();
	
	/**
	 * Finaliza a escolha gerando um pedido 
	 * para o cliente.
	 * @param usuario Usuário logado.
	 * @throws RegraDeNegocioException 
	 */
	public void finalizaEscolha(Usuario usuario) throws RegraDeNegocioException;
	
	/**
	 * Finaliza a escolha pelo carriho gerando um pedido 
	 * para o cliente.
	 * 
	 * @param carrinho do cliente.
	 * @param usuario Usuário logado.
	 * @throws RegraDeNegocioException 
	 */
	public void finalizaEscolha(Carrinho carrinho, Usuario usuario) throws RegraDeNegocioException;
	
	/**
	 * Calcula a quantidade de itens do carrinho.
	 * 
	 * @return qtd total de itens.
	 */
	public double qtdDeItens();
	
	/**
	 * Calcula a quantidade de itens descontáveis do carrinho.
	 * 
	 * @return qtd descontável.
	 * @throws RegraDeNegocioException
	 */
	public double qtdDeItensDescontaveis() throws RegraDeNegocioException;
	
	/**
	 * Retorna o carrinho de escolha.
	 * 
	 * @return Carrinho de escolha.
	 */
	public List<ItemCarrinho> getCarrinho();
	
	/**
	 * Retorna o resumo das informações do carrinho, isto é, 
	 * subgrupo e quantidade.
	 * 
	 * @return
	 */
	public Map<Subgrupo, BigDecimal> getResumo();
	
	/**
	 * Calcula a quantidade do produto para a grade selecionada 
	 * em estoque soma à já reservada.
	 * 
	 * @param produto Produto selecionado.
	 * @param grade Grade especificada.
	 * @return Quantidade disponível.
	 */
	public BigDecimal qtdDisponivelDoProduto(Produto produto, Grade grade);
	
	/**
	 * Verifica se a quantidade do produto para a grade selecionada 
	 * em estoque mais a reserva é positiva.
	 * 
	 * @param produto Produto selecionado.
	 * @param grade Grade especificada.
	 * @return se grade do produto disponível ou não.
	 */
	public boolean temQtdDisponivelDoProduto(Produto produto, Grade grade);
}
