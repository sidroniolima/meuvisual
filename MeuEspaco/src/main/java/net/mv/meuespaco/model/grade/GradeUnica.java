package net.mv.meuespaco.model.grade;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;

/**
 * Especialização da Grade de Produtos com 
 * grade única, isto é, sem especificação de tamanho e
 * ou cor.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Entity
@DiscriminatorValue("UNICA")
@Vetoed
public class GradeUnica extends Grade implements Serializable{

	private static final long serialVersionUID = 5957386336094866864L;

	/**
	 * Construtor padrão.
	 */
	public GradeUnica() {	}
	
	/**
	 * Constrói pelo Código e Produto.
	 * 
	 * @param codigo
	 * @param produto
	 */
	public GradeUnica(Long codigo, Produto produto) {
		super(codigo, produto);
	}

	@Override
	public TipoGrade tipoDeGrade() {
		return TipoGrade.UNICA;
	}

	@Override
	public void valida() throws RegraDeNegocioException {
		
	}
	
	@Override
	public boolean temAsMesmasCaracteristicas(Grade outra) {
		return this.tipoDeGrade().equals(outra.tipoDeGrade()); 
	}
	
	@Override
	public String toString() {
		return this.tipoDeGrade().getDescricao();
	}
	
	@Override
	public int compareTo(Object o) {
		return 0;
	}
}
