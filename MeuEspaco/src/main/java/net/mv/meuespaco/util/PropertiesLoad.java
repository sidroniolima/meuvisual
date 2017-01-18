package net.mv.meuespaco.util;

import java.io.IOException;
import java.util.Properties;

import javax.ejb.Singleton;

@Singleton
public class PropertiesLoad {

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
