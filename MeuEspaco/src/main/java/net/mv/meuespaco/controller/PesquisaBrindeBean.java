package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.BrindeService;
import net.mv.meuespaco.util.IConstants;
import net.mv.meuespaco.util.Paginator;

/**
 * Pesquisa de brinde.
 * 
 * @author sidronio
 * @created 11/05/2017
 */
@Named
@ViewScoped
public class PesquisaBrindeBean implements Serializable
{
	private static final long serialVersionUID = 5036262398331477893L;
	
	@Inject
	private BrindeService brindeSrvc;
	
	private List<Produto> brindes;
	
	private Paginator paginator;
	
	@Inject @Param
	private String pesquisa;
	
	@Inject @Param
	private String min;
	
	@Inject @Param
	private String max;
	
	public PesquisaBrindeBean() 
	{
		paginator = new Paginator(IConstants.QTD_DE_PRODUTOS_POR_PAGINA);
	}

	@PostConstruct
	public void init()
	{
		System.out.println(String.format("Pesquisa: %s, Min: %s, Max: %s", pesquisa, min, max));
		if (null != pesquisa)
		{
			brindes = this.brindeSrvc.pesquisaDiversa(pesquisa, this.getPaginator());
			return;
		}
		
		if (null != min && null != max)
		{
			brindes = this.brindeSrvc.filtraPeloValor(new BigDecimal(min), new BigDecimal(max), this.getPaginator());
			return;
		}
	}
	
	public Paginator getPaginator() 
	{
		return paginator;
	}
	
	public List<Produto> getBrindes() 
	{
		return brindes;
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
