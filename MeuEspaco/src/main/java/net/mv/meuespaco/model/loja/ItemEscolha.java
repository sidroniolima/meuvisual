package net.mv.meuespaco.model.loja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.StringJoiner;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Itens da escolha do cliente que correspondem  
 * aos itens do carrinho.
 * 
 * @author Sidronio
 * 08/01/2016
 */
@Entity
@Vetoed
@Table(name="itens_escolha")
public class ItemEscolha implements IMovimentavel, ItemCalculavel, Serializable {

	private static final long serialVersionUID = -4479351395614624552L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="escolha_codigo")
	private Escolha escolha;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Produto produto;
	
	@Column(name="valor_unitario")
	private BigDecimal valorUnitario;
	
	private BigDecimal qtd;
	
	private BigDecimal qtdAtendido;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Grade grade;
	
	/**
	 * Cria um item de escolha.
	 */
	public ItemEscolha() {	
		qtdAtendido = BigDecimal.ZERO;
	}
	
	/**
	 * Construtor do Item pelo Produto e Qtd.
	 * 
	 * @param produto
	 * @param qtd
	 */
	public ItemEscolha(Produto produto, BigDecimal qtd) {
		this();
		this.produto = produto;
		this.valorUnitario = produto.valor();
		this.qtd = qtd;
	}
	
	/**
	 * Construtor do Item pelo Produto, Qtd e grade.
	 * 
	 * @param produto
	 * @param qtd
	 * @param grade
	 */
	public ItemEscolha(Produto produto, BigDecimal qtd, Grade grade) {
		this(produto, qtd);
		this.grade = grade;
	}
	
	/**
	 * Construtor do Item pelo Produto, Qtd e grade.
	 * 
	 * @param produto
	 * @param qtd
	 * @param grade
	 */
	public ItemEscolha(Produto produto, BigDecimal qtd, BigDecimal valorUnitario, Grade grade) {
		this(produto, qtd);
		this.grade = grade;
		this.valorUnitario = valorUnitario;
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
	}

	/**
	 * Atualiza a quantidade do item.
	 * @param qtd Quantidade para acrescentar.
	 */
	public void atualizaQtd(BigDecimal qtd) {
		this.qtd = this.qtd.add(qtd);
	}
	
	/**
	 * Verifica se um item foi atendido ou parcialmente atendido.
	 * 
	 * @return True se atendido ou False, se não.
	 */
	public boolean isAtendido() {
		
		if (null == this.getQtdAtendido()) {
			return false;
		}
		
		return qtdAtendido.compareTo(BigDecimal.ZERO) > 0;
	}
	
	/**
	 * Muda a quantidade atendida caso seja 0 ou zera 
	 * caso seja > 0.
	 */
	public void atendeOuCancelaAtendimento() {
		
		if (null != qtdAtendido && qtdAtendido.compareTo(BigDecimal.ZERO) > 0) {
			qtdAtendido = BigDecimal.ZERO;
		} else {
			qtdAtendido = qtd;
		}
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
	
	@Override
	public boolean isDescontavel() throws RegraDeNegocioException {
		return produto.isDescontavel();
	}
	
	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	/**
	 * @return the escolha
	 */
	public Escolha getEscolha() {
		return escolha;
	}
	/**
	 * @param escolha the escolha to set
	 */
	public void setEscolha(Escolha escolha) {
		this.escolha = escolha;
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
	 * Informa o valor do item ou do produto caso 
	 * não fora previamente informado o do item.
	 * 
	 * @return valor do item.
	 */
	public BigDecimal getValorUnitario() {
		
		if (null == valorUnitario) {
			return produto.valor();
		}
		
		return valorUnitario.setScale(2, RoundingMode.HALF_UP);
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
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
	 * @return the qtdAtendido
	 */
	public BigDecimal getQtdAtendido() {
		
		if (null != qtdAtendido) {
			return qtdAtendido.setScale(0);
		}
		
		return qtdAtendido;
	}
	
	
	/**
	 * Calcula o valor atendido.
	 * @return
	 */
	public BigDecimal valorAtendido() 
	{
		return this.qtdAtendido.multiply(this.valorUnitario);
	}
	
	/**
	 * @param qtdAtendido the qtdAtendido to set
	 */
	public void setQtdAtendido(BigDecimal qtdAtendido) {
		this.qtdAtendido = qtdAtendido;
	}

	@Override
	public String toString() 
	{
		StringJoiner joiner = new StringJoiner(";");
		joiner.add(this.getProduto().getCodigoInterno());
		joiner.add(this.getQtd().toString());
		joiner.add(this.getValorUnitario().toString());
		joiner.add(this.valorTotal().toString());
		
		return joiner.toString();
	}

	/**
	 * Gera as informações para CSV.
	 * @return
	 */
	public String toCsv() 
	{
		StringJoiner joiner = new StringJoiner(";");
		joiner.add(this.getProduto().getCodigoInterno());
		joiner.add(this.getQtdAtendido().toString());
		joiner.add(this.getValorUnitario().toString());
		joiner.add(this.valorAtendido().toString());
		
		return joiner.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemEscolha other = (ItemEscolha) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
