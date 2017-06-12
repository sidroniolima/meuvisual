package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.CarrinhoBrindeBeanAnnotation;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.CarrinhoBrinde;
import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.service.PontuacaoService;
import net.mv.meuespaco.service.ResgateBrindeService;
import net.mv.meuespaco.util.Encryptor;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Carrinho para brindes.
 * 
 * @author sidronio
 * @created 02/06/2017 
 */
@Named
@SessionScoped
@CarrinhoBrindeBeanAnnotation
public class CarrinhoBrindeBean extends CarrinhoAbstractBean implements Serializable
{
	private static final long serialVersionUID = -6309202919424287536L;
	
	private final String urlIndexResgateCriado = "/private/brinde/index.xhtml?resgate-criado=%s&faces-redirect=true";
	
	@Inject
	private PontuacaoService pontosSrvc;
	
	@Inject
	private ResgateBrindeService resgateSrvc;
	
	private CarrinhoBrinde carrinho;
	
	@Override
	public void criaCarrinho() throws RegraDeNegocioException 
	{
		this.carrinho = new CarrinhoBrinde(pontosSrvc.pontosAcumuladosDoClienteLogado());
	}

	@Override
	public String finalizaCarrinho() 
	{
		ResgateBrinde resgateCriado = 
				resgateSrvc.criaResgateDeCarrinho(
						this.carrinho.getItens(), 
						this.getClienteLogado(), 
						this.pontosSrvc.pontosAcumuladosDoClienteLogado());
		
		ResgateBrinde resgateSalvo;
		
		try 
		{
			resgateSalvo = this.resgateSrvc.salva(resgateCriado);
			carrinho.atualizaSaldo();
			this.esvazia();
			
			String urlFormatada = String.format(this.urlIndexResgateCriado, Encryptor.encrypt(resgateSalvo.getCodigo().toString())); 
			
			FacesContext.getCurrentInstance().getExternalContext().redirect(urlFormatada);
			
		} catch (RegraDeNegocioException | IOException e) 
		{
			FacesUtil.addErrorMessage("Não foi possível resgatar os itens. " + e.getMessage());
		}
		
		return "";
	}

	@Override
	public Carrinho getCarrinho() 
	{
		return this.carrinho;
	}
	
	/**
	 * Retorna o saldo de pontos de acordo com a pontuação acumulada 
	 * descontando os itens do carrinho.
	 * @return
	 */
	public long saldoDePontos() 
	{
		BigDecimal saldo = new BigDecimal(pontosSrvc.pontosAcumuladosDoClienteLogado())
				.subtract(this.getCarrinho().valorDosItens());
		
		return saldo.longValue();
	}
}
