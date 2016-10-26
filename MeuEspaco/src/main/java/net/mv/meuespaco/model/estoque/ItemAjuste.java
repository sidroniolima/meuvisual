package net.mv.meuespaco.model.estoque;

import java.math.BigDecimal;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Unidade;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Um item do ajuste com produto, grade, quantidade que 
 * representa uma movimentação do ajuste.
 * 
 * @author Sidronio
 * 15/12/20915
 */ 
public class ItemAjuste {

	private Produto produto;
	private Unidade unidade;
	private Grade grade;
	private BigDecimal qtd;
	
	public ItemAjuste() {	
		qtd = BigDecimal.ZERO;
	}
	
	/**
	 * Construtor.
	 * 
	 * @param produto
	 * @param grade
	 * @param qtd
	 */
	public ItemAjuste(Produto produto, Grade grade, BigDecimal qtd) {
		this();
		
		this.produto = produto;
		this.grade = grade;
		this.qtd = qtd;
	}
	

	/**
	 * Construtor.
	 * 
	 * @param produto
	 * @param unidade
	 * @param grade
	 * @param qtd
	 */
	public ItemAjuste(Produto produto, Unidade unidade, Grade grade, BigDecimal qtd) {
		this();
		
		this.produto = produto;
		this.unidade = unidade;
		this.grade = grade;
		this.qtd = qtd;
	}

	/**
	 * Valida um item de ajuste para certificar que 
	 * os campos necessários estão presentes.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void valida() throws RegraDeNegocioException {
		
		if (null == produto) {
			throw new RegraDeNegocioException("O item de ajuste deve ser para um produto.");
		}
		
		if (qtd.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("A quantidade deve ser maior que zero.");
		}
		
		if (null == grade) {
			throw new RegraDeNegocioException("O item de ajuste deve ser para uma grade do produto.");
		}
				
	}
	
	/**
	 * Atualiza as informações do item pelo um Produto.
	 * 
	 * @param produto
	 */
	public void atualizaPeloProduto(Produto produto) {
		this.produto = produto;
		this.unidade = produto.getUnidade();
	}
	
	@Override
	public String toString() {
		return String.format("Item do produto %s com grade %s", this.produto.getDescricao(), this.grade);
	}
	
	/**
	 * @return the produto
	 */
	public Produto getProduto() {
		return produto;
	}
	/**
	 * @param produto the produto to set
	 */
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	/**
	 * @return the grade
	 */
	public Grade getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	/**
	 * @return the qtd
	 */
	public BigDecimal getQtd() {
		return qtd;
	}
	/**
	 * @param qtd the qtd to set
	 */
	public void setQtd(BigDecimal qtd) {
		this.qtd = qtd;
	}

	/**
	 * @return the unidade
	 */
	public Unidade getUnidade() {
		return unidade;
	}
	/**
	 * @param unidade the unidade to set
	 */
	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

}
