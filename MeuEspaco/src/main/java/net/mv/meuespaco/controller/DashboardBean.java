package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.model.dashboard.LoginInfo;
import net.mv.meuespaco.service.DashboardService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

	private static final long serialVersionUID = -6720843179671175499L;

	@Inject
	private DashboardService dashSrvc;
	
	private boolean login;
	private String msg;
	
	@PostConstruct
	public void init()
	{
		login = !this.dashSrvc.isDesabilitaLogin();
		msg = this.dashSrvc.getMsgDeManutencao();
	}
	
	/**
	 * Lista as informações dos logins.
	 * 
	 * @return lista de logins.
	 */
	public List<LoginInfo> getLogins()
	{
		return dashSrvc.getLogins();
	}
	
	/**
	 * Habilita ou desabilita o Login e atualiza a 
	 * mensagem de manutenção.
	 */
	public void atualiza()
	{
		if (login)
			this.dashSrvc.habilitaLogin();
		else 
			this.dashSrvc.desabilitaLogin();
		
		this.dashSrvc.setMsgDeManutencao(msg);
		
		FacesUtil.addSuccessMessage("Alterações realizadas com sucesso.");
	}
	
	/**
	 * Retorna a qtde usuários logados.
	 * 
	 * @return
	 */
	public int qtdUsuariosLogados()
	{
		return this.dashSrvc.qtdUsuariosLogados();
	}

	public boolean isLogin() {
		return login;
	}
	public void setLogin(boolean login) {
		this.login = login;
	}

	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
