package net.mv.meuespaco.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Proxy das propriedades para os arquivos de Exportação do ERP.
 * 
 * @author sidronio
 * @created 09/01/2017
 */
@Stateless
public class ExportPropertiesProxy {

	@Inject
	private PropertiesLoad props;
	
	public String getExportPath()
	{
		return props.getProperty("export-path");
	}
	
	public String getFileNameCreditos()
	{
		return props.getProperty("file-name-creditos");
	}
	
	public String absolutePathToCreditos()
	{
		return this.getExportPath().concat(this.getFileNameCreditos());
	}
	
}
