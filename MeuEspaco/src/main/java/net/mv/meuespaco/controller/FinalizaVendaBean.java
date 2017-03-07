package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller para atendimento dos itens de uma Venda e 
 * finalização da mesma.
 * 
 * @author sidronio
 * @created 07/03/2017
 */
@Named
@ViewScoped
public class FinalizaVendaBean implements Serializable {

	private static final long serialVersionUID = -4610486466305621352L;

	@Inject
	private VendaService vendaSrvc;
	
	@Inject @Param
	private Long paramCodigo;
	
	private Venda venda;
	
	@PostConstruct
	public void init()
	{
		if (null != paramCodigo)
		{
			venda = vendaSrvc.buscaCompletaPeloCodigo(paramCodigo);
		}
		
		if (null == venda)
		{
			FacesUtil.addErrorMessage("Não foi possível localizar a Venda. Retorne à pesquisa e selecione um item");
		}
	}
	
	/**
	 * Finaliza a venda e a salva.
	 */
	public String finaliza()
	{
		this.venda.finaliza();
		
		try 
		{
			this.vendaSrvc.salva(venda);
					
			return "pesquisa-venda";
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage("Não foi possível finalizar a venda. " + e.getMessage());
			
			return "";
		}
	}
	
	public Venda getVenda() {
		return venda;
	}
	public void setVenda(Venda venda) {
		this.venda = venda;
	}
	
}
