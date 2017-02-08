package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.service.EquipeService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@RequestScoped
public class AtualizacaoEquipesBean implements Serializable {

	private static final long serialVersionUID = -4652659416270954667L;
	
	@Inject
	private EquipeService equipeSrvc;

	/**
	 * Atualiza equipes do ERP.
	 */
	public void atualizaEquipes()
	{
		try 
		{
			this.equipeSrvc.atualizaEquipesDoERP();
			
			FacesUtil.addSuccessMessage("Equipes atualizadas com sucesso.");
			
		} catch (IOException e) 
		{
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}
	
}
