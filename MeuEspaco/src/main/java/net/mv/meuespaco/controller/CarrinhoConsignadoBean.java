package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.CarrinhoConsignadoBeanAnnotation;
import net.mv.meuespaco.annotations.UsuarioLogado;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.CarrinhoConsignado;
import net.mv.meuespaco.service.CarrinhoService;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.ReservaProdutoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * View do carrinho com os produtos selecionados.
 * 
 * @author Sidronio
 * 08/12/2015
 */
@Named
@SessionScoped
@CarrinhoConsignadoBeanAnnotation
public class CarrinhoConsignadoBean extends CarrinhoAbstractBean implements Serializable {

	private static final long serialVersionUID = -4319724610531872528L;

	@Inject
	private CarrinhoService carrinhoSrvc;
	
	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;
	
	@Inject
	private ClienteService clienteSrvc;
	
	private CarrinhoConsignado carrinho;
	
	public CarrinhoConsignadoBean() {	}
	
	public CarrinhoConsignadoBean(ReservaProdutoService reservaSrvc, EstoqueService estoqueSrvc, 
			CarrinhoService carrinhoSrvc, ClienteService clienteSrvc) 
	{
		super(reservaSrvc, estoqueSrvc);
		this.carrinhoSrvc = carrinhoSrvc;
		this.clienteSrvc = clienteSrvc;
	}

	/**
	 * Cria um carrinho com o valor e qtd disponíveis. É criado 
	 * no momento do login.
	 * 
	 * @param valor disponível para adição de itens.
	 * @param qtd disponível para adição de itens.
	 * @throws RegraDeNegocioException 
	 */
	@Override
	public void criaCarrinho() throws RegraDeNegocioException {
		carrinho = new CarrinhoConsignado(clienteSrvc.valorDisponivelParaEscolha(), 
		clienteSrvc.qtdDisponivelParaEscolha());
	}
	
	/**
	 * Finaliza o carrinho, gerando o pedido e 
	 * redirecinando para os pedidos do cliente. 
	 * 
	 * @return
	 */
	public String finalizaCarrinho() {
		
		try {
			carrinhoSrvc.finalizaEscolha(carrinho, usuarioLogado);
			carrinho.atualizaSaldos();
			this.esvazia();
			
			FacesUtil.addSuccessMessage("Escolha criada com sucesso.");
			
			return "minhas-escolhas";
			
		} catch (RegraDeNegocioException e) {

			FacesUtil.addErrorMessage(
					String.format("Não foi possível criar a escolha. %s", e.getMessage()));
			
			return "";
		}
	}
	
	/**
	 * Retorna o carrinho.
	 * 
	 * @return
	 */
	@Override
	public Carrinho getCarrinho(){
		return carrinho;
	}
	
}
