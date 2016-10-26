package net.mv.meuespaco.service;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Usuario;

/**
 * Interface de Serviço de Login.
 * 
 * @author Sidronio
 * 05/01/2016
 */
public interface LoginService {

	/**
	 * Efetua o Login e registra o login no dashboard.
	 * 
	 * @param login
	 * @param senha
	 * @return Usuario localizado pelo login e senha.
	 * @throws RegraDeNegocioException 
	 */
	public Usuario login(String login, String senha) throws RegraDeNegocioException;
	
	/**
	 * Logout.
	 * @param usuario para logout.
	 */
	public void logoutManual(Usuario usuario);

	/**
	 * Logout com usuário via parâmetro.
	 * 
	 * @param usuario para logout.
	 */
	void logoutAutomatico(Usuario usuario);
}
