package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.model.integracao.Equipe;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.EquipeService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Bean para listagem da equipe do cliente logado.
 * 
 * @author sidronio
 * @created 07/02/2017
 */
@Named
@RequestScoped
public class ListaEquipeBean implements Serializable 
{
	private static final long serialVersionUID = -6692184625965046155L;

	@Inject
	private EquipeService equipeSrvc;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	private List<Equipe> equipe;
	
	@PostConstruct
	public void init()
	{
		if (null == clienteLogado)
		{
			FacesUtil.addErrorMessage("Não foi possível listar a equipe do cliente logado.");
		}
		
		equipe = this.equipeSrvc.listaEquipesDoCliente(clienteLogado);
	}

	public List<Equipe> getEquipe() {
		return equipe;
	}
}
