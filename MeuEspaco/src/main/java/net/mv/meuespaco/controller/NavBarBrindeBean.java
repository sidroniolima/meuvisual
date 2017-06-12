package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import net.mv.meuespaco.service.PontuacaoService;

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
	
	@Inject
	private PontuacaoService pontuacaoSrvc;
	
	private String pesquisa;
	private String min = "2000";
	private String max = "6000";
	
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
		min = Faces.getRequestParameter("min");
		max = Faces.getRequestParameter("max");
		
		if (min.isEmpty() || max.isEmpty())
		{
			min = "2000";
			max = "6000";
		}
			
		return this.url.concat(String.format("?min=%s&max=%s", min, max)).concat("&faces-redirect=true");
	}
	
	/**
	 * Retorna o saldo dos pontos do cliente.
	 * 
	 * @return saldo da pontuação.
	 */
	public Long saldo()
	{
		return this.pontuacaoSrvc.pontosAcumuladosDoClienteLogado();
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
