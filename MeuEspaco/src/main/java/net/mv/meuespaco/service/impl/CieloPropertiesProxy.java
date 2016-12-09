package net.mv.meuespaco.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.util.PropertiesLoad;

@Stateless
public class CieloPropertiesProxy {

	@Inject
	private PropertiesLoad props;
	
	public String getUrlTransacao()
	{
		return props.getProperty("api_cielo_transacao_url");
	}
	
	public String getUrlConsulta()
	{
		return props.getProperty("api_cielo_consulta_url");
	}
	
	public String getMerchantId()
	{
		return props.getProperty("merchant_id");
	}
	
	public String getMerchantKey()
	{
		return props.getProperty("merchant_key");
	}
	
}
