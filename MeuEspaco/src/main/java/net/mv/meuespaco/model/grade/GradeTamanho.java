package net.mv.meuespaco.model.grade;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;

/**
 * Especialização da Grade de Produtos por tamanho.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Entity
@DiscriminatorValue("TAMANHO")
@Vetoed
public class GradeTamanho extends Grade implements GradeComTamanho, Serializable{

	private static final long serialVersionUID = -458222871000565443L;
	
	@Enumerated(EnumType.STRING)
	@Column(length=60)
	private Tamanho tamanho;
	
	/**
	 * Construtor padrão.
	 */
	public GradeTamanho() {	}

	/**
	 * Constói uma Grade pelo Tamanho.
	 * 
	 * @param tamanho
	 */
	public GradeTamanho(Tamanho tamanho) {
		super();
		this.tamanho = tamanho;
	}

	/**
	 * Constrói a grade pelo Código, Produto e Tamanho.
	 * 
	 * @param codigo
	 * @param produto
	 * @param tamanho
	 */
	public GradeTamanho(Long codigo, Produto produto, Tamanho tamanho) {
		super(codigo, produto);
		this.tamanho = tamanho;
	}
	
	public GradeTamanho(Long codigo, Tamanho tamanho) {
		super(codigo);
		this.tamanho = tamanho;
	}

	@Override
	public void valida() throws RegraDeNegocioException {
		
		if (null == tamanho) {
			throw new RegraDeNegocioException("É necessário o tamanho.");
		}
	}	
	
	@Override
	public boolean temAsMesmasCaracteristicas(Grade outra) {
		return this.tipoDeGrade().equals(outra.tipoDeGrade()) && 
				this.tamanho.equals(((GradeTamanho) outra).getTamanho());
	}

	@Override
	public TipoGrade tipoDeGrade() {
		return TipoGrade.TAMANHO;
	}
	
	public Tamanho getTamanho() {
		return tamanho;
	}
	public void setTamanho(Tamanho tamanho) {
		this.tamanho = tamanho;
	}
	
	@Override
	public String toString() {
		return "Tamanho: " + this.getTamanho().getDescricao();
	}

	@Override
	public int compareTo(Object other) {
		return this.getTamanho().toString().compareTo( ((GradeTamanho) other).getTamanho().toString());
	}

}
