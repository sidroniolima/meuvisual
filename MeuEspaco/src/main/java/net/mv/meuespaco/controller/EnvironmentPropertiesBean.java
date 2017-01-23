package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.service.impl.PropertiesLoad;

@Named("props")
@RequestScoped
public class EnvironmentPropertiesBean implements Serializable {

	private static final long serialVersionUID = 2919543257523574267L;
	
	@Inject
	private PropertiesLoad properties;
	
	public String serverName()
	{
		return properties.getProperty("server");
	}
	
	public String serverImageName()
	{
		return properties.getProperty("image-server");
	}

}
