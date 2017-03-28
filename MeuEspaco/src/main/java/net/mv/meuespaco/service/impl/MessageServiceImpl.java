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

import com.google.gson.Gson;

import net.mv.meuespaco.model.Message;
import net.mv.meuespaco.service.MessageService;

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
	public List<Message> findByUsuario(String usuario) 
	{
		String urlFormed = String.format(apiUrlByUser, usuario);
		
		List<Message> messages = 
				new Gson().fromJson(this.get(urlFormed).readEntity(String.class), Message.getSerializedCollectionType());

		return messages;
	}
	
	@Override
	public List<Message> findByUsuarioNaoLidas(String usuario) 
	{
		String urlFormed =String.format(this.apiUrlByUserNews, usuario);
		
		List<Message> novas = 
				new Gson().fromJson(this.get(urlFormed).readEntity(String.class), Message.getSerializedCollectionType());
		
		return novas;
	}
	
	@Override
	public Message read(Message message) 
	{
		String url = String.format(this.apiUrlRead, message.getId());
		
		return new Gson().fromJson(this.update(url, message).readEntity(String.class), Message.class);
	}

	@Override
	public List<Message> getAll() 
	{
		return new Gson()
				.fromJson(this.get(this.apiUrlAll)
						.readEntity(String.class), Message.getSerializedCollectionType());
	}
	
	@Override
	public Message findByCodigo(Long id) 
	{
		return new Gson().fromJson(
				this.get(String.format(apiUrlById, id.toString())).readEntity(String.class), Message.class);
	}

	@Override
	public Message createMessage(Message message) 
	{
		Response clientResponse = getRestClient()
				.target(apiUrlAll)
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.post(Entity.entity(new Gson().toJson(message), MediaType.APPLICATION_JSON_TYPE));
		
		return new Gson().fromJson(clientResponse.readEntity(String.class), Message.class);
	}
	
	@Override
	public Message updateMessage(Message message) 
	{
		String url = String.format(this.apiUrlById, message.getId());
		
		return new Gson().fromJson(this.update(url, message).readEntity(String.class), Message.class);
	}
	
	@Override
	public void deleteMessage(Long id) 
	{
		getRestClient()
				.target(String.format(apiUrlById, id.toString()))
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.delete();
		
		log.log(Level.INFO, String.format("Message %s excluída.", id));
	}

	private ResteasyClient getRestClient() 
	{
		ResteasyClient client = new ResteasyClientBuilder()
				.hostnameVerification(ResteasyClientBuilder.HostnameVerificationPolicy.ANY)
				.build();
		
		return client;
	}
	
	private Response get(String url)
	{
		return this.getRestClient()
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.get(Response.class);
	}
	
	private Response update(String url, Message message)
	{
		return this.getRestClient()
				.target(url)
				.request(MediaType.APPLICATION_JSON)
				.header("Authorization", "Basic " + Base64.encodeBase64String("admin:pass$".getBytes()))
				.put(Entity.entity(new Gson().toJson(message), MediaType.APPLICATION_JSON_TYPE));
	}
}
