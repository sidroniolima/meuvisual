package net.mv.meuespaco.model.loja;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Transient;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Grupo de produto, quantidade e grade específica 
 * formando um item do carrinho de escolha.
 * 
 * @author Sidronio
 * 08/12/2015
 */
public class ItemCarrinho implements IMovimentavel {

	private Produto produto;
	private BigDecimal qtd;
	private BigDecimal valorUnitario;
	private Grade grade;

	/**
	 * Construtor padrão
	 */
	public ItemCarrinho() {	
		this.qtd = BigDecimal.ONE;
	}
	
	/**
	 * Constutor com os campos da instância.
	 * 
	 * @param produto
	 * @param qtd
	 * @param grade
	 */
	public ItemCarrinho(Produto produto, BigDecimal qtd, Grade grade) {
		this();
		
		this.produto = produto;
		this.qtd = qtd;
		this.grade = grade;
		
		if (null != produto) {
			this.valorUnitario = 
					produto.valor().setScale(2, RoundingMode.HALF_UP);
		} else
		{
			valorUnitario = BigDecimal.ZERO;
		}
			
	}
	
	/**
	 * Valida um item observado se há produto, grade e
	 * a quantidade é maior que zero.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void valida() throws RegraDeNegocioException{
		
		if (null == produto) {
			throw new RegraDeNegocioException("O item deve ser de um produto.");
		}
		
		if (null == grade) {
			throw new RegraDeNegocioException("O item deve estar associado a uma grade.");
		}
		
		if (qtd.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("A quantidade deve ser maior que zero.");
		}
		
		if (this.valorUnitario.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("O valor do item não está correto.");
		}
	}
	
	/**
	 * Soma a nova quantidade a já existente.
	 * 
	 * @param novaQtd
	 */
	public void atualizaQtd(BigDecimal novaQtd) {
		this.setQtd(this.qtd.add(novaQtd));
	}
	
	/**
	 * Calcula o valor total do item multiplicando quantidade 
	 * pelo valor unitário.
	 * 
	 * @return
	 */
	public BigDecimal valorTotal() {
		return getValorUnitario().multiply(qtd).setScale(2, RoundingMode.HALF_UP);
	}	

	/**
	 * Verifica se o item pertence a um produto descontável.
	 * 
	 * @return
	 * @throws RegraDeNegocioException
	 */
	public boolean isDescontavel() throws RegraDeNegocioException {
		return produto.isDescontavel();
	}
	
	@Transient
	public Grupo getGrupo() 
	{
		return produto.getSubgrupo().getGrupo();
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
	
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grade == null) ? 0 : grade.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemCarrinho other = (ItemCarrinho) obj;
		if (grade == null) {
			if (other.grade != null)
				return false;
		} else if (!grade.equals(other.grade))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}

}


