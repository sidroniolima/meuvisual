package net.mv.meuespaco.model.estoque;

import java.math.BigDecimal;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Interface para movimentação de produtos de forma 
 * dinâmica, isto é, seja para carrinho, item de escolha, ajuste,
 * etc.
 * 
 * @author Sidronio
 * 28/01/2016
 */
public interface IMovimentavel {

	/**
	 * Informa o produto para movimentação.
	 * 
	 * @return
	 */
	public Produto getProduto();
	
	/**
	 * Informa a quantidade para movimentação.
	 * 
	 * @return
	 */
	public BigDecimal getQtd();
	
	/**
	 * Informa a grade para movimentação.
	 * 
	 * @return
	 */
	public Grade getGrade();
	
}

