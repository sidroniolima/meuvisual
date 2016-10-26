package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;

import net.mv.meuespaco.annotations.CarrinhoConsignadoBeanAnnotation;
import net.mv.meuespaco.annotations.CarrinhoVendaBeanAnnotation;
import net.mv.meuespaco.annotations.UsuarioLogado;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.dashboard.LoginInfo;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.DashboardService;
import net.mv.meuespaco.service.LoginService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -5199376091795031527L;

	@Inject
	private LoginService loginSrvc;
	
	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	@CarrinhoConsignadoBeanAnnotation
	private CarrinhoConsignadoBean carrinhoConsignadoBean;
	
	@Inject
	@CarrinhoVendaBeanAnnotation
	private CarrinhoVendaBean carrinhoVendaBean;
	
	private Usuario userLogged;
	private String sessionId;
	
	private String username;
	private String password;
	
	private String mensagemPadrao = "Bem vinda(o), %s. %s";

	@Inject
	private DashboardService dashSrvc;
	
	@PreDestroy
	public void onDestroyLogouAndClean() 
	{
		if (userIsLogged())
		{
			dashSrvc.removeLogin(new LoginInfo(sessionId));
			
			loginSrvc.logoutAutomatico(userLogged);
			carrinhoConsignadoBean.esvazia();
			carrinhoVendaBean.esvazia();
		}
	}
	
	public String logout()
	{
		dashSrvc.removeLogin(new LoginInfo(sessionId));
		
		loginSrvc.logoutManual(userLogged);
		carrinhoConsignadoBean.esvazia();
		carrinhoVendaBean.esvazia();
		userLogged = null;
		
		return "/public/register.xhtml?faces-redirect=true";
	}
	
	/**
	 * Efetua o login.
	 * 
	 * @return
	 */
	public String login() {
		
		try {
		
			if (!userIsLogged())
			{
				userLogged = loginSrvc.login(username, password);
				sessionId = SecurityUtils.getSubject().getSession().getId().toString();
						
				carrinhoConsignadoBean.criaCarrinho();
				carrinhoVendaBean.criaCarrinho();
				
				dashSrvc.adicionaLogin(new LoginInfo(
						sessionId, 
						userLogged, 
						LocalDateTime.now(), 
						SecurityUtils.getSubject()));
			}
			
			if (userLogged.isCliente()) {
				return "index-cliente";
			} else {
				return "index-admin";
			}
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(String.format("Não foi possível logar no site. %s", e.getMessage()));
			
			return "";
			
		}
		
	}

	/**
	 * Verifica se o usuário está logad.
	 * 
	 * @return se há usuário logado ou não.
	 */
	private boolean userIsLogged() {
		return null != userLogged;
	}
	
	/**
	 * Informa a mensagem ao usuário (Cliente) logado.
	 * 
	 * @return
	 */
	public String getMensagemInicial() {
		
		try {
		
			clienteSrvc.verificaSeOUsuarioLogadoPodeEscolher();
			
			return String.format(mensagemPadrao, userLogged.primeiroNome(), "Ciclo aberto.");
		
		} catch (RegraDeNegocioException e) {

			return String.format(mensagemPadrao, userLogged.primeiroNome(), "Ciclo encerrado.");
		}
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userLogged
	 */
	@Produces
	@UsuarioLogado
	public Usuario getUserLogged() {
		return userLogged;
	}

}
