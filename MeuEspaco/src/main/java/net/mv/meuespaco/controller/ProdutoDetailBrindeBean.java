package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.CarrinhoBrindeBeanAnnotation;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.PontuacaoService;
import net.mv.meuespaco.service.ProdutoService;

/**
 * Visualição dos detalhes do brinde com acesso às características e
 * grades do produto. Com função de adição à cesta de brindes. 
 * 
 * @author sidronio
 * @created 02/06/2017
 */
@Named
@ViewScoped
public class ProdutoDetailBrindeBean extends ProdutoDetailAbstratcBean implements Serializable
{
	private static final long serialVersionUID = -5887570111834631021L;
	
	@Inject
	private PontuacaoService pontuacaoSrvc;
	
	@Inject
	@CarrinhoBrindeBeanAnnotation
	private CarrinhoAbstractBean carrinhoBean;
	
	public ProdutoDetailBrindeBean() {	}
	
	public ProdutoDetailBrindeBean(ProdutoService produtoService, ClienteService clienteSrvc, CarrinhoAbstractBean carrinhoBean, Long paramCodigo) 
	{
		super(produtoService, clienteSrvc, paramCodigo);
		this.carrinhoBean = carrinhoBean;
	}

	@Override
	void verificaDisponibilidadeDeEscolha() 
	{
		this.habilitaEscolha(true);
	}

	@Override
	public CarrinhoAbstractBean getCarrinhoBean() 
	{
		return this.carrinhoBean;
	}

	@Override
	public String getUrlCarrinho() 
	{
		return "carrinho-brinde";
	}

}
