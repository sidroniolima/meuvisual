package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import net.mv.meuespaco.model.dashboard.LoginInfo;

public class DashboardServiceImplTest {

	private DashboardServiceImpl dash;
	private String idSession01;
	private String idSession02;
	private LoginInfo login01;
	private LoginInfo login02;
	
	@Before
	public void setup()
	{
		dash = new DashboardServiceImpl();
		idSession01 = "dqbjAFbi0GcSzGVk3-IPaBlgD2Flpqbw0WFAMvaw";
		idSession02 = "UXCtjDbJE0JUZuPEm_e4DKNSdP2RUz06rj2UKHDd";
		
		login01 = new LoginInfo(idSession01);
		login02 = new LoginInfo(idSession02);
		
	}
	
	@Test
	public void deveAdicionarUmLogin() {
		dash.adicionaLogin(login01);
		dash.adicionaLogin(login01);
		dash.adicionaLogin(login02);
		
		dash.removeLogin(new LoginInfo(idSession01));
		dash.removeLogin(new LoginInfo(idSession01));
		dash.removeLogin(new LoginInfo(idSession02));
		
		assertEquals("Check da qtd de logins...", 0, dash.logins.size());
	}
	
	@Test
	public void naoDeveAdicionarLoginsDaMesmaSessao() {
		dash.adicionaLogin(login01);
		dash.adicionaLogin(login01);
		dash.adicionaLogin(login02);
		
		assertEquals("Check da qtd de logins...", 2, dash.logins.size());
	}

}
