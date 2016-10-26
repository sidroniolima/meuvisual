package net.mv.meuespaco.model.loja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Cupom de desconto.
 * 
 * @author Sidronio
 * @created 09/09/2016
 */
public class Cupom implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codigo;
	private String descricao;
	private LocalDateTime validade;
	private LocalDateTime criacao;
	private BigDecimal desconto;
	
	public Cupom() {
		this.criacao = LocalDateTime.now();
	}
	
	public Cupom(String codigo, String descricao, LocalDateTime validade, BigDecimal desconto) 
	{
		this();
		
		this.codigo = codigo;
		this.descricao = descricao;
		this.validade = validade;
		this.desconto = desconto;
	}

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public LocalDateTime getValidade() {
		return validade;
	}
	public void setValidade(LocalDateTime validade) {
		this.validade = validade;
	}
	
	public LocalDateTime getCriacao() {
		return criacao;
	}
	public void setCriacao(LocalDateTime criacao) {
		this.criacao = criacao;
	}
	
	public BigDecimal getDesconto() {
		return desconto;
	}
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
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
		Cupom other = (Cupom) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
