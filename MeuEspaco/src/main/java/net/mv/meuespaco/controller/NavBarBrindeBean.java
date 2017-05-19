package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Controller do NavBar dos Brindes.
 * 
 * @author sidronio
 * @created 11/05/2017
 */
@Named
@ViewScoped
public class NavBarBrindeBean implements Serializable 
{
	private static final long serialVersionUID = 1718570606292967713L;

	private final String url = "/private/brinde/lista-brindes-da-pesquisa-ou-valor.xhtml"; 
	
	private String pesquisa;
	private String min;
	private String max;
	
	/**
	 * Cria a url da pesquisa pelos campos descritivos.
	 */
	public String urlPesquisaDescritiva()
	{
		return this.url.concat("?pesquisa=" + pesquisa).concat("&faces-redirect=true");
	}
	
	/**
	 * Cria a url da pesquisa por valor.
	 * 
	 * @return
	 */
	public String urlPesquisaPorValor()
	{
		return this.url.concat(String.format("?min=%&max=%", min, max)).concat("&faces-redirect=true");
	}

	public String getPesquisa() {
		return pesquisa;
	}
	public void setPesquisa(String pesquisa) {
		this.pesquisa = pesquisa;
	}

	public String getMin() {
		return min;
	}
	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}
	public void setMax(String max) {
		this.max = max;
	}
	
}
