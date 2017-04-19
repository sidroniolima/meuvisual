package net.mv.meuespaco.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.ejb.Singleton;

@Singleton
public class PropertiesLoad implements Serializable {

	private static final long serialVersionUID = -6319760600855727710L;

	private final String filePath = "environment.properties";
	
	private Properties props;
	
	public PropertiesLoad() 
	{
		try {
			props = new Properties();
			props.load(getClass().getClassLoader().getResourceAsStream(filePath));

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public String getProperty(String propertyName)
	{
		return props.getProperty(propertyName).toString();
	}
	
}
