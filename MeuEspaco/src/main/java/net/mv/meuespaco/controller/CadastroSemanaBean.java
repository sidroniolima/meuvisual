package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.service.SemanaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller do cadastro da entidade Semana responsável 
 * pela inclusão e alteração.
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Named
@ViewScoped
public class CadastroSemanaBean extends CadastroSingle implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private SemanaService semanaService;
	
	private Semana semana;
	
	@Override
	@PostConstruct
	public void init() {
		
		if (isEdicao()) {
			semana = semanaService.buscaPeloCodigo(getParamCodigo());
		
		} else {
			novoRegistro();
		}
		
	}	
	
	@Override
	void novoRegistro() {
		semana = new Semana();
	}

	@Override
	public void salvar() {
		
		try {
			
			semanaService.salva(semana);
			
			if (isEdicao()) {
				FacesUtil.addSuccessMessage(this.getMensagemDeAlteracao(semana.getDescricao()));
			} else {
				FacesUtil.addSuccessMessage(this.getMensagemDeInclusao(semana.getDescricao()));
			}
			
			novoRegistro();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		
	}

	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Semana %s incluída com sucesso.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("Semana %s alterada com sucesso.", registro);
	}

	public Semana getSemana() {
		return semana;
	}
	public void setSemana(Semana semana) {
		this.semana = semana;
	}

}
