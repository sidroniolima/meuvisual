package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.model.integracao.MessageLevel;
import net.mv.meuespaco.service.MessageService;

public class MessageServiceImplTest {

	private MessageService messageSrvc;
	
	public MessageServiceImplTest() 
	{
		this.messageSrvc = new MessageServiceImpl();
	}
	
	@Test
	public void mustFindByUsuario() throws IntegracaoException 
	{
		messageSrvc.findByUsuario("000042").stream().forEach(System.out::println);
	}
	
	@Test
	public void mustFindById() throws IntegracaoException
	{
		Message message = criaTempMessage();
		
		Message finded = messageSrvc.findByCodigo(message.getId());
		
		assertFalse("Não null", null == finded);
		
		System.out.println(finded);
	}
	
	@Test
	public void mustCreate() throws IntegracaoException
	{
		Message msg1 = new Message("Teste de mensagem", "008741", MessageLevel.NORMAL);
		
		System.out.println(messageSrvc.createMessage(msg1));
	}
	
	@Test
	public void mustUpdate() throws IntegracaoException
	{
		Message message = criaTempMessage();
		
		System.out.println("Created: " + message);

		message.setMessage("Teste de atualização.");
		
		messageSrvc.updateMessage(message);
		Message messageUpdated = messageSrvc.findByCodigo(message.getId());
		
		assertEquals("Message atualizada.", "Teste de atualização.", messageUpdated.getMessage());
	}

	private Message criaTempMessage() throws IntegracaoException {
		Message message = new Message("Mensagem original.", "000042", MessageLevel.NORMAL);
		message = this.messageSrvc.createMessage(message);
		return message;
	}
	
	@Test
	public void mustDelete()
	{
		try 
		{
			Message message = criaTempMessage();
			messageSrvc.findByCodigo(message.getId());
			messageSrvc.deleteMessage(message.getId());
		
		} catch (IntegracaoException e) 
		{
			assertTrue(e.getMessage().contains("Não foi possível localizar a(s) mensagem(ns)."));
		}
	}
	
	@Test(expected=IntegracaoException.class)
	public void deveGerarErroNaCriacao() throws IntegracaoException
	{
		this.messageSrvc.createMessage(null);
	}
	
}
