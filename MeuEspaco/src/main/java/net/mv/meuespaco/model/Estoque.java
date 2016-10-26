package net.mv.meuespaco.model;

import java.util.ArrayList;
import java.util.List;

import net.mv.meuespaco.exception.EstoqueException;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.Tamanho;

public class Estoque {

	private List<Movimentacao> movimentacoes;
	
	public Estoque() {
		movimentacoes = new ArrayList<Movimentacao>();
	}
	
	public void adicionaMovimentacao(Movimentacao movimentacao) {
		this.movimentacoes.add(movimentacao);
	}
	
	/**
	 * Retorna a quantidade por Grade, isto é, de um produto, cor e 
	 * tamanho específicos.
	 * 
	 * @param grade
	 * @return
	 * @throws EstoqueException 
	 */
	public Long qtdDoProdutoPorGrade(Produto produto, Grade grade) throws EstoqueException {
		
		Long qtd = 0L;
		
		if (!produto.temGrade()) {
			throw new EstoqueException("Este produto não possui grades.");
		}
		
		for (Movimentacao movimentacao : movimentacoes) {
		
			if (movimentacao.getGrade().equals(grade)) {
				qtd += movimentacao.getQtd();
			}
		}
		
		return qtd;
	}
	
	/**
	 * Retorna a quantidade pelo produto.
	 * 
	 * @param produto
	 * @return
	 */
	public Long qtdDoProduto(Produto produto) {
		Long qtd = 0L;
		
		for (Movimentacao movimentacao : movimentacoes) {
			
			if (movimentacao.getProduto().equals(produto)) {
				qtd += movimentacao.getQtd();
			}
		}
		
		return qtd;
	}
	
	
	/**
	 * Retorna a quantidade do produto de um tamanho específico.
	 * 
	 * @param produto
	 * @param tamanho
	 * @return
	 * @throws EstoqueException 
	 */
	public Long qtdDoProdutoPorTamanho(Produto produto, Tamanho tamanho) throws EstoqueException {
		Long qtd = 0L;
		
		if (!produto.temGrade()) {
			throw new EstoqueException("Este produto não possui grades.");
		}
		
		for (Movimentacao movimentacao : movimentacoes) {
			
			if (movimentacao.getProduto().equals(produto)
					&& ((GradeTamanho)movimentacao.getGrade()).getTamanho() == tamanho) {
				qtd += movimentacao.getQtd();
			}
		}
		
		return qtd;
	}
	
	/**
	 * Retorna a quantidade do produto de uma cor específica.
	 * 
	 * @param produto
	 * @param cor
	 * @return
	 * @throws EstoqueException 
	 */
	public Long qtdDoProdutoPorCor(Produto produto, Cor cor) throws EstoqueException {
		Long qtd = 0L;
		
		if (!produto.temGrade()) {
			throw new EstoqueException("Este produto não possui grades.");
		}
		
		for (Movimentacao movimentacao : movimentacoes) {
			
			if (movimentacao.getProduto().equals(produto)
					&& ((GradeCor) movimentacao.getGrade()).getCor() == cor) {
				qtd += movimentacao.getQtd();
			}
		}
		
		return qtd;
	}
}
