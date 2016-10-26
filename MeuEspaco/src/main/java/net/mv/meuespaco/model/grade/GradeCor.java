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
 * Especialização da Grade de Produto por Cores.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Entity
@DiscriminatorValue("COR")
@Vetoed
public class GradeCor extends Grade implements GradeComCor, Serializable {

	private static final long serialVersionUID = -123426426930870737L;
	
	@Enumerated(EnumType.STRING)
	@Column(length=60)
	private Cor cor;
	
	/**
	 * Construtor padrão.
	 */
	public GradeCor() {	}
	
	/**
	 * Constói uma Grade pela Cor.
	 * 
	 * @param cor
	 */
	public GradeCor(Cor cor) {
		this.cor = cor;
	}

	/**
	 * Construtor da Grade de Cor pelo Produto e
	 * Cor.
	 * 
	 * @param codigo
	 * @param produto
	 * @param cor
	 */
	public GradeCor(Long codigo, Produto produto, Cor cor) {
		super(codigo, produto);
		this.cor = cor;
	}

	public GradeCor(long codigo, Cor cor) {
		super(codigo);
		this.cor = cor;
	}

	@Override
	public TipoGrade tipoDeGrade() {
		return TipoGrade.COR;
	}
	
	@Override
	public void valida() throws RegraDeNegocioException {
		
		if (null == cor) {
			throw new RegraDeNegocioException("É necessário a cor.");
		}
	}
	
	@Override
	public boolean temAsMesmasCaracteristicas(Grade outra) {
		return this.tipoDeGrade().equals(outra.tipoDeGrade()) && 
				this.cor.equals(((GradeCor) outra).getCor());
	}

	public Cor getCor() {
		return cor;
	}
	public void setCor(Cor cor) {
		this.cor = cor;
	}

	@Override
	public String toString() {
		return "Cor: " + this.getCor().getDescricao();
	}
	
	@Override
	public int compareTo(Object other) {
		return this.getCor().compareTo( ((GradeCor) other).getCor());
	}
	
}
