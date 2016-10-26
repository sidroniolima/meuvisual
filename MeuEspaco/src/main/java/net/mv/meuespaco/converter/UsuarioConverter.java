package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.service.UsuarioService;

/**
 * Converter para a entidade Usuario.
 * 
 * @author Sidronio
 * 05/01/2016
 */
@FacesConverter(forClass=Usuario.class)
public class UsuarioConverter implements Converter {

	@Inject
	private UsuarioService usuarioSrvc;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return usuarioSrvc.buscaPeloCodigo(new Long(value));
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null == value) {
			return "";
		}
		
		Long codigo = ((Usuario) value).getCodigo();
		
		return codigo == null ? "" : codigo.toString();
	}

}
