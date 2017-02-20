package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class VisualizaVendaBean implements Serializable {

	private static final long serialVersionUID = -833121079065448383L;
	
	@Inject
	private VendaService vendaSrvc;

	@Inject @Param
	private Long paramCodigo;
	
	private Venda venda;

	private Pagamento pagamento;
	
	@PostConstruct
	public void init()
	{
		if (null == paramCodigo)
		{
			FacesUtil.addErrorMessage("Não foi possível localizar a Venda por esse código.");
			return;
		}
		
		venda = vendaSrvc.buscaCompletaPeloCodigo(paramCodigo);
		venda.setItens(vendaSrvc.buscaItensCompleto(paramCodigo));
		
		if (venda.isPaga())
		{
			try 
			{
				pagamento = this.vendaSrvc.consultaPagamento(venda);
			
			} catch (CieloException | IntegracaoException e) 
			{
				FacesUtil.addErrorMessage("Não foi possível recuperar as informações do pagamento. Tente novamente mais tarde.");
			}
		}
	}

	public Venda getVenda() {
		return venda;
	}
	
	public Pagamento getPagamento() {
		return pagamento;
	}
}
