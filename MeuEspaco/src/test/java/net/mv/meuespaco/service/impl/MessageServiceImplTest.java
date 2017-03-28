package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.mv.meuespaco.model.Message;
import net.mv.meuespaco.service.MessageService;

public class MessageServiceImplTest {

	private MessageService messageSrvc;
	
	public MessageServiceImplTest() 
	{
		this.messageSrvc = new MessageServiceImpl();
	}
	
	@Test
	public void mustFindByUsuario() 
	{
		messageSrvc.findByUsuario("000042").stream().forEach(System.out::println);
	}
	
	@Test
	public void mustFindById()
	{
		Message message = messageSrvc.findByCodigo(1L);
		
		assertFalse("Não null", null == message);
		
		System.out.println(message);
	}
	
	@Test
	public void mustCreate()
	{
		Message msg1 = new Message("Teste de mensagem", "008741", "NORMAL");
		
		System.out.println(messageSrvc.createMessage(msg1));
	}
	
	@Test
	public void mustUpdate()
	{
		Message message = criaTempMessage();
		
		System.out.println("Created: " + message);

		message.setMessage("Teste de atualização.");
		
		messageSrvc.updateMessage(message);
		Message messageUpdated = messageSrvc.findByCodigo(message.getId());
		
		assertEquals("Message atualizada.", "Teste de atualização.", messageUpdated.getMessage());
	}

	private Message criaTempMessage() {
		Message message = new Message("Mensagem original.", "000042", "NORMAL");
		message = this.messageSrvc.createMessage(message);
		return message;
	}
	
	@Test
	public void mustDelete()
	{
		Message message = criaTempMessage();

		messageSrvc.deleteMessage(message.getId());
				
		Message deleted = messageSrvc.findByCodigo(23L);
		
		assertTrue("Message deletada.", deleted == null);
	}
	
}
