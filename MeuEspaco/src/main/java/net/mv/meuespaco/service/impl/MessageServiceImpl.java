package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.util.HttpResponseCodes;

import com.google.gson.Gson;

import net.mv.meuespaco.controller.filtro.FiltroMensagem;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.integracao.CustomPageImpl;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.service.MessageService;
import net.mv.meuespaco.util.Paginator;

/**
 * Implementação do serviço de comunicação com a API Hermes, de Mensagens.
 * 
 * @author sidronio
 * @created 20/03/2017
 */
@Stateless
public class MessageServiceImpl implements MessageService, Serializable
{
	private static final long serialVersionUID = 6409256827647594418L;

	private final Logger log = Logger.getLogger(MessageServiceImpl.class.getName());
	
	@Inject
	private PropertiesLoad props;
	
	private String apiUrlForTest = "/test/";
	private String apiUrl = "/messages/";
	private String apiUrlAll = "/messages/all/";
	private String apiUrlByUser = "/%s/messages/all/";
	private String apiUrlByUserNews = "/%s/messages/news/";
	private String apiUrlById = "/messages/%s";
	private String apiUrlRead = "/messages/%s/read";
	private String apiUrlAllByPage = "/messages/?page=%s&size=%s";
	private String apiUrlByUserByPage = "/%s/messages/?page=%s&size=%s&sort=horarioCriacao,desc";
	
	public MessageServiceImpl() 	{	}
	
	public MessageServiceImpl(PropertiesLoad props) 
	{
		this.props = props;
	}
	
	@Override
	public int createMessages(List<Message> messages) throws IntegracaoException
	{
		this.testaConexao();
		
		long qtd = messages
			.parallelStream()
			.peek(m -> {
				
				try 
				{
					this.createMessage(m);
					
				} catch (IntegracaoException e) 
				{
					log.warning("Nâo foi possível gerar a mensagem. " + e.getMessage());
				}
			})
			.count();
		
		return (int) qtd;
	}
	
	@Override
	public List<Message> findByUsuario(String usuario) throws IntegracaoException 
	{
		String urlFormed = String.format(this.makeUrl(apiUrlByUser), usuario);
		
		List<Message> messages = 
				new Gson().fromJson(this.get(urlFormed).readEntity(String.class), Message.getSerializedCollectionType());

		return messages;
	}
	
	@Override
	public List<Message> findByUsuarioNaoLidas(String usuario) throws IntegracaoException 
	{
		String urlFormed =String.format(this.apiUrlByUserNews, usuario);
		
		List<Message> novas = 
				new Gson().fromJson(this.get(urlFormed).readEntity(String.class), Message.getSerializedCollectionType());
		
		return novas;
	}
	
	@Override
	public Message read(Message message) throws IntegracaoException 
	{
		String url = String.format(this.apiUrlRead, message.getId());
		
		return new Gson().fromJson(this.update(url, message).readEntity(String.class), Message.class);
	}
	
	@Override
	public Message read(Long codigo) throws IntegracaoException 
	{
		return this.read(this.findByCodigo(codigo));
	}

	@Override
	public List<Message> getAll() throws IntegracaoException 
	{
		String urlFormed = String.format(this.apiUrlAll);
		
		return new Gson()
				.fromJson(this.get(urlFormed)
						.readEntity(String.class), Message.getSerializedCollectionType());
	}
	
	@Override
	public Message findByCodigo(Long id) throws IntegracaoException 
	{
		return new Gson().fromJson(
				this.get(String.format(apiUrlById, id.toString())).readEntity(String.class), Message.class);
	}

	@Override
	public Message createMessage(Message message) throws IntegracaoException 
	{
		Response clientResponse = null;
		
		try 
		{	
			String url = this.makeUrl(apiUrl);
			
			clientResponse = getRestClient()
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.post(Entity.entity(new Gson().toJson(message), MediaType.APPLICATION_JSON_TYPE));
			
			verificaResposta(clientResponse);
			
			return new Gson().fromJson(clientResponse.readEntity(String.class), Message.class);
			
		}catch (ProcessingException e) 
		{
			log.severe("O servidor de mensagens não está disponível.");
			throw new IntegracaoException("O servidor de mensagens não está disponível.");
		}		
	}

	@Override
	public void verificaResposta(Response clientResponse) throws IntegracaoException 
	{
		if (clientResponse.getStatus() == HttpResponseCodes.SC_INTERNAL_SERVER_ERROR)
		{
			throw new IntegracaoException("Não foi possível acessar o serviço.");
		}
		
		if (clientResponse.getStatus() == HttpResponseCodes.SC_BAD_REQUEST)
		{
			throw new IntegracaoException("Não foi possível criar a mensagem. Verifique os campos.");
		}
		
		if (clientResponse.getStatus() == HttpResponseCodes.SC_NOT_FOUND)
		{
			throw new IntegracaoException("Não foi possível localizar a(s) mensagem(ns).");
		}
		
		if (!(clientResponse.getStatus() == HttpResponseCodes.SC_CREATED || 
				clientResponse.getStatus() == HttpResponseCodes.SC_OK))
		{
			throw new IntegracaoException("Não foi possível completar a requisição. Certifique-se que você tem acesso e/ou os campos estão corretos.");
		}
	}
	
	@Override
	public Message updateMessage(Message message) throws IntegracaoException
	{
		return new Gson().fromJson(
				this.update(String.format(this.apiUrlById, message.getId()), message)
				.readEntity(String.class), Message.class);
	}
	
	@Override
	public void deleteMessage(Long id) throws IntegracaoException
	{
		Response response = null;
		
		try 
		{		
			response = getRestClient()
				.target(String.format(this.makeUrl(apiUrlById), id.toString()))
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.delete();
		
			this.verificaResposta(response);
			
			log.log(Level.INFO, String.format("Message %s excluída.", id));
			
		}catch (ProcessingException e) 
		{
			log.severe("O servidor de mensagens não está disponível.");
			throw new IntegracaoException("O servidor de mensagens não está disponível.");
		}
	}

	@Override
	public List<Message> listByFilterPagination(FiltroMensagem filtro, Paginator paginator) 
	{
		return null;
	}

	@Override
	public CustomPageImpl<Message> listAllByPagination(int page, int size) throws IntegracaoException 
	{
		Response clientResponse = this.get(String.format(apiUrlAllByPage, page, size));
		
		String response = clientResponse.readEntity(String.class);
				
		CustomPageImpl<Message> resultPage 
			= new Gson().fromJson(response, Message.getSerializedPageType());
		
		return resultPage;
	}	
	
	@Override
	public CustomPageImpl<Message> listByUsuarioByPagination(String usuario, int page, int size) throws IntegracaoException 
	{
		String url = String.format(apiUrlByUserByPage, usuario, page, size);
		
		Response clientResponse  = this.get(url);
		
		CustomPageImpl<Message> resultPage = new Gson().fromJson(clientResponse.readEntity(String.class), Message.getSerializedPageType());
		
		return resultPage;
	}
	
	private ResteasyClient getRestClient() 
	{
		ResteasyClient client = new ResteasyClientBuilder()
				.hostnameVerification(ResteasyClientBuilder.HostnameVerificationPolicy.ANY)
				.build();
		
		return client;
	}
	
	private Response get(String url) throws IntegracaoException
	{
		Response response = null;
		
		String urlMaked = this.makeUrl(url);

		try 
		{
			response = this.getRestClient()
					.target(urlMaked)
					.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
					.get(Response.class);
			
			this.verificaResposta(response);
		
		}catch (ProcessingException e) 
		{
			log.severe("O servidor de mensagens não está disponível.");
			throw new IntegracaoException("O servidor de mensagens não está disponível.");
		}
		
		return response;
	}
	
	private Response update(String url, Message message) throws IntegracaoException
	{
		Response response = null;
		
		try 
		{
		
			response = this.getRestClient()
					.target(this.makeUrl(url))
					.request(MediaType.APPLICATION_JSON)
					.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
					.put(Entity.entity(new Gson().toJson(message), MediaType.APPLICATION_JSON_TYPE));
			
			this.verificaResposta(response);
		
		}catch (ProcessingException e) 
		{
			log.severe("O servidor de mensagens não está disponível.");
			throw new IntegracaoException("O servidor de mensagens não está disponível.");
		}
		
		return response;
	}
	
	/**
	 * Testa a conexão com o servidor de mensagens.
	 */
	private void testaConexao() throws IntegracaoException
	{
		try
		{
			this.get(apiUrlForTest);
		}
		catch (IntegracaoException e) 
		{
			throw new IntegracaoException(e.getMessage());
		}
		
	}
	
	/**
	 * Make the complete Url with the server path and 
	 * the resource.
	 * 
	 * @param lastUrl
	 * @return
	 */
	private String makeUrl(String resource) 
	{
		return this.props.getProperty("hermes-path").concat(resource);
	}

}
