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
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.service.SemanaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller de pesquisa da entidade Semana responsável 
 * pela listagem dos registros e exclusão.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Named
@ViewScoped
public class PesquisaSemanaBean extends PesquisaSingle implements Serializable{

	private static final long serialVersionUID = 2869677083786999251L;

	@Inject
	private SemanaService semanaService;
	
	private Semana semanaSelecionada;
	
	private List<Semana> semanas;
	
	@Override
	@PostConstruct
	void init() {
		listarComPaginacao();
	}

	@Override
	public void excluir() {
		
		try {
			
			semanaService.exclui(semanaSelecionada.getCodigo());
			semanas.remove(semanaSelecionada);
			
			FacesUtil.addSuccessMessage(getMensagemDeExclusaoOk());
			
		} catch (RegraDeNegocioException | DeleteException e) {
			
			FacesUtil.addErrorMessage(this.getMensagemDeErroDeExclusao(e.getMessage()));
		}
	}

	@Override
	public void listarComPaginacao() {
		
		semanas = semanaService.listarComPaginacao(
				getPaginator(), 
				Arrays.asList("dataInicial","descricao"), 
				null, 
				null);
	}
	
	@Override
	public String descricaoDoRegistro() {
		return semanaSelecionada.getDescricao();
	}


	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Semana %s excluída com sucesso.", descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("Não foi possível excluir a semana %s. %s", descricaoDoRegistro(), msgError); 
	}

	public Semana getSemanaSelecionada() {
		return semanaSelecionada;
	}
	public void setSemanaSelecionada(Semana semanaSelecionada) {
		this.semanaSelecionada = semanaSelecionada;
	}

	public List<Semana> getSemanas() {
		return semanas;
	}

}
