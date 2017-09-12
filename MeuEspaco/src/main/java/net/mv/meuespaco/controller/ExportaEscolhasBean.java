package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroEscolha;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.model.loja.StatusEscolha;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * View da exportação das escolhas.
 * 
 * @author sidronio
 * @created 10/07/2017
 */
@Named
@ViewScoped
public class ExportaEscolhasBean implements Serializable 
{
	private static final long serialVersionUID = 2592958740212182582L;
	
	private static final String MSG_ERROR = "Não foi possível exportar as escolhas selecionadas.";
	private static final String MSG_SUCCESS = "Foram exportadas %s escolhas.";
	private static final String MSG_FILTRO_NAO_PREENCHIDO = "O filtro deve ser preenchido";
	private static final String MSG_ZERO_REGISTROS_RETORNADOS = "Não foi possível encontrar escolhas para este filtro.";

	@Inject
	private EscolhaService escolhaSrvc;
	
	private FiltroEscolha filtro;
	private List<Escolha> escolhas = new ArrayList<Escolha>();
	
	@PostConstruct
	public void init()
	{
		filtro = new FiltroEscolha();
	}
	
	public void filtra()
	{
		if (!this.filtro.isPreenchido())
		{
			FacesUtil.addErrorMessage(MSG_FILTRO_NAO_PREENCHIDO);
			return;
		}
		
		filtro.setStatus(StatusEscolha.FINALIZADA);
		
		escolhas = escolhaSrvc.filtraPelaPesquisaSemPaginacao(filtro);
		
		if (this.escolhas.isEmpty())
		{
			FacesUtil.addErrorMessage(MSG_ZERO_REGISTROS_RETORNADOS);
		}
	}
	
	public int qtdEscolhasFiltradas()
	{
		return escolhas.size();
	}
	
	public void exporta()
	{
		try 
		{
			int numero = this.escolhaSrvc.exportaEscolhas(this.getEscolhas());
			FacesUtil.addSuccessMessage(String.format(MSG_SUCCESS, numero));
			
		} catch (IOException | IntegracaoException e) 
		{
			FacesUtil.addErrorMessage(MSG_ERROR);
		}
	}
	
	public List<Escolha> getEscolhas() 
	{
		return escolhas;
	}
	
	public FiltroEscolha getFiltro() 
	{
		return filtro;
	}
	public void setFiltro(FiltroEscolha filtro) 
	{
		this.filtro = filtro;
	}
}
