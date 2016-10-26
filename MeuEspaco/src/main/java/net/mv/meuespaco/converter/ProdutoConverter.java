package net.mv.meuespaco.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.ProdutoService;

/**
 * Conveter da entidade Produto.
 * 
 * @author Sidronio
 * 21/12/2015
 */
@FacesConverter(forClass=Produto.class)
public class ProdutoConverter implements Converter {

	@Inject
	private ProdutoService produtoService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (null == value || value.isEmpty()) {
			return null;
		}
		
		return produtoService.buscaPeloCodigo(Long.parseLong(value.toString()));
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (null != value) {
			
			Long codigo =  ((Produto) value).getCodigo(); 
			
			return (codigo == null ? "" : codigo.toString());
		}
		
		return "";
	}

}
