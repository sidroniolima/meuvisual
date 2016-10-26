package net.mv.meuespaco.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.mv.meuespaco.exception.RegraDeNegocioException;

@Entity
@Table(name="grupo")
@Vetoed
public class Grupo extends EntidadeModel implements Serializable{

	private static final long serialVersionUID = 7899828826232887180L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	@Column(length=60)
	private String descricao;
	
	@Column(columnDefinition = "boolean default true")
	private boolean descontavel;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="familia_codigo")
	private Familia familia;

	@OneToMany(fetch=FetchType.LAZY, mappedBy="grupo")
	private Set<Subgrupo> subgrupos;

	public Grupo() {	
		subgrupos = new HashSet<Subgrupo>();
		descontavel = true;
	}
	
	/**
	 * Constrói um grupo pelo código e descrição.
	 * 
	 * @param codigo
	 * @param descricao
	 */
	public Grupo(Long codigo, String descricao) {
		this();		
		
		this.codigo = codigo;
		this.descricao = descricao;
	}

	@Override
	public void valida() throws RegraDeNegocioException {
	
		if (null == descricao || descricao.isEmpty()) {
			throw new RegraDeNegocioException("O grupo deve ter uma descrição.");
		}
		
		if (null == familia) {
			throw new RegraDeNegocioException("O grupo deve pertencer a uma família.");
		}
	}
	
	@Override
	public String toString() {
		
		if (null == this.getFamilia()) {
			return descricao;
		}
		
		return familia.getDescricao() + ", " + descricao;
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
	
	public boolean isDescontavel() {
		return descontavel;
	}
	public void setDescontavel(boolean descontavel) {
		this.descontavel = descontavel;
	}

	public Familia getFamilia() {
		return familia;
	}
	public void setFamilia(Familia familia) {
		this.familia = familia;
	}
	
	/**
	 * @return the subgrupos
	 */
	public Set<Subgrupo> getSubgrupos() {
		return subgrupos;
	}
	/**
	 * @param subgrupos the subgrupos to set
	 */
	public void setSubgrupos(Set<Subgrupo> subgrupos) {
		this.subgrupos = subgrupos;
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
		Grupo other = (Grupo) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
