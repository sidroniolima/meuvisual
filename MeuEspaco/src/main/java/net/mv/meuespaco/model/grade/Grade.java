package net.mv.meuespaco.model.grade;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;

/**
 * Abstração de Grade de Produtos.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING, name="tipo_grade")
@Table(name="grade")
@Vetoed
public abstract class Grade implements Comparable<Object>, Serializable {

	private static final long serialVersionUID = -3950972821185112373L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Produto produto;
	
	private String localizacao;
	
	/**
	 * Construtor padrão.
	 */
	public Grade() {	}
	
	/**
	 * Constrói pelo Código e Produto.
	 * 
	 * @param codigo 
	 * @param produto
	 */
	public Grade(Long codigo, Produto produto) {
		this.codigo = codigo;
		this.produto = produto;
	}

	public Grade(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Retorna o tipo de grade: Cor, Tamanho ou 
	 * Cor e Tamanho.
	 * 
	 * @return
	 */
	public abstract TipoGrade tipoDeGrade();
	
	/**
	 * Valida a grade de acordo com seu tipo.
	 * Em suma deve ser preenchido os campos da grade, 
	 * exemplo: para grade de cor e tamanho os dois devem 
	 * estar presentes.

	 * @throws RegraDeNegocioException
	 */
	public void valida() throws RegraDeNegocioException{	
		
		if (null == produto) {
			throw new RegraDeNegocioException("Não há produto para a grade.");
		}
		
		if (localizacao.isEmpty()) {
			throw new RegraDeNegocioException("É necessário definir a localização.");
		}
	}
	
	/**
	 * Verifica se 2 grades tem as mesmas características, isto é, tem 
	 * os mesmo tipos, a mesma cor, ou o mesmo tamanho, ou a cor e tamanho iguais, 
	 * ou a letra.
	 * 
	 * @return
	 */
	public abstract boolean temAsMesmasCaracteristicas(Grade outra);

	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	/**
	 * @return the localizacao
	 */
	public String getLocalizacao() {
		return localizacao;
	}
	/**
	 * @param localizacao the localizacao to set
	 */
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
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
		Grade other = (Grade) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
	//TODO: Try to use getClass instead of instanceof in the equals method.

}
