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
	
	/**
	 * Retorna o Absolute Path para o arquivo de exportação 
	 * do ERP de Creditos.
	 * 
	 * @return file path.
	 */
	public String absolutePathToCreditos()
	{
		return this.getExportPath().concat(props.getProperty("file-name-creditos"));
	}
	
	/**
	 * Retorna o Absolute Path para o arquivo de exportação 
	 * do ERP de Equipes.
	 * 
	 * @return file path.
	 */
	public String absolutePathToEquipes()
	{
		return this.getExportPath().concat(props.getProperty("file-name-equipes"));
	}

	/**
	 * Retorna o Absolute Path para o arquivo de exportação 
	 * do ERP de Clientes.
	 * 
	 * @return file path.
	 */
	public String absolutePathToClientes() {
		return this.getExportPath().concat(props.getProperty("file-name-clientes"));
	}
	
}
