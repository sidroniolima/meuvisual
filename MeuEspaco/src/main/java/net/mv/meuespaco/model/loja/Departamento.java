package net.mv.meuespaco.model.loja;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.EntidadeModel;

/**
 * Departamento ao qual pertence o produto.
 * 
 * @author Sidronio
 * 03/05/2016
 */
@Entity
@Table(name="departamento")
public class Departamento extends EntidadeModel {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(length=100)
	private String descricao;
	
	@Column(columnDefinition = "boolean default true")
	private boolean ativo;
	
	public Long getCodigo() {
		return codigo;
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
	
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	@Override
	public void valida() throws RegraDeNegocioException {
		if (null == descricao || descricao.isEmpty()) {
			throw new RegraDeNegocioException("A descrição do departamento é necessária.");
		}
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
		Departamento other = (Departamento) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
