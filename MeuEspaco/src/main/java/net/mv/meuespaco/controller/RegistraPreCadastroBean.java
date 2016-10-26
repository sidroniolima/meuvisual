package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Registra um pré-cadastro de cliente.
 * 
 * @author Sidronio
 * 22/12/2015
 */
@Named
@ViewScoped
public class RegistraPreCadastroBean implements Serializable {

	private static final long serialVersionUID = -3075594197996152114L;

	@Inject
	private ClienteService clienteSrvc;
	
	private Cliente preCadastro;
	
	/**
	 * Inicia os campos do Bean.
	 */
	@PostConstruct
	public void init() {
		preCadastro = new Cliente();
	}
	
	/**
	 * Registra um pré-cadastro de cliente.
	 */
	public void registra() {

		try {
			clienteSrvc.salvaPreCadastro(preCadastro);
			
			FacesUtil.addSuccessMessage(
					"Seu cadastro foi efetuado com sucesso. Seu acesso será liberado em até 72 horas.");
			
			preCadastro = new Cliente();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	/**
	 * @return the preCadastro
	 */
	public Cliente getPreCadastro() {
		return preCadastro;
	}
	/**
	 * @param preCadastro the preCadastro to set
	 */
	public void setPreCadastro(Cliente preCadastro) {
		this.preCadastro = preCadastro;
	}

}
