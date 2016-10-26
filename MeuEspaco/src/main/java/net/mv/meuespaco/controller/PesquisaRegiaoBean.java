package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller responsável pela pesquisa e exclusão de uma Região.
 * 
 * @author Sidronio
 * 12/11/2015
 */
@Named
@ViewScoped
public class PesquisaRegiaoBean extends PesquisaSingle implements Serializable{

	private static final long serialVersionUID = 928034335739822162L;

	@Inject
	private RegiaoService regiaoService;
	
	private Regiao regiaoSelecionada;
	private List<Regiao> regioes;
	
	@PostConstruct
	@Override
	void init() {
		
		this.listarComPaginacao();
	}

	@Override
	public void excluir() {
		
		try {
			
			regiaoService.exclui(regiaoSelecionada.getCodigo());
			
			regioes.remove(regiaoSelecionada);
			
			FacesUtil.addSuccessMessage(getMensagemDeExclusaoOk());
			
		} catch (RegraDeNegocioException | DeleteException e) {
			
			FacesUtil.addErrorMessage(
					getMensagemDeErroDeExclusao(e.getMessage()));
		}
		
	}

	@Override
	public void listarComPaginacao() {
		regioes = regiaoService.listarComPaginacao(
				getPaginator(),
				Arrays.asList("descricao"),
				Arrays.asList("semana"),
				null);
	}
	
	@Override
	public String descricaoDoRegistro() {
		return regiaoSelecionada.getDescricao();
	}

	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Região %s excluída com sucesso.", descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("Região %s não pôde ser excluída. %s", descricaoDoRegistro(), msgError);
	}

	public Regiao getRegiaoSelecionada() {
		return regiaoSelecionada;
	}
	public void setRegiaoSelecionada(Regiao regiaoSelecionada) {
		this.regiaoSelecionada = regiaoSelecionada;
	}

	public List<Regiao> getRegioes() {
		return regioes;
	}
	
}
