package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.ClienteService;

/**
 * Conveter da entidade Cliente.
 * 
 * @author Sidronio
 * 21/12/2015
 */
@FacesConverter(forClass=Cliente.class)
public class ClienteConverter implements Converter {

	@Inject
	private ClienteService clienteService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return clienteService.buscaPeloCodigo(Long.parseLong(value.toString()));
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null != value) {
			
			Long codigo =  ((Cliente) value).getCodigo();
			
			return (value == null ? "" : codigo.toString());
		}
		
		return "";
	}

}
