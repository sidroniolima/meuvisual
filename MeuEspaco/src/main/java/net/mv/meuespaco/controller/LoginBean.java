package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.primefaces.context.RequestContext;

import com.google.gson.Gson;

import net.mv.meuespaco.annotations.CarrinhoBrindeBeanAnnotation;
import net.mv.meuespaco.annotations.CarrinhoConsignadoBeanAnnotation;
import net.mv.meuespaco.annotations.CarrinhoVendaBeanAnnotation;
import net.mv.meuespaco.annotations.UsuarioLogado;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.dashboard.LoginInfo;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.DashboardService;
import net.mv.meuespaco.service.LoginService;
import net.mv.meuespaco.service.MessageService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -5199376091795031527L;
	
	private final Logger log = Logger.getLogger(LoginBean.class.getSimpleName());

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
	
	@Inject
	@CarrinhoBrindeBeanAnnotation
	private CarrinhoBrindeBean carrinhoBrindeBean;
	
	private Usuario userLogged;
	private String sessionId;
	
	private String username;
	private String password;
	
	private String mensagemPadrao = "Bem vinda(o), %s. %s";
	
	@Inject
	private DashboardService dashSrvc;
	
	private Map<String, List<String>> map = new HashMap<String, List<String>>();
	
	@Inject
	private MessageService msgService;
	
	Optional<Cliente> optClienteLogado;

	private List<Message> messages;
	private List<Message> priorityMessages;
	
	private String mustShowPriorityMessages = "no";

	private String json;
	
	public LoginBean() 
	{
		messages = new ArrayList<Message>();
		priorityMessages = new ArrayList<Message>();
	}
	
	@PreDestroy
	public void onDestroyLogouAndClean() 
	{
		if (userIsLogged())
		{
			dashSrvc.removeLogin(new LoginInfo(sessionId));
			
			loginSrvc.logoutAutomatico(userLogged);
			carrinhoConsignadoBean.esvazia();
			carrinhoVendaBean.esvazia();
			carrinhoBrindeBean.esvazia();
		}
	}
	
	public String logout()
	{
		dashSrvc.removeLogin(new LoginInfo(sessionId));
		
		loginSrvc.logoutManual(userLogged);
		carrinhoConsignadoBean.esvazia();
		carrinhoVendaBean.esvazia();
		carrinhoBrindeBean.esvazia();
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
				carrinhoBrindeBean.criaCarrinho();

				optClienteLogado = Optional.ofNullable(clienteSrvc.buscaClientePeloUsuarioLogado());
				
				dashSrvc.adicionaLogin(new LoginInfo(
						sessionId, 
						userLogged, 
						LocalDateTime.now(), 
						SecurityUtils.getSubject(),
						optClienteLogado.get()));
			}
			
			return redirecionaCliente();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(String.format("Não foi possível logar no site. %s", e.getMessage()));
			
			return "";
		}
		
	}

	/**
	 * Redireciona o cliente de acordo com a permissão.
	 */
	private String redirecionaCliente() {
		
		if (userLogged.isAdmin())
		{
			return "index-admin";
		}
		
		if (userLogged.isVendaApenas())
		{
			return "index-venda";
		}
		
		return "index-cliente";
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
	 * Número de mensagens não lidas.
	 * @return número.
	 */
	public int numberOfMessages()
	{
		if(null != this.messages)
		{
			return this.messages.size();
		}
		
		return 0;
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

	public void mostraObservers() 
	{
		this.map.values().forEach(System.out::println); 
	}

	public List<Message> getMessages() 
	{
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	/**
	 * Push das mensagens para o usuário logado.
	 */
	public void doPull()
	{
		if (optClienteLogado.isPresent())
		{
			try 
			{
				this.messages = msgService.findByUsuarioNaoLidas(optClienteLogado.get().getCodigoSiga());
				
				extraiPrioritarias();
				
				log.info("Recuperando as mensagens da API Hermes.");
				
			} catch (IntegracaoException e) 
			{
				log.warning("Não foi possível recuperar as mensagens não lidas do usuário. " + e.getMessage());
			}
			
		} else 
		{
			log.warning("Não foi possível identificar o cliente logado para o login " + this.username);
		}
	}

	/**
	 * Extrai as mensagens prioritárias não lidas.
	 */
	private void extraiPrioritarias() 
	{
		this.priorityMessages.clear();
		
		this.priorityMessages = this.messages
				.stream()
				.filter(Message::isPrioritaria)
				.collect(Collectors.toList());
	
		this.mustShowPriorityMessages = this.priorityMessages.size() > 0 ? "yes" : "no";
		
		RequestContext.getCurrentInstance().execute("countMsg = " + this.numberOfMessages());
		RequestContext.getCurrentInstance().execute("must = '" + this.mustShowPriorityMessages + "'");
		RequestContext.getCurrentInstance().execute("list = " + this.getJson());
		
		this.priorityMessages.stream().forEach(m -> {
			try 
			{
				this.msgService.read(m);
				
			} catch (IntegracaoException e) 
			{
				log.warning("Não foi possível marcar a mensagem como lida. " + e.getMessage());
			}
		});
	}
	
	public List<Message> getPriorityMessages() 
	{		
		return priorityMessages;
	}

	public String getMustShowPriorityMessages() {
		return mustShowPriorityMessages;
	}
	public void setMustShowPriorityMessages(String mustShowPriorityMessages) {
		this.mustShowPriorityMessages = mustShowPriorityMessages;
	}
	
	public String getJson() {
		return new Gson().toJson(this.priorityMessages.stream().map(Message::getMessage).collect(Collectors.toList()));
	}
}
