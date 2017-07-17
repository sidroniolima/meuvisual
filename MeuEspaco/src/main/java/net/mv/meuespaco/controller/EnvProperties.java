package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class EnvProperties 
{
	Properties props;
	
	@PostConstruct
	public void init()
	{
		InputStream inputStream = this.getClass().getClassLoader()
				 .getResourceAsStream("environment.properties");

		props = new Properties();
        
        try 
        {
			props.load(inputStream);
		} catch (IOException e) 
        {
			throw new RuntimeException("Não foi possível carregar as propriedades.");
		}
	}
	
	public String get(String key) 
	{
		return props.getProperty(key);
	}
}
