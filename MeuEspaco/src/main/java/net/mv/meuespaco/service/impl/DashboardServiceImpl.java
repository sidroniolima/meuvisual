package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import net.mv.meuespaco.model.dashboard.LoginInfo;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.DashboardService;

@ApplicationScoped
public class DashboardServiceImpl implements DashboardService, Serializable {

	private static final long serialVersionUID = 2707933773293150856L;
	
	private boolean desabilitaLogin = false;
	private String msgDeManutencao;

	List<LoginInfo> logins = new ArrayList<LoginInfo>();

	@Override
	public List<Cliente> getClientesLogados() 
	{
		return logins
				.stream()
				.map(LoginInfo::getCliente)
				.collect(Collectors.toList());
	}
	
	@Override
	public void adicionaLogin(LoginInfo login) 
	{
		if (!logins.contains(login))
		{
			logins.add(login);
		}
	}
	
	@Override
	public int qtdUsuariosLogados() 
	{
		return logins.size();
	}
	
	@Override
	public void removeLogin(LoginInfo login) {
		logins.remove(login);
	}
	
	@Override
	public List<LoginInfo> getLogins() {
		return logins;
	}

	@Override
	public void desabilitaLogin() {
		this.desabilitaLogin = true;
	}
	
	@Override
	public void habilitaLogin() {
		this.desabilitaLogin = false;
	}
	
	@Override
	public String getMsgDeManutencao() {
		return msgDeManutencao;
	}
	@Override
	public void setMsgDeManutencao(String msgDeManutencao) {
		this.msgDeManutencao = msgDeManutencao;
	}

	@Override
	public boolean isDesabilitaLogin() {
		return desabilitaLogin;
	}
}
