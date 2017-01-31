package net.mv.meuespaco.factory;

import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeCor;
import net.mv.meuespaco.model.grade.GradeCorETamanho;
import net.mv.meuespaco.model.grade.GradeDeMusica;
import net.mv.meuespaco.model.grade.GradeLetra;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.model.grade.GradeUnica;

/**
 * Fábrica de objetos Grade.
 * 
 * @author Sidronio
 * 19/11/2015
 */
public class GradeFactory {

	public static Grade cria(TipoGrade tipoGrade) {
		
		if (tipoGrade.equals(TipoGrade.UNICA)) {
			return new GradeUnica();
		}
		
		if (tipoGrade.equals(TipoGrade.COR)) {
			return new GradeCor();
		}
		
		if (tipoGrade.equals(TipoGrade.TAMANHO)) {
			return new GradeTamanho();
		}
		
		if (tipoGrade.equals(TipoGrade.COR_E_TAMANHO)) {
			return new GradeCorETamanho();
		}
		
		if (tipoGrade.equals(TipoGrade.LETRA)) {
			return new GradeLetra();
		}
		
		if (tipoGrade.equals(TipoGrade.MUSICAL)) {
			return new GradeDeMusica();
		}
	
		
		return null;
	}
	
}
