package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class VisualizaEscolhaBean implements Serializable {

	private static final long serialVersionUID = 5541146596545103306L;

	@Inject
	private EscolhaService escolhaSrvc;
	
	@Inject @Param
	private Long paramCodigo;
	
	private Escolha escolha;
	
	@PostConstruct
	public void init() {
		
		if (null == paramCodigo) {
		
			FacesUtil.addErrorMessage("Não foi possível visualizar a escolha.");

		} else {
			escolha = escolhaSrvc.buscaComRelacionamentos(paramCodigo);
		}
		
	}

	/**
	 * @return the escolha
	 */
	public Escolha getEscolha() {
		return escolha;
	}
}
