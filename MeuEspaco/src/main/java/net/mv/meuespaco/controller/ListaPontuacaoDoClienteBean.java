package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroDePesquisa;
import net.mv.meuespaco.model.integracao.Pontuacao;
import net.mv.meuespaco.service.PontuacaoService;

/**
 * Listagem dos pontos do cliente logado.
 * 
 * @author sidronio
 * @created 25/04/2017
 */
@Named(value="pontuacaoDoClienteBean")
@ViewScoped
public class ListaPontuacaoDoClienteBean extends ListaSimples implements Serializable 
{
	private static final long serialVersionUID = 1918941281476114605L;

	@Inject
	private PontuacaoService pontoSrvc;

	private List<Pontuacao> pontuacao;
	 
	@Override
	@PostConstruct
	public void init() 
	{
		this.listarComPaginacao();
	}

	@Override
	public void listarComPaginacao() 
	{
		this.pontuacao = this.pontoSrvc.pontuacaoDoClienteLogado(this.getPaginator());
	}
	
	/**
	 * Retorna os pontos acumulados do cliente.
	 * 
	 * @return soma dos pontos.
	 */
	public long pontosAcumuladosDoCliente()
	{
		return this.pontoSrvc.pontosAcumuladosDoClienteLogado();
	}

	public List<Pontuacao> getPontuacao() {
		return pontuacao;
	}
	
	@Override
	public FiltroDePesquisa getFiltro() 
	{
		return null;
	}
}
