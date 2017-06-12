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
import javax.persistence.Table;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.EntidadeModel;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Item (brinde) do resgate.
 * 
 * @author sidronio
 * @created 03/05/2017
 */
@Entity
@Table(name="item_resgate")
public class ItemResgate extends EntidadeModel implements IMovimentavel, ItemCalculavel, Serializable
{
	private static final long serialVersionUID = -7369109801834324692L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="resgate_codigo")
	private ResgateBrinde resgate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="produto_codigo")
	private Produto produto;
	
	private BigDecimal qtd;
	
	@Column(name="valor_unitario")
	private BigDecimal valorUnitario;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="grade_codigo")
	private Grade grade;

	public ItemResgate() 
	{
		this.qtd = BigDecimal.ZERO;
		this.valorUnitario = BigDecimal.ZERO;
	}
	
	public ItemResgate(Long codigo, ResgateBrinde resgate, Produto produto, 
			BigDecimal qtd, BigDecimal valorUnitario, Grade grade) 
	{
		this();
		
		this.codigo = codigo;
		this.resgate = resgate;
		this.produto = produto;
		this.qtd = qtd;
		this.valorUnitario = valorUnitario;
		this.grade = grade;
	}

	public ItemResgate(ResgateBrinde resgate, Produto produto, BigDecimal qtd, BigDecimal valorUnitario, Grade grade) 
	{
		this();
		
		this.resgate = resgate;
		this.produto = produto;
		this.qtd = qtd;
		this.valorUnitario = valorUnitario;
		this.grade = grade;
	}

	@Override
	public BigDecimal valorTotal() 
	{
		return this.valorUnitario.multiply(this.qtd);
	}

	@Override
	public boolean isDescontavel() throws RegraDeNegocioException 
	{
		return true;
	}

	@Override
	public void valida() throws RegraDeNegocioException 
	{
		if (null == resgate)
		{
			throw new RegraDeNegocioException("O item deve estar associadao a um resgate.");
		}
		
		if (null == produto)
		{
			throw new RegraDeNegocioException("O item deve ser um brinde.");
		}
		
		if (null == grade)
		{
			throw new RegraDeNegocioException("O brinde deve ter sua grade.");
		}
		
		if (qtd.compareTo(BigDecimal.ZERO) <= 0)
		{
			throw new RegraDeNegocioException("A quantidade deve ser maior que zero.");
		}
	}
	
	@Override
	public Produto getProduto() 
	{
		return this.produto;
	}

	@Override
	public BigDecimal getQtd() 
	{
		return this.qtd;
	}

	@Override
	public Grade getGrade() 
	{
		return this.grade;
	}

	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public ResgateBrinde getResgate() {
		return resgate;
	}
	public void setResgate(ResgateBrinde resgate) {
		this.resgate = resgate;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}
	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public void setQtd(BigDecimal qtd) {
		this.qtd = qtd;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}
}
