package net.mv.meuespaco.service;

import java.math.BigDecimal;
import java.util.Map;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Interface da Camada Service para reserva de 
 * produtos selecionados na adição do produto ao carrinho.
 * Isso evita que produtos sejam selecionados sem efetiva
 * quantidade disponível.
 * 
 * @author Sidronio
 * 14/12/2015
 */
public interface ReservaProdutoService {

	/**
	 * Adiciona a quantidade de um produto a reserva.
	 * 
	 * @param produto Produto selecionado.
	 * @param qtd Quantidade requisitada.
	 */
	public void adicionaReserva(Produto produto, Grade grade, BigDecimal qtd);
	
	/**
	 * Quantidade já reservada do produto.
	 * 
	 * @param produto Produto selecinado.
	 * @return Quantidade já reservada do produto.
	 */
	public BigDecimal qtdReservadaDoProduto(Produto produto, Grade grade);
	
	/**
	 * Reserva de produtos pro grade.
	 * 
	 * @return Reserva de todos os produtos.
	 */
	public Map<Produto, Map<Grade, BigDecimal>> getReserva();

	/**
	 * Remove a quantidade específica do produto.
	 * 
	 * @param produto Produto para remoção da qtd.
	 * @param qtd Quantidade para remoção.
	 */
	public void removeReserva(Produto produto, Grade grade, BigDecimal qtd);
	
	/**
	 * Retorna as grades reservadas do produto e sua quantidade.
	 * 
	 * @return listagem das grades com quantidade do produto reservadas.
	 */
	public Map<Grade, BigDecimal> gradesReservadasDoProduto(Produto produto);

	/**
	 * Imprime a reserva para conferência das peças.
	 */
	void imprimeReserva();
}
