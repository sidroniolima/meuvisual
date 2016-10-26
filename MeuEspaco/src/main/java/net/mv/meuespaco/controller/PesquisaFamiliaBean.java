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
import net.mv.meuespaco.model.Familia;
import net.mv.meuespaco.service.FamiliaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller responsável pela pesquisa e exclusão de 
 * uma Familia de produtos.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Named
@ViewScoped
public class PesquisaFamiliaBean extends PesquisaSingle implements Serializable{

	private static final long serialVersionUID = 6922118092986950559L;

	@Inject
	private FamiliaService familiaService;
	
	private Familia familiaSelecionada;
	
	private List<Familia> familias;
	
	@Override
	@PostConstruct
	void init() {
		listarComPaginacao();
	}

	@Override
	public void excluir() {
		
		try {
			
			familiaService.exclui(familiaSelecionada.getCodigo());
			
			familias.remove(familiaSelecionada);
			
			FacesUtil.addSuccessMessage(getMensagemDeExclusaoOk());
			
		} catch (RegraDeNegocioException | DeleteException e) {
			
			FacesUtil.addErrorMessage(this.getMensagemDeErroDeExclusao(e.getMessage()));
		
		}
	}

	@Override
	public void listarComPaginacao() {
		
		familias = familiaService.listarComPaginacao(
				getPaginator(),
				Arrays.asList("descricao"), 
				null, 
				null);
	}
	
	@Override
	public String descricaoDoRegistro() {
		return familiaSelecionada.getDescricao();
	}

	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Família %s excluída com sucesso.", descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("Não foi possível excluir a família %s. %s", descricaoDoRegistro(), msgError);
	}

	public List<Familia> getFamilias() {
		return familias;
	}

	public Familia getFamiliaSelecionada() {
		return familiaSelecionada;
	}
	public void setFamiliaSelecionada(Familia familiaSelecionada) {
		this.familiaSelecionada = familiaSelecionada;
	}

}
