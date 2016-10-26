package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.CarrinhoVendaBeanAnnotation;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.ProdutoService;

/**
 * Implementação da visualização dos detalhes do produto 
 * para venda.
 * 
 * @author Sidronio
 * @created 17/08/16
 */
@Named
@ViewScoped
public class ProdutoDetailVendaBean extends ProdutoDetailAbstratcBean implements Serializable {

	private static final long serialVersionUID = -7663834166702901740L;
	
	@Inject
	@CarrinhoVendaBeanAnnotation
	private CarrinhoAbstractBean carrinhoBean;
	
	public ProdutoDetailVendaBean() {	}
	
	public ProdutoDetailVendaBean(ProdutoService produtoService, ClienteService clienteSrvc, Long paramCodigo) {
		super(produtoService, clienteSrvc, paramCodigo);
	}
	
	@Override
	public CarrinhoAbstractBean getCarrinhoBean() {
		return carrinhoBean;
	}

	@Override
	public String getUrlCarrinho() {
		return "carrinho-venda";
	}

	@Override
	void verificaDisponibilidadeDeEscolha() {
		this.habilitaEscolha(true);
	}

}
