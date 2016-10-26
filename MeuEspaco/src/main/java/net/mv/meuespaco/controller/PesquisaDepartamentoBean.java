package net.mv.meuespaco.controller;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.service.DepartamentoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Classe da camada View responsável pela exibição dos 
 * resultados da pesquisa de departamentos.
 * 
 * @author Sidronio
 * 02/03/2016
 */
@Named
@ViewScoped
public class PesquisaDepartamentoBean extends PesquisaSingle {

	@Inject
	private DepartamentoService depSrvc;
	
	private List<Departamento> departamentos;
	private Departamento depSelecionado;
	
	@Override
	public void init() {
		
		if (null == departamentos) {
			listarComPaginacao();
		}
		
	}

	@Override
	public void excluir() {
		
		try {
			
			depSrvc.exclui(depSelecionado.getCodigo());
			departamentos.remove(depSelecionado);
			
			FacesUtil.addSuccessMessage(this.getMensagemDeExclusaoOk());
			
		} catch (RegraDeNegocioException | DeleteException e) 
		{
			FacesUtil.addErrorMessage(this.getMensagemDeErroDeExclusao(e.getMessage()));
		}
		
	}

	@Override
	public void listarComPaginacao() {
		
		departamentos = depSrvc.listarComPaginacao(
				this.getPaginator(), 
				Arrays.asList("descricao"), 
				null, 
				null);
	}

	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Departamento %s excluído.", this.descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("O departamento %s não pode ser excluído. %s", this.descricaoDoRegistro(), msgError);
	}

	@Override
	public String descricaoDoRegistro() {
		return depSelecionado.getDescricao();
	}

	public Departamento getDepSelecionado() {
		return depSelecionado;
	}
	public void setDepSelecionado(Departamento depSelecionado) {
		this.depSelecionado = depSelecionado;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

}
