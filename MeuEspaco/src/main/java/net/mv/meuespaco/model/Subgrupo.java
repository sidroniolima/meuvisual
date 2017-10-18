package net.mv.meuespaco.model;

import java.io.Serializable;
import java.util.List;

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

/**
 * @author Sidronio
 *
 */
@Entity
@Table(name="sub_grupo")
public class Subgrupo extends EntidadeModel implements Serializable{

	private static final long serialVersionUID = 7899828826232887180L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;

	@Column(length=60)
	private String descricao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="grupo_codigo")
	private Grupo grupo;
	
	@OneToMany(fetch=FetchType.LAZY, 
			mappedBy="subgrupo")
	private List<Produto> produtos;
	
	/**
	 * Default constructor.
	 */
	public Subgrupo() {	}
	
	/**
	 * Constructor with params.
	 * 
	 * @param codigo
	 * @param descricao
	 */
	public Subgrupo(Long codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	@Override
	public void valida() throws RegraDeNegocioException {
		
		if (null == descricao || descricao.isEmpty()) {
			throw new RegraDeNegocioException("O subgrupo deve ter uma descrição.");
		}
		
		if (null == grupo) {
			throw new RegraDeNegocioException("O subgrupo deve pertencer a um grupo.");
		}
	}
	
	@Override
	public String toString() {
		
		if (null == this.getGrupo()) {
			return descricao;
		}
		
		return grupo + ", " + descricao;
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
	
	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
	/**
	 * @return the produtos
	 */
	public List<Produto> getProdutos() {
		return produtos;
	}
	/**
	 * @param produtos the produtos to set
	 */
	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
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
		Subgrupo other = (Subgrupo) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
	
	
}
