package net.mv.meuespaco.model;

import java.io.Serializable;

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

@Entity
@Table(name="regiao")
@Vetoed
public class Regiao extends EntidadeModel implements Serializable {

	private static final long serialVersionUID = 1215340986653231006L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(name="codigo_interno", length=60)
	private String codigoInterno;

	@Column(length=60)
	private String descricao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="semana_codigo")
	private Semana semana;
	
	/**
	 * Construtor padrão.
	 */
	public Regiao() {	}
	
	/**
	 * Construto pelo código.
	 * 
	 * @param codigo
	 */
	public Regiao(Long codigo) {
		this.codigo = codigo;
	}

	public Regiao(Long codigo, String codigoInterno) 
	{
		this.codigo = codigo;
		this.codigoInterno = codigoInterno;
	}

	public Regiao(String codigoInterno) 
	{
		this.codigoInterno = codigoInterno;
	}

	@Override
	public void valida() throws RegraDeNegocioException {
		
		if (null == descricao || descricao.isEmpty()) {
			throw new RegraDeNegocioException("A descrição da região é necessária."); 
		}
	}
	
	@Override
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCodigoInterno() {
		return codigoInterno;
	}
	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public Semana getSemana() {
		return semana;
	}
	public void setSemana(Semana semana) {
		this.semana = semana;
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
		Regiao other = (Regiao) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
