package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Familia;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.service.FamiliaService;
import net.mv.meuespaco.service.GrupoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Implementação do Bean de Controller para a entidade Grupo 
 * responsável pelo cadastro edição. 
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Named
@ViewScoped
public class CadastroGrupoBean extends CadastroSingle implements Serializable{

	private static final long serialVersionUID = -9194706205254220909L;

	@Inject
	private GrupoService grupoService;
	
	@Inject
	private FamiliaService familiaService;
	
	private Grupo grupo;
	private List<Familia> familias;
	
	@PostConstruct
	@Override
	public void init() {
		
		if (isEdicao()) {
		
			grupo = grupoService.buscaGrupoComFamilia(getParamCodigo());
		
		} else {
			novoRegistro();
		}
		
		familias = familiaService.buscaTodas();
	}

	@Override
	void novoRegistro() {
		grupo = new Grupo();
	}

	@Override
	public void salvar() {
		
		try {
			
			grupoService.salva(grupo);
			
			if (isEdicao()) {
			
				FacesUtil.addSuccessMessage(this.getMensagemDeAlteracao(grupo.getDescricao()));
			
			} else {
			
				FacesUtil.addSuccessMessage(this.getMensagemDeInclusao(grupo.getDescricao()));
			}
			
			novoRegistro();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Grupo %s incluído com sucesso.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("Grupo %s alterado com sucesso.", registro);
	}

	public Grupo getGrupo() {
		return grupo;
	}
	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public List<Familia> getFamilias() {
		return familias;
	}

}
