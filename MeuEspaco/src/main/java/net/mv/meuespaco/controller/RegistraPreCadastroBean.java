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

	private static final String MSG_CADASTRO 
				= "Seu cadastro foi efetuado com sucesso. " + 
				"O acesso para clientes que pegaram estojo pela primeira vez estará liberado na data do seu ciclo. "
				+ "Para os demais clientes o acesso estará liberado dentro de 72 horas.";

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
					MSG_CADASTRO);
			
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
