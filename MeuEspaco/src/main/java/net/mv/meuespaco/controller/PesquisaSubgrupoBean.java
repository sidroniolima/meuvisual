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
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.service.SubgrupoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller responsável pela pesquisa e exclusão de um 
 * subrupo de produtos.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Named
@ViewScoped
public class PesquisaSubgrupoBean extends PesquisaSingle implements Serializable {

	private static final long serialVersionUID = 8459019269891449257L;

	@Inject
	private SubgrupoService sugrupoService;
	
	private List<Subgrupo> subgrupos;
	
	private Subgrupo subgrupoSelecionado;
	
	@Override
	@PostConstruct
	void init() {
		listarComPaginacao();
	}

	@Override
	public void excluir() {
		
		try {
			
			sugrupoService.exclui(subgrupoSelecionado.getCodigo());
			
			subgrupos.remove(subgrupoSelecionado);
			
			FacesUtil.addSuccessMessage(getMensagemDeExclusaoOk());
			
		} catch (RegraDeNegocioException | DeleteException e) {
			
			FacesUtil.addErrorMessage(getMensagemDeErroDeExclusao(e.getMessage()));
		}
	}

	@Override
	public void listarComPaginacao() {
		
		subgrupos = sugrupoService.listarComPaginacao(
				getPaginator(), 
				Arrays.asList("familia.descricao", "grupo.descricao", "descricao"), 
				Arrays.asList("grupo", "grupo.familia"), 
				Arrays.asList("grupo","grupo.familia"));
	}
	
	@Override
	public String descricaoDoRegistro() {
		return subgrupoSelecionado.toString();
	}

	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Subgrupo %s excluído com sucesso.", descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("Subgrupo %s não pode ser excluído. %s", descricaoDoRegistro(), msgError);
	}

	public List<Subgrupo> getSubgrupos() {
		return subgrupos;
	}

	public Subgrupo getSubgrupoSelecionado() {
		return subgrupoSelecionado;
	}
	public void setSubgrupoSelecionado(Subgrupo subgrupoSelecionado) {
		this.subgrupoSelecionado = subgrupoSelecionado;
	}
}
