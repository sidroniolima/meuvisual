package net.mv.meuespaco.model.loja;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Item de venda com produto, quantidade e valor.
 * 
 * @author Sidronio
 * @created 23/08/2016
 */
@Entity
@Table(name="item_venda")
public class ItemVenda implements IMovimentavel, Serializable {

	private static final long serialVersionUID = 431603399388088158L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="venda_codigo")
	private Venda venda;
	
	private BigDecimal qtd;
	
	@Column(name="qtd_atendido")
	private BigDecimal qtdAtendido;
	
	@Column(name="valor_unitario")
	private BigDecimal valorUnitario;
	private BigDecimal desconto;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="produto_codigo")
	private Produto produto;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="grade_codigo")
	private Grade grade;
	
	/**
	 * Cria um item de venda inicializando a quantidade, 
	 * o valor e o desconto como zero.
	 */
	public ItemVenda() {
		this.qtd = BigDecimal.ZERO;
		this.qtdAtendido = BigDecimal.ZERO;
		this.valorUnitario = BigDecimal.ZERO;
		this.desconto = BigDecimal.ZERO;
	}
	
	public ItemVenda(Produto produto, BigDecimal qtd, Grade grade) {
		this();
		this.qtd = qtd;
		this.produto = produto;
		this.grade = grade;
		
		this.valorUnitario = produto.valor();
	}

	public ItemVenda(Produto produto, Grade grade) {
		this();
		this.produto = produto;
		this.valorUnitario = produto.valor();
		this.grade = grade;
	}

	/**
	 * Calcula o total do item mutiplicando a 
	 * quantidade pelo valor unitário e subtraindo o desconto.
	 * 
	 * @return valor do item.
	 */
	public BigDecimal total()
	{
		return qtd.multiply(valorUnitario).subtract(desconto); 
	}
	
	/**
	 * Verifica se um item é igual ao outro
	 * considerando o produto e a grade.
	 * @return
	 */
	public boolean ehIgual(ItemVenda outro) {
		return outro.getProduto().equals(this.getProduto()) && 
				outro.getGrade().temAsMesmasCaracteristicas(this.getGrade());
	}
	
	/** 
	 * Adiciona quantidade ao item.
	 * 
	 * @param qtd
	 */
	public void adicionaQtd(BigDecimal acrescimo) {
		this.qtd = this.qtd.add(acrescimo);
	}
	
	/**
	 * Atende um item igualando a quantidade comprada a atendida.
	 */
	public void atende()
	{
		this.qtdAtendido = this.qtd;
	}
	
	/**
	 * Zera a quantidade atendida de um item.
	 */
	public void cancelaAtendimento()
	{
		this.qtdAtendido = BigDecimal.ZERO;
	}
	
	/**
	 * Calcula o valor atendido.
	 * 
	 * @return valor atendido.
	 */
	public BigDecimal valorAtendido()
	{
		return this.valorUnitario.multiply(this.getQtdAtendido());
	}
	
	/**
	 * Verifica se um item foi atendido, mesmo que parcialmente.
	 * 
	 * @return atendio ou não.
	 */
	public boolean isAtendido()
	{
		return this.qtdAtendido.compareTo(BigDecimal.ZERO) > 0;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public Venda getVenda() {
		return venda;
	}
	public void setVenda(Venda venda) {
		this.venda = venda;
	}
	
	public BigDecimal getQtd() {
		return qtd;
	}
	public void setQtd(BigDecimal qtd) {
		this.qtd = qtd;
	}
	
	public BigDecimal getQtdAtendido() {
		return qtdAtendido;
	}
	public void setQtdAtendido(BigDecimal qtdAtendido) {
		this.qtdAtendido = qtdAtendido;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}
	
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {

	}

	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
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
		ItemVenda other = (ItemVenda) obj;
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
