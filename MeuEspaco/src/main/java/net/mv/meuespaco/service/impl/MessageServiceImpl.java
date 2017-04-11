package net.mv.meuespaco.service.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.util.HttpResponseCodes;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.mv.meuespaco.controller.filtro.FiltroMensagem;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.MessageService;
import net.mv.meuespaco.util.Paginator;

/**
 * Implementação do serviço de comunicação com a API Hermes, de Mensagens.
 * 
 * @author sidronio
 * @created 20/03/2017
 */
@Stateless
public class MessageServiceImpl implements MessageService {

	private final String apiUrlAll = "http://localhost:8090/messages/";
	private final String apiUrlByUser = "http://localhost:8090/%s/messages/";
	private final String apiUrlByUserNews = "http://localhost:8090/%s/messages/news/";
	private final String apiUrlById = "http://localhost:8090/messages/%s";
	private final String apiUrlRead = "http://localhost:8090/messages/%s/read";
	
	private final Logger log = Logger.getLogger(MessageServiceImpl.class.getName());
	
	@Override
	public int createMessages(List<Message> messages) 
	{
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
	public List<Message> findByUsuario(String usuario) throws JsonSyntaxException, IntegracaoException 
	{
		String urlFormed = String.format(apiUrlByUser, usuario);
		
		List<Message> messages = 
				new Gson().fromJson(this.get(urlFormed).readEntity(String.class), Message.getSerializedCollectionType());

		return messages;
	}
	
	@Override
	public List<Message> findByUsuarioNaoLidas(String usuario) throws JsonSyntaxException, IntegracaoException 
	{
		String urlFormed =String.format(this.apiUrlByUserNews, usuario);
		
		List<Message> novas = 
				new Gson().fromJson(this.get(urlFormed).readEntity(String.class), Message.getSerializedCollectionType());
		
		return novas;
	}
	
	@Override
	public Message read(Message message) throws JsonSyntaxException, IntegracaoException 
	{
		String url = String.format(this.apiUrlRead, message.getId());
		
		return new Gson().fromJson(this.update(url, message).readEntity(String.class), Message.class);
	}

	@Override
	public List<Message> getAll() throws JsonSyntaxException, IntegracaoException 
	{
		return new Gson()
				.fromJson(this.get(this.apiUrlAll)
						.readEntity(String.class), Message.getSerializedCollectionType());
	}
	
	@Override
	public Message findByCodigo(Long id) throws JsonSyntaxException, IntegracaoException 
	{
		return new Gson().fromJson(
				this.get(String.format(apiUrlById, id.toString())).readEntity(String.class), Message.class);
	}

	@Override
	public Message createMessage(Message message) throws IntegracaoException 
	{
		System.out.println(new Gson().toJson(message));
		
		Response clientResponse = getRestClient()
				.target(apiUrlAll)
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.post(Entity.entity(new Gson().toJson(message), MediaType.APPLICATION_JSON_TYPE));
		
		verificaResposta(clientResponse);
		
		return new Gson().fromJson(clientResponse.readEntity(String.class), Message.class);
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
			System.out.println(clientResponse.getStatus());
			throw new IntegracaoException("Não foi possível criar a mensagem. Certifique-se que você tem acesso e/ou os campos estão corretos.");
		}
	}
	
	@Override
	public Message updateMessage(Message message) throws IntegracaoException 
	{
		String url = String.format(this.apiUrlById, message.getId());
		
		return new Gson().fromJson(this.update(url, message).readEntity(String.class), Message.class);
	}
	
	@Override
	public void deleteMessage(Long id) throws IntegracaoException 
	{
		Response response = getRestClient()
				.target(String.format(apiUrlById, id.toString()))
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.delete();
		
		this.verificaResposta(response);
		
		log.log(Level.INFO, String.format("Message %s excluída.", id));
	}

	@Override
	public List<Message> listByFilterPagination(FiltroMensagem filtro, Paginator paginator) 
	{
		return null;
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
		Response response = this.getRestClient()
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.get(Response.class);
		
		this.verificaResposta(response);
		
		return response;
	}
	
	private Response update(String url, Message message) throws IntegracaoException
	{
		Response response = this.getRestClient()
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.put(Entity.entity(new Gson().toJson(message), MediaType.APPLICATION_JSON_TYPE));
		
		this.verificaResposta(response);
		
		return response;
	}

}
