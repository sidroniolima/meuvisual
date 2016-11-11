package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.CarrinhoVendaBeanAnnotation;
import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.CarrinhoVenda;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cupom;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.CupomService;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.ReservaProdutoService;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Implementação do Bean para o carrinho de venda. 
 * 
 * @author Sidronio
 * @created 17/08/16
 */
@Named
@SessionScoped
@CarrinhoVendaBeanAnnotation
public class CarrinhoVendaBean extends CarrinhoAbstractBean implements Serializable {

	private static final long serialVersionUID = -1167542083924471028L;

	private CarrinhoVenda carrinho;
	
	@Inject
	private VendaService vendaSrvc;
	
	@Inject
	@ClienteLogado
	private Cliente cliente;
	
	@Inject
	private CupomService cupomSrvc;
	
	@Inject
	private PrePagamentoBean prePagamento;
	
	private String codigoCupom;
	
	private Cupom cupom;
	
	public CarrinhoVendaBean() {	}
	
	public CarrinhoVendaBean(ReservaProdutoService reservaProdutoSrvc, EstoqueService estoqueSrvc) {
		super(reservaProdutoSrvc, estoqueSrvc);
	}
	
	@Override
	public void criaCarrinho() throws RegraDeNegocioException {
		carrinho = new CarrinhoVenda();
	}

	public void buscaCupom()
	{
		try {
			carrinho.setDesconto(cupomSrvc.descontoDoCupom(codigoCupom));
		} catch (RegraDeNegocioException e) 
		{
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}
	
	@Override
	public String finalizaCarrinho() 
	{
		try 
		{
			Venda venda = vendaSrvc.criaVendaPeloCarrinho(carrinho, cliente, cupom);
			
			prePagamento.registrarVenda(venda);
			this.esvazia();
			return "cielo-api";
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		
		return null;
	}

	@Override
	public Carrinho getCarrinho() {
		return carrinho;
	}

	public String getCodigoCupom() {
		return codigoCupom;
	}
	public void setCodigoCupom(String codigoCupom) {
		this.codigoCupom = codigoCupom;
	}
}
