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
 * Especialização da Grade de Produtos por tamanho 
 * e cor.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Entity
@DiscriminatorValue("COR_E_TAMANHO")
@Vetoed
public class GradeCorETamanho extends Grade implements GradeComCor, GradeComTamanho, Serializable{

	private static final long serialVersionUID = 1897874819846443851L;

	@Enumerated(EnumType.STRING)
	@Column(length=60)
	private Tamanho tamanho;
	
	@Enumerated(EnumType.STRING)
	@Column(length=60)
	private Cor cor;
	
	/**
	 * Construtor padrão.
	 */
	public GradeCorETamanho() {	}
	
	/**
	 * Constói uma Grade pela Cor e Tamanho.
	 * 
	 * @param cor
	 * @param tamanho
	 */
	public GradeCorETamanho(Cor cor, Tamanho tamanho) {
		this.tamanho = tamanho;
		this.cor = cor;
	}	
	
	/**
	 * Constrói a grade pelo Código, Produto e Tamanho.
	 * 
	 * @param codigo
	 * @param produto
	 * @param tamanho
	 */
	public GradeCorETamanho(Long codigo, Produto produto, Cor cor, Tamanho tamanho) {
		super(codigo, produto);
		this.tamanho = tamanho;
		this.cor = cor;
	}
	
	public GradeCorETamanho(long codigo, Cor cor, Tamanho tamanho) {
		super(codigo);
		this.tamanho = tamanho;
		this.cor = cor;
	}

	@Override
	public void valida() throws RegraDeNegocioException {
		
		if (null == cor && null == tamanho) {
			throw new RegraDeNegocioException("É necessário o tamanho e a cor.");
		}
		
		if (null == cor) {
			throw new RegraDeNegocioException("É necessário a cor.");
		}
		
		if (null == tamanho) {
			throw new RegraDeNegocioException("É necessário o tamanho.");
		}
	}
	
	@Override
	public boolean temAsMesmasCaracteristicas(Grade outra) {
		return this.tipoDeGrade().equals(outra.tipoDeGrade()) && 
				this.cor.equals(((GradeCorETamanho) outra).getCor()) &&
				this.tamanho.equals(((GradeCorETamanho) outra).getTamanho());
	}

	@Override
	public TipoGrade tipoDeGrade() {
		return TipoGrade.COR_E_TAMANHO;
	}
	
	public Tamanho getTamanho() {
		return tamanho;
	}
	public void setTamanho(Tamanho tamanho) {
		this.tamanho = tamanho;
	}

	public Cor getCor() {
		return cor;
	}
	public void setCor(Cor cor) {
		this.cor = cor;
	}
	
	@Override
	public String toString() {
		return String.format("Cor: %s, Tamanho: %s", 
				this.getCor().getDescricao(), this.getTamanho().getDescricao());
	}
	
	@Override
	public int compareTo(Object other) {
		return this.getTamanho().toString().compareTo( ((GradeCorETamanho) other).getTamanho().toString());
	}
}
