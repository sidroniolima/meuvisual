package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.CarrinhoVendaBeanAnnotation;
import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.cielo.Brand;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.FacesUtil;
import net.mv.meuespaco.util.ValorEmCentavos;

/**
 * Camada controller para o pagamento da venda com preenchimento 
 * do cartão de crédito.
 * 
 * @author sidronio
 * @created 19/10/2016
 */
@Named
@ViewScoped
public class PagamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VendaService vendaSrvc;
	
	@Inject	
	@ClienteLogado
	private Cliente clienteLogado;
	
	@Inject
	@CarrinhoVendaBeanAnnotation
	private CarrinhoAbstractBean carrinhoBean;
	
	@Inject
	@ClienteLogado
	private Cliente cliente;
	
	private Venda venda;
	private Pagamento pagamento;
	
	private Brand[] brands;
	
	public PagamentoBean() {	}
	
	public PagamentoBean(VendaService vendaSrvc, Cliente clienteLogado, CarrinhoAbstractBean carrinhoBean) 
	{
		this.vendaSrvc = vendaSrvc;
		this.clienteLogado = clienteLogado;
		this.carrinhoBean = carrinhoBean;
	}

	@PostConstruct
	public void init() throws IOException
	{
		if (isPostback())
	    {
			this.redirecionaParaIndex();
			return;
	    }
		
		brands = Brand.values();
			
		try 
		{
			this.venda = vendaSrvc.criaVendaPeloCarrinho(carrinhoBean.getCarrinho(), cliente);
			this.criaPagamento();
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage("Não foi possível localizar sua compra. " + e.getMessage());
		}
		
	}
	   
	/**
	 * Redericiona para o Index. Utilizado no caso de PostBack
	 * @throws IOException 
	 */
	private void redirecionaParaIndex() throws IOException 
	{
		FacesContext.getCurrentInstance().getExternalContext().redirect("/private/venda/index.xhtml");
	}

	/**
	 * Verifica se é Postback.
	 * 
	 * @return
	 */
	private boolean isPostback()
	{
		if (null == FacesContext.getCurrentInstance())
		{
			return false;
		}
		
		return FacesContext.getCurrentInstance().isPostback();
	}

	/**
	 * Cria o pagamento dada a venda, o nome do cliente e o valor.
	 */
	private void criaPagamento() 
	{
		pagamento = new Pagamento(
				this.venda.getUniqueId().toString(), 
				clienteLogado.getNome(), 
				ValorEmCentavos.toCentavos(venda.valorComDesconto()).intValue());
	}
	
	/**
	 * Finaliza o pagamento enviando os dados para API 
	 * da Cielo.
	 * 
	 * @return url de compras caso seja bem sucedido.
	 */
	public String finaliza()
	{
		try 
		{
			this.vendaSrvc.registraPagamento(this.venda, this.pagamento);
			carrinhoBean.esvazia();
			
			return "pagamento-sucesso";
			
		} catch (CieloException | IntegracaoException | RegraDeNegocioException e) 
		{
			FacesUtil.addErrorMessage("Sua compra não foi aprovada. Tente novamente");
		}
		
		return "";
	}

	public Pagamento getPagamento() {
		return pagamento;
	}
	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Venda getVenda() {
		return venda;
	}

	public Brand[] getBrands() {
		return brands;
	}
	
}
