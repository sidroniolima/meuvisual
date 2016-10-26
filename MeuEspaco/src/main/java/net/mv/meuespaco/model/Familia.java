package net.mv.meuespaco.model;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.mv.meuespaco.exception.RegraDeNegocioException;

@Entity
@Vetoed
@Table(name="familia")
public class Familia extends EntidadeModel implements Serializable{

	private static final long serialVersionUID = 7899828826232887180L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	@Column(length=60)
	private String descricao;
	
	public Familia() {	}
	
	@Override
	public void valida() throws RegraDeNegocioException {
		
		if (null == descricao || descricao.isEmpty()) {
			throw new RegraDeNegocioException("A família deve ter uma descrição.");
		}
	}	
	
	@Override
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
		Familia other = (Familia) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
