package net.mv.meuespaco.model.grade;

import java.io.Serializable;

import javax.enterprise.inject.Vetoed;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;

/**
 * Grade com símbolos de nota musical.
 * 
 * @author sidronio
 * @created 26/01/2017
 */
@Entity
@DiscriminatorValue("MUSICAL")
@Vetoed
public class GradeDeMusica extends Grade implements GradeMusical, Serializable {

	private static final long serialVersionUID = -7735515405148440540L;

	private String simbolo;
	
	public GradeDeMusica() {	}
	
	public GradeDeMusica(String simbolo) 
	{
		this.simbolo = simbolo;
	}

	public GradeDeMusica(Long codigo, Produto produto, String simbolo)
	{
		super(codigo, produto);
		this.simbolo = simbolo;
	}
	
	@Override
	public void valida() throws RegraDeNegocioException 
	{
		if (null == this.simbolo || this.simbolo.isEmpty())
		{
			throw new RegraDeNegocioException("É necessário o símbolo musical.");
		}
	}

	@Override
	public int compareTo(Object other) {
		return this.simbolo.compareTo((String) other);
	}

	@Override
	public String getSimbolo() {
		return this.simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}
	
	@Override
	public TipoGrade tipoDeGrade() {
		return TipoGrade.MUSICAL;
	}

	@Override
	public boolean temAsMesmasCaracteristicas(Grade outra) {
		return (this.tipoDeGrade().equals(outra.tipoDeGrade()) &&
				this.getSimbolo().equals(((GradeMusical) outra).getSimbolo()));
	}

	@Override
	public String toString() {
		return this.simbolo;
	}

}
