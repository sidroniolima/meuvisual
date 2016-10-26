package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.dao.ClienteDAO;
import net.mv.meuespaco.model.loja.Cliente;

@SessionScoped
public class ClienteLogadoBean implements Serializable {

	private static final long serialVersionUID = 6985054493336825938L;
	
	@Inject
	private LoginBean loginBean;
	
	@Inject
	private ClienteDAO clienteDAO;
	
	private Cliente clienteLogado;
	
	@Produces
	@ClienteLogado
	public Cliente getClienteLogado() {
		
		if(null == clienteLogado)
		{
			clienteLogado = clienteDAO.buscaClientePeloUsuario(loginBean.getUserLogged());
		}
		return clienteLogado;
	}

}
