package net.mv.meuespaco.service.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import net.mv.meuespaco.service.EscolhaService;

/**
 * Utilizado para alterar o status das escolhas no tempo 
 * definido.
 * 
 * @author Sidronio
 * @created 13/06/2016
 */
@Startup
@Singleton
public class AgendadorServiceImpl {

	@Inject
	private EscolhaService escolhaSrvc;
	
	private static final Logger logger = Logger.getLogger(AgendadorServiceImpl.class.getName());
	
	/**
	 * Executa a atualização das escolhas que já devem 
	 * ser enviadas, de acordo ocm a data.
	 * 
	 */
	@Schedule(hour="00", minute="00")
	public void enviarEscolhas()
	{
		escolhaSrvc.enviaEscolhasJaVencidas();
		logger.log(Level.INFO, "Escolhas vencidas atualizadas");
	}
}
