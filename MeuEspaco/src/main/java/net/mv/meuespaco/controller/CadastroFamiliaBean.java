package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Familia;
import net.mv.meuespaco.service.FamiliaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Implementação do Bean de Controller para a entidade Familia 
 * responsável pelo cadastro edição. 
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Named
@ViewScoped
public class CadastroFamiliaBean extends CadastroSingle implements Serializable{

	private static final long serialVersionUID = 7500416188112553847L;

	@Inject
	private FamiliaService familiaService;
	
	private Familia familia;
	
	@Override
	@PostConstruct
	public void init() {
		
		if (isEdicao()) {
			familia = familiaService.buscaPeloCodigo(getParamCodigo());
		} else {
			novoRegistro();
		}
	}

	@Override
	void novoRegistro() {
		familia = new Familia();
	}

	@Override
	public void salvar() {
		
		try {
			
			familiaService.salva(familia);
			
			if (isEdicao()) {
				
				FacesUtil.addSuccessMessage(this.getMensagemDeAlteracao(familia.getDescricao()));
			} else {
				FacesUtil.addSuccessMessage(this.getMensagemDeInclusao(familia.getDescricao()));
			}
			
			novoRegistro();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Família %s incluída com sucesso.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("Família %s alterada com sucesso.", registro);
	}

	public Familia getFamilia() {
		return familia;
	}
	public void setFamilia(Familia familia) {
		this.familia = familia;
	}

}
