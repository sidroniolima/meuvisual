package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.ClienteService;

@Named
@ViewScoped
public class MeuCadastroBean implements Serializable {

	private static final long serialVersionUID = 1497313314075340902L;

	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	String mensagem;
	
	@PostConstruct
	public void init() {
		
		mensagem = clienteSrvc.criaMensagemParaClienteLogado();
	}
	
	public Cliente getCliente() {
		//return clienteSrvc.buscaClientePeloUsuario(loginBean.getUserLogged());
		return clienteLogado;
	}

	/**
	 * @return the mensagem
	 */
	public String getMensagem() {
		return mensagem;
	}

}
