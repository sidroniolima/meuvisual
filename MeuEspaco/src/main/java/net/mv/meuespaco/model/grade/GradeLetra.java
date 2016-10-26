package net.mv.meuespaco.model.grade;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;

/**
 * Especialização da Grade de Produto por Letra.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Entity
@DiscriminatorValue("LETRA")
@Vetoed
public class GradeLetra extends Grade implements GradeComLetra, Serializable {

	private static final long serialVersionUID = -5263251077637478303L;
	
	@Column(columnDefinition="VARCHAR(1)")
	private char letra;
	
	/**
	 * Construtor padrão.
	 */
	public GradeLetra() {	}
	
	@Override
	public void valida() throws RegraDeNegocioException {
		if ('\u0000' == letra) {
			throw new RegraDeNegocioException("É necessário informar a letra.");
		}
	}
	
	@Override
	public boolean temAsMesmasCaracteristicas(Grade outra) {
		return this.tipoDeGrade().equals(outra.tipoDeGrade()) && 
				this.letra == ((GradeLetra) outra).getLetra();
	}
	
	@Override
	public String toString() {
		return "Letra: " + letra;
	}
	
	/**
	 * Constrói a grade com a letra.
	 * @param letra
	 */
	public GradeLetra(char letra) {
		this.letra = letra;
	}

	public GradeLetra(long codigo, Produto produto, char letra) {
		super(codigo, produto);
		this.letra = letra;
	}

	public GradeLetra(long codigo, char letra) {
		super(codigo);
		this.letra = letra;
	}

	@Override
	public int compareTo(Object o) {
		return  getLetra() - (char) o;
	}
	
	@Override
	public TipoGrade tipoDeGrade() {
		return TipoGrade.LETRA;
	}

	@Override
	public char getLetra() {
		return this.letra;
	}
	/**
	 * @param letra the letra to set
	 */
	public void setLetra(char letra) {
		this.letra = letra;
	}

}
