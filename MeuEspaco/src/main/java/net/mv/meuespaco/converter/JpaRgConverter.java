/**
 * 
 */
package net.mv.meuespaco.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import net.mv.meuespaco.model.loja.Rg;

/**
 * @author Sidronio
 *
 */
@Converter(autoApply=true)
public class JpaRgConverter implements AttributeConverter<Rg, String>{

	
	@Override
	public String convertToDatabaseColumn(Rg cpf) {
		
		if (null == cpf) {
			return "";
		}
		
		return cpf.getValor();
	}

	@Override
	public Rg convertToEntityAttribute(String stringDoBanco) {
		return new Rg(stringDoBanco);
	}

}
