package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.service.SemanaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Implementação do Bean de Controller para a entidade Regiao. 
 * 
 * @author Sidronio
 * 05/11/2015
 */
@Named
@ViewScoped
public class CadastroRegiaoBean extends CadastroSingle implements Serializable {

	private static final long serialVersionUID = -7009502581589864017L;

	@Inject
	private RegiaoService regiaoService;
	
	@Inject
	private SemanaService semanaService;
	
	private Regiao regiao;
	
	private List<Semana> semanas;
	
	@Override
	public void novoRegistro() {
		regiao = new Regiao();
	}

	@Override
	@PostConstruct
	public void init() {
		
		if (isEdicao()) {
			regiao = regiaoService.buscarPeloCodigoComSemana(getParamCodigo());
		} else {
			novoRegistro();
		}
		
		semanas = semanaService.buscaTodas();
	}

	@Override
	public void salvar() {
		try {
			regiaoService.salva(regiao);
			
			if (isEdicao()) {
				
				FacesUtil.addSuccessMessage(this.getMensagemDeAlteracao(regiao.getDescricao()));
			} else {
				
				FacesUtil.addSuccessMessage(this.getMensagemDeInclusao(regiao.getDescricao()));
			}
			
			novoRegistro();
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage("Não foi possível inclui o registro. " + e.getMessage());
		}
	}

	@Override
	public String getMensagemDeInclusao(String registro) {
		return String.format("Região %s incluída com sucesso.", registro);
	}

	@Override
	public String getMensagemDeAlteracao(String registro) {
		return String.format("Região %s alterada com sucesso.", registro);
	}

	public Regiao getRegiao() {
		return regiao;
	}
	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public List<Semana> getSemanas() {
		return semanas;
	}
}
