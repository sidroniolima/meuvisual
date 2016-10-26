package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaVenda;
import net.mv.meuespaco.model.loja.StatusVenda;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.VendaService;

/**
 * Bean da pesquisa de vendas de forma paginada para controle: 
 * visualização, exclusão e separação.
 * 
 * @author Sidronio
 * @created 01/09/2016
 */
@Named
@ViewScoped
public class PesquisaVendaBean extends PesquisaSingle implements Serializable {

	private static final long serialVersionUID = 9079743538161016692L;

	@Inject
	private VendaService vendaSrvc;
	
	private List<Venda> vendas;
	
	private FiltroPesquisaVenda filtro;
	
	private Venda vendaSelecionada;
	
	private StatusVenda[] status;
	
	@Override
	@PostConstruct
	public void init() {
		filtro = new FiltroPesquisaVenda();
		this.listarComPaginacao();
		status = StatusVenda.values();
	}

	@Override
	public void excluir() {
		
	}

	@Override
	public void listarComPaginacao() {
		this.vendas = vendaSrvc.filtraPelaPesquisa(filtro, this.getPaginator());
	}

	@Override
	String getMensagemDeExclusaoOk() {
		return "Não é possível excluir uma venda.";
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return "Não é possível excluir uma venda.";
	}

	@Override
	public String descricaoDoRegistro() {
		return vendaSelecionada.codigoFormatado();
	}

	public FiltroPesquisaVenda getFiltro() {
		return filtro;
	}

	public void setFiltro(FiltroPesquisaVenda filtro) {
		this.filtro = filtro;
	}
	public List<Venda> getVendas() {
		return vendas;
	}

	public Venda getVendaSelecionada() {
		return vendaSelecionada;
	}
	public void setVendaSelecionada(Venda vendaSelecionada) {
		this.vendaSelecionada = vendaSelecionada;
	}

	public StatusVenda[] getStatus() {
		return status;
	}

}
