package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;

import net.mv.meuespaco.annotations.CarrinhoBrindeBeanAnnotation;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.ProdutoService;
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
	
	private final int MIN = 200; 
	
	@Inject
	private ProdutoService brindeSrvc;
	
	private List<Produto> brindes;
	
	private Paginator paginator;
	
	@Inject @Param
	private String pesquisa;
	
	@Inject @Param
	private String min;
	
	@Inject @Param
	private String max;
	
	@Inject
	@CarrinhoBrindeBeanAnnotation
	private CarrinhoBrindeBean carrinhoBrinde;
	
	public PesquisaBrindeBean() 
	{
		paginator = new Paginator(IConstants.QTD_DE_PRODUTOS_POR_PAGINA);
	}

	@PostConstruct
	public void init()
	{
		if (this.naoTemParametro())
		{
			definiValoresDefault();
		}
		
		if (null != pesquisa)
		{
			brindes = this.brindeSrvc.pesquisaDiversa(pesquisa, Finalidade.BRINDE, this.getPaginator());
			return;
		}
		
		if (null != min && null != max)
		{
			brindes = this.brindeSrvc.filtraPeloValor(new BigDecimal(min), new BigDecimal(max), Finalidade.BRINDE, this.getPaginator());
			return;
		}
	}

	/**
	 * Defini os valores para min e max, default.
	 */
	private void definiValoresDefault() 
	{
		min = String.valueOf(MIN);
		max = String.valueOf(carrinhoBrinde.saldoDePontos());
	}
	
	/**
	 * Verifica se não exite parâmetro na inicialização do Bean.
	 * Utilizado para exibir a listagem default de acordo com 
	 * o saldo do cliente. 
	 * 
	 * @return
	 */
	private boolean naoTemParametro() 
	{
		return (null == pesquisa && null == min && null == max);
	}

	public void filtraBrindesPorValor()
	{
		min = Faces.getRequestParameter("min");
		max = Faces.getRequestParameter("max");
		
		if (min.isEmpty() || max.isEmpty())
		{
			this.definiValoresDefault();
		}

		brindes = this.brindeSrvc.filtraPeloValor(new BigDecimal(min), new BigDecimal(max), Finalidade.BRINDE, this.getPaginator());
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
