package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.CarrinhoConsignadoBeanAnnotation;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.FacesUtil;


/**
 * Implementação do Bean para visualização de produtos 
 * para Consignação.
 * 
 * @author Sidronio
 * @updated 17/08/16
 */
@Named
@ViewScoped
public class ProdutoDetailConsignadoBean extends ProdutoDetailAbstratcBean implements Serializable {

	private static final long serialVersionUID = -7618681132928786610L;

	@Inject
	@CarrinhoConsignadoBeanAnnotation
	private CarrinhoAbstractBean carrinhoBean;
	
	public ProdutoDetailConsignadoBean() {	}
	
	public ProdutoDetailConsignadoBean(ProdutoService produtoService, ClienteService clienteSrvc, CarrinhoAbstractBean carrinhoBean, Long paramCodigo) {
		super(produtoService, clienteSrvc, paramCodigo);
		this.carrinhoBean = carrinhoBean;
	}

	@Override
	public CarrinhoAbstractBean getCarrinhoBean() {
		return carrinhoBean;
	}
	
	@Override
	public String getUrlCarrinho() {
		return "carrinho";
	}

	@Override
	void verificaDisponibilidadeDeEscolha() 
	{
		try 
		{
			this.getClienteSrvc().verificaSeOUsuarioLogadoPodeEscolher();
			this.habilitaEscolha(true);
			
		} catch (RegraDeNegocioException e) 
		{
			this.habilitaEscolha(false);
			FacesUtil.addErrorMessage(String.format("Não será possível a escolha. %s", e.getMessage()));
		}		
	}
	
}
