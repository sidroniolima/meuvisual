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
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.service.GrupoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller responsável pela pesquisa e exclusão de um 
 * Grupo de produtos.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Named
@ViewScoped
public class PesquisaGrupoBean extends PesquisaSingle implements Serializable {

	private static final long serialVersionUID = 7526858895139347344L;

	@Inject
	private GrupoService grupoService;
	
	private Grupo grupoSelecionado;
	
	private List<Grupo> grupos;
	
	@Override
	@PostConstruct
	void init() {
		listarComPaginacao();
	}

	@Override
	public void excluir() {
		
		try {
			
			grupoService.exclui(grupoSelecionado.getCodigo());
			
			grupos.remove(grupoSelecionado);
			
			FacesUtil.addSuccessMessage(getMensagemDeExclusaoOk());
			
		} catch (RegraDeNegocioException | DeleteException e) {
			
			FacesUtil.addErrorMessage(getMensagemDeErroDeExclusao(e.getMessage()));
		}
	}

	@Override
	public void listarComPaginacao() {
		
		grupos = grupoService.listarComPaginacao(
				getPaginator(), 
				Arrays.asList("familia.descricao", "descricao"), 
				Arrays.asList("familia"), 
				Arrays.asList("familia"));
	}
	
	@Override
	public String descricaoDoRegistro() {
		return grupoSelecionado.toString();
	}

	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Grupo %s excluído com sucesso.", descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("Grupo %s não pode ser excluído. %s", descricaoDoRegistro(), msgError);
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}
	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

}
