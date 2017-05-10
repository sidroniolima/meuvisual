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
import net.mv.meuespaco.model.Subgrupo;

/**
 * Brinde para resgate de acordo com a pontuação dos clientes.
 * 
 * @author sidronio
 * @created 03/05/2017
 */
@Entity
@Table(name="brinde")
public class Brinde extends EntidadeModel implements Serializable
{
	private static final long serialVersionUID = -2560546411890465909L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	private String descricao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="subgrupo_codigo")
	private Subgrupo subgrupo;
	
	private BigDecimal valor;
	
	@Column(columnDefinition = "TEXT")
	private String detalhes;
	
	private boolean ativo;
	
	@Override
	public void valida() throws RegraDeNegocioException 
	{
		if (null == descricao || (null != descricao && this.descricao.isEmpty()))
		{
			throw new RegraDeNegocioException("A descrição deve ser informada.");
		}

		if (null == valor)
		{
			throw new RegraDeNegocioException("O valor deve ser informado.");
		}
	}

	@Override
	public Long getCodigo() 
	{
		return this.codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}	

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Subgrupo getSubgrupo() {
		return subgrupo;
	}
	public void setSubgrupo(Subgrupo subgrupo) {
		this.subgrupo = subgrupo;
	}

	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getDetalhes() {
		return detalhes;
	}
	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		Brinde other = (Brinde) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
	
}
