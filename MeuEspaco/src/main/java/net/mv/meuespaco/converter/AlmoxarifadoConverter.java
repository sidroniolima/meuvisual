package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.mv.meuespaco.model.estoque.Almoxarifado;
import net.mv.meuespaco.service.AlmoxarifadoService;

/**
 * Conveter da entidade Almoxarifado.
 * 
 * @author Sidronio
 * 21/12/2015
 */
@FacesConverter(forClass=Almoxarifado.class)
public class AlmoxarifadoConverter implements Converter {

	@Inject
	private AlmoxarifadoService almService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return almService.buscaPeloCodigo(Long.parseLong(value.toString()));
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null != value) {
			
			Long codigo =  ((Almoxarifado) value).getCodigo(); 
			
			return (codigo == null ? "" : codigo.toString());
		}
		
		return "";
	}

}
