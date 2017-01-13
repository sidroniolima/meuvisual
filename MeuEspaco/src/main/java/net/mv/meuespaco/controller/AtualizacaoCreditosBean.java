package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.service.CreditoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller para a atualização de Creditos.
 * 
 * @author sidronio
 * @created 09/01/2017
 */
@Named("atualizacaoCreditosBean")
@ViewScoped
public class AtualizacaoCreditosBean implements Serializable
{
	private static final long serialVersionUID = -6781748735493185334L;
	
	@Inject
	private CreditoService creditoSrvc;
	
	/**
	 * Atualiza os Creditos vindos do ERP.
	 */
	public void atualiza()
	{
		try 
		{
			creditoSrvc.atualizaCreditosDoERP();
		
			FacesUtil.addSuccessMessage("Créditos dos clientes importados do ERP.");
		
		} catch (IOException e) 
		{
			FacesUtil.addErrorMessage("Não foi possível atualizar os créditos. Entre contanto com o administrador." + e.getMessage());
		}
	}

}
