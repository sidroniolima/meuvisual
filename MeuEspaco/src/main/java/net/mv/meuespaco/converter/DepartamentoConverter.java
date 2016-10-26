package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.service.DepartamentoService;

@FacesConverter(forClass=Departamento.class)
public class DepartamentoConverter implements Converter {

	@Inject
	private DepartamentoService depService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		
		if (null == value || value.isEmpty()) {
			return null;
		}
		
		Departamento dep = depService.buscaPeloCodigo(new Long(value));
		
		return dep;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null == value) {
			return "";
		}
		
		Departamento dep = (Departamento) value;
		
		return dep == null ? "" : dep.getCodigo().toString();
	}

}
