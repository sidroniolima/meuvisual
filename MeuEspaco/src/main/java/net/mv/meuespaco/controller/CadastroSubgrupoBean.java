package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.service.GrupoService;
import net.mv.meuespaco.service.SubgrupoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Implementação do Bean de Controller para a entidade Subgrupo 
 * responsável pelo cadastro edição. 
 * 
 * @author Sidronio
 * 17/11/2015
 */
@Named
@ViewScoped
public class CadastroSubgrupoBean extends CadastroSingle implements Serializable {

	private static final long serialVersionUID = 8844114025770771530L;

	@Inject
	private SubgrupoService subgrupoService;
	
	@Inject
	private GrupoService grupoService;
	
	private Subgrupo subgrupo;
	private List<Grupo> grupos;
	
	@PostConstruct
	@Override
	public void init() {
		
		if (isEdicao()) {
			
			subgrupo = subgrupoService.buscaPeloCodigoComGrupoEFamilia(getParamCodigo());
		} else {
			
			novoRegistro();
		}
		
		grupos = grupoService.listaTodosComFamilia();
	}

	@Override
	void novoRegistro() {
		subgrupo = new Subgrupo();
	}

	@Override
	public void salvar() {
		
		try {
			
			subgrupoService.salva(subgrupo);
			
			if (isEdicao()){
				
				FacesUtil.addSuccessMessage(getMensagemDeAlteracao(subgrupo.getDescricao()));
				
			} else {
				FacesUtil.addSuccessMessage(getMensagemDeInclusao(subgrupo.getDescricao()));
			}
			
			novoRegistro();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Subgrpo %s incluído com sucesso.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("Subgrpo %s alterado com sucesso.", registro);
	}

	public Subgrupo getSubgrupo() {
		return subgrupo;
	}
	public void setSubgrupo(Subgrupo subgrupo) {
		this.subgrupo = subgrupo;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

}
