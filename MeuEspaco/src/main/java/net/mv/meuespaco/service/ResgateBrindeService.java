package net.mv.meuespaco.service;

import java.util.List;

import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ResgateBrinde;
/**
 * Abstração da camada Service para a entidade ResgateBrinde.
 * 
 * @author sidronio
 * @created 02/06/2017
 */
public interface ResgateBrindeService extends SimpleServiceLayer<ResgateBrinde, Long> 
{
	/**
	 * Cria um resgate com os itens do carrinho.
	 * 
	 * @param itensDoCarrinho escolhidos pelo cliente.
	 * @param saldoAtual 
	 * @para cliente dono do carrinho
	 * @return resgate criado a partir dos itens.
	 */
	public ResgateBrinde criaResgateDeCarrinho(List<? extends IMovimentavel> itensDoCarrinho, Cliente cliente, Long saldoAtual);

	/**
	 * Retorna um resgate com seus itens pelo código.
	 * 
	 * @param codigo
	 * @return
	 */
	public ResgateBrinde buscarComItensPeloCodigo(Long codigo);

	/**
	 * Lista os últimos 10 resgates do cliente.
	 * @param cliente
	 * @return 10 últimos resgates do cliente.
	 */
	public List<ResgateBrinde> utlimosResgatesDoCliente(Cliente cliente);

	/**
	 * Busca um resgate com todos os relacionamentos pelo código.
	 * 
	 * @param codigo
	 * @return
	 */
	public ResgateBrinde buscarCompletaPeloCodigo(Long codigo);
}
