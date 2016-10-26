package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.service.GrupoService;

/**
 * @author Sidronio
 *
 */
@FacesConverter(forClass=Grupo.class)
public class GrupoConverter implements Converter {

	@Inject
	private GrupoService semanaService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return semanaService.buscaPeloCodigo(Long.parseLong(value.toString()));
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null != value) {
			
			Long codigo =  ((Grupo) value).getCodigo(); 
			
			return (codigo == null ? "" : codigo.toString());
		}
		
		return "";
	}

}
