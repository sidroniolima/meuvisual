package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.cielo.Brand;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.CreditPayment;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.IntegracaoCieloService;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.FacesUtil;

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
	private IntegracaoCieloService cielo;
	
	@Inject
	private VendaService vendaSrvc;
	
	@Inject	
	@ClienteLogado
	private Cliente clienteLogado;
	
	@Inject
	private PrePagamentoBean prePagamento;
	
	private Venda venda;
	private Pagamento pagamento;
	
	private Brand[] brands;
	
	@PostConstruct
	public void init()
	{
		brands = Brand.values();
		
		venda = prePagamento.vendaParaPagamento();

		pagamento = new Pagamento(
				venda.codigoFormatado(), 
				clienteLogado.getNome());
		
		pagamento.setPayment(new CreditPayment(venda.valorComDesconto().floatValue()));
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
			this.pagamento.getPayment().getCard().setCardNumber("0000000000000001");
			//TODO: retirar!!!
			cielo.efetuaPagamentoCredito(this.pagamento);
			
			prePagamento.removerVenda();
			
			return "minhas-compras";
			
		} catch (CieloException | IntegracaoException e) {
			
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
