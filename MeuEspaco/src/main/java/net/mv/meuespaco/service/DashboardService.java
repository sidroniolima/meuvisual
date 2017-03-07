package net.mv.meuespaco.service;

import java.util.List;

import net.mv.meuespaco.model.dashboard.LoginInfo;

/**
 * Serviços relacionados ao Dashboard, criado no início da aplicação. 
 * Fornece informações sobre usuários logados e peças reservadas com ações 
 * sobre estas informações.
 * 
 * @author Sidronio
 * @created 28/06/2016
 */
public interface DashboardService {

	/**
	 * Adiciona um login e suas informações à lista.
	 * 
	 * @param login
	 */
	public void adicionaLogin(LoginInfo login);
	
	/**
	 * Remove um login dos ativos.
	 * 
	 * @param login que efetuou logout.
	 */
	public void removeLogin(LoginInfo login);

	/**
	 * Lista os logins ativos do sistema.
	 * @return lista de logins.
	 */
	public List<LoginInfo> getLogins();
	
	/**
	 * Desabilita logins on sistema.
	 */
	public void desabilitaLogin();
	
	/**
	 * Habilita logins no sistema.
	 */
	public void habilitaLogin();

	public String getMsgDeManutencao();

	public void setMsgDeManutencao(String msgDeManutencao);

	boolean isDesabilitaLogin();
	
	public int qtdUsuariosLogados();
}
