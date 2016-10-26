package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.service.SubgrupoService;

/**
 * @author Sidronio
 *
 */
@FacesConverter(forClass=Subgrupo.class)
public class SubrupoConverter implements Converter {

	@Inject
	private SubgrupoService subgrupoService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return subgrupoService.buscaPeloCodigo(Long.parseLong(value.toString()));
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null != value) {
			
			Long codigo =  ((Subgrupo) value).getCodigo(); 
			
			return (codigo == null ? "" : codigo.toString());
		}
		
		return "";
	}

}
