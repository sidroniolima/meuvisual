package net.mv.meuespaco.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.ConnectException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonSyntaxException;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.integracao.CustomPageImpl;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.model.integracao.MessageLevel;
import net.mv.meuespaco.service.MessageService;
import net.mv.meuespaco.util.Paginator;

public class MessageServiceImplTest {

	private MessageService messageSrvc;
	private PropertiesLoad props = new PropertiesLoad();
	
	public MessageServiceImplTest() 
	{
		this.messageSrvc = new MessageServiceImpl(props);
	}
	
	private void createMessages() throws IntegracaoException, JsonSyntaxException, ConnectException 
	{
		for (int i=0;i<30;i++)
		{
			this.messageSrvc.createMessage(new Message("Teste de mensagem automática.", "000042", MessageLevel.NORMAL));
		}
	}
	
	@Before
	@After
	public void deleteAll() throws JsonSyntaxException, ConnectException
	{
		List<Message> messages;
		try 
		{
			messages = this.messageSrvc.getAll();
			messages.stream().mapToLong(Message::getId).forEach(i -> {
				try 
				{
					messageSrvc.deleteMessage(i);
					
				} catch (IntegracaoException e) 
				{
					System.out.println(e.getMessage());
				}
			});
		
		} catch (IntegracaoException e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void mustFindByUsuario() throws JsonSyntaxException, ConnectException
	{
		try {
			messageSrvc.findByUsuario("000042").stream().forEach(System.out::println);
		} catch (IntegracaoException e) {
			System.out.println(e.getMessage());
		}
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
		
		try {
			System.out.println(messageSrvc.createMessage(msg1));
		} catch (IntegracaoException e) {
			System.out.println(e.getMessage());
		}
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

	private Message criaTempMessage() throws IntegracaoException
	{
		Message message = new Message("Mensagem original.", "000042", MessageLevel.NORMAL);
		message = this.messageSrvc.createMessage(message);
		return message;
	}
	
	@Test
	public void mustDelete() throws IntegracaoException
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
	
	@Test
	public void mustFindAll()
	{
		try 
		{
			criaTempMessage();
			
			List<Message> messages = this.messageSrvc.getAll();
			
			assertFalse("Mensagens", messages.isEmpty());
			
		} catch (IntegracaoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void mustListAllByPage()
	{
		Paginator paginator = new Paginator(10);
		
		try 
		{
			CustomPageImpl<Message> page = this.messageSrvc.listAllByPagination(paginator.getPage(), paginator.getQtdPorPagina());
			assertTrue("Conteúdo recuperado.", null != page.getContent());
			
		} catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
			
	
	@Test
	public void mustListAllByPageAndDoPaginator()
	{
		Paginator paginator = new Paginator(10);
		
		try 
		{
			createMessages();
			
			System.out.println("PAGE: " + paginator.getPage() + "***********************************************");
			
			CustomPageImpl<Message> page = this.messageSrvc.listAllByPagination(paginator.getPage(), paginator.getQtdPorPagina());

			paginator.setTotalDeRegistros(page.getTotalElements());
			paginator.setTotalPages(page.getTotalPages());
			
			page.getContent().stream().forEach(System.out::println);
			/*
			paginator.nextPage();
			
			System.out.println("PAGE: " + paginator.getPage() + "***********************************************");
			
			page = this.messageSrvc.listAllByPagination(paginator.getPage(), paginator.getQtdPorPagina());
			paginator.setPaged(page.getTotalElements(), page.getTotalPages());
			page.getContent().stream().forEach(System.out::println);
			*/
			paginator.goToPage(0);
			
			System.out.println("PAGE: " + paginator.getPage() + "***********************************************");
			
			page = this.messageSrvc.listAllByPagination(paginator.getPage(), paginator.getQtdPorPagina());
			paginator.setPaged(page.getTotalElements(), page.getTotalPages());
			page.getContent().stream().forEach(System.out::println);
			
		} catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
	}

	@Test
	public void mustListByUsarioByPage()
	{
		Paginator paginator = new Paginator(10);
		
		try 
		{
			createMessages();
			
			CustomPageImpl<Message> page = this.messageSrvc.listByUsuarioByPagination("000042", paginator.getPage(), paginator.getQtdPorPagina());

			paginator.setTotalDeRegistros(page.getTotalElements());
			paginator.setTotalPages(page.getTotalPages());
			
			page.getContent().stream().forEach(System.out::println);
			
			paginator.nextPage();
			
			System.out.println("PAGE: " + paginator.getPage() + "***********************************************");

			page = this.messageSrvc.listAllByPagination(paginator.getPage(), paginator.getQtdPorPagina());
			page.getContent().stream().forEach(System.out::println);
			
		} catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
		
	}
	
}
