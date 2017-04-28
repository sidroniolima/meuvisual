package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroDePesquisa;
import net.mv.meuespaco.controller.filtro.FiltroPontuacao;
import net.mv.meuespaco.model.integracao.Pontuacao;
import net.mv.meuespaco.service.PontuacaoService;

/**
 * Listagem da pontuação com filtro utilizado no Administrativo.
 * 
 * @author sidronio
 * @created 27/04/2017
 */
@Named
@ViewScoped
public class ListaPontuacaoBean extends ListaSimples implements Serializable
{
	private static final long serialVersionUID = 8961018690604372488L;

	@Inject
	private PontuacaoService pontoSrvc;
	
	private List<Pontuacao> pontuacao;
	
	private FiltroPontuacao filtro;
	
	@Override
	@PostConstruct
	public void init() 
	{
		this.filtro = new FiltroPontuacao();
	}

	@Override
	public void listarComPaginacao() 
	{
		pontuacao = this.pontoSrvc.filtraPeloModoEspecifico((FiltroPontuacao) this.getFiltro(), this.getPaginator());
		
	}

	public List<Pontuacao> getPontuacao() 
	{
		return pontuacao;
	}
	
	@Override
	public FiltroDePesquisa getFiltro() 
	{
		return this.filtro;
	}
}
