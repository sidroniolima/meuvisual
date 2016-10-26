package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.dashboard.LoginInfo;
import net.mv.meuespaco.service.DashboardService;
import net.mv.meuespaco.service.LoginService;
import net.mv.meuespaco.service.UsuarioService;

/**
 * Implementação da camada Servie do Login.
 * 
 * @author Sidronio
 * 05/01/2016
 */
public class LoginServiceImpl implements LoginService, Serializable{

	private static final long serialVersionUID = 8449003944571502171L;

	@Inject
	private UsuarioService usuarioSrvc;
	
	@Inject
	private DashboardService dashService;
	
	private static final Logger logger = Logger.getLogger(LoginServiceImpl.class.getName());
	
	@Override
	public Usuario login(String login, String senha) throws RegraDeNegocioException {
		
		Usuario usuario = usuarioSrvc.buscaUsuarioPorLoginESenha(login, senha);
		
		if (null == usuario) {
			throw new RegraDeNegocioException("Usuário e/ou senha inválidos.");
		}
		
		if (!usuario.isAtivo()) {
			throw new RegraDeNegocioException("Usuário inativo.");
		}
		
		if (dashService.isDesabilitaLogin())
		{
			throw new RegraDeNegocioException(
					String.format("O login está temporariamente desabilitado. %s", dashService.getMsgDeManutencao()));
		}
		
		UsernamePasswordToken token = new UsernamePasswordToken(login, senha);
		SecurityUtils.getSubject().login(token);
		
		logger.log(Level.INFO, String.format("Usuário %s logado.",	usuario.getLogin()));
		
		return usuario;
	}

	@Override
	public void logoutManual(Usuario usuario) {
		
		if (null != SecurityUtils.getSubject()) {
			
			if (SecurityUtils.getSubject().isAuthenticated()) {

				logger.log(Level.INFO, 
						String.format("Usuário %s efetuou logout.", usuario.getLogin()));

				SecurityUtils.getSubject().logout();
			}
			
		}
	}
	
	@Override
	public void logoutAutomatico(Usuario usuario) 
	{
		logger.log(Level.INFO, 
				String.format("Usuário %s desconectado por tempo excedido.", usuario.getLogin()));
	}

}
