package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.service.GradeService;

/**
 * Conveter da entidade Grade.
 * 
 * @author Sidronio
 * 21/12/2015
 */
@FacesConverter(forClass=Grade.class)
public class GradeConverter implements Converter {

	@Inject
	private GradeService gradeService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return gradeService.buscaPeloCodigo(Long.parseLong(value.toString()));
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null != value) {
			
			Long codigo =  ((Grade) value).getCodigo(); 
			
			return (codigo == null ? "" : codigo.toString());
		}
		
		return "";
	}

}
