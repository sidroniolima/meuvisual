package net.mv.meuespaco.model;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.reflect.TypeToken;

/**
 * Modelo de Message para integração com API para 
 * CRUD de mensagens para clientes.
 * 
 * @author sidronio
 * @created 115/03/2017
 */
@XmlRootElement
public class Message implements Serializable {

	private static final long serialVersionUID = -4299853234574177542L;
	
	private Long id;
	private String message;
	private String usuario;
	private String nivel;
	
	public Message() {	}
	
	public Message(Long id, String message, String usuario, String nivel) 
	{
		this.id = id;
		this.message = message;
		this.usuario = usuario;
		this.nivel = nivel;
	}

	public Message(String message, String usuario, String nivel) 
	{
		this.message = message;
		this.usuario = usuario;
		this.nivel = nivel;
	}
	
	/**
	 * Informa se a mensagem é de nível prioritária.
	 * 
	 * @return se prioritária ou não.
	 */
	public boolean isPrioritaria()
	{
		return this.nivel.equals("PRIORITARIA");
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	@Override
	public String toString() {
		return "Message \n"+
				"id=" + id + "\t" + 
				"usuario=" + usuario + "\t" + 
				"message=" + message + "\t" +
				"nivel=" + nivel;
	}
	
	/**
	 * Cria um objeto List para Message utilizado 
	 * na desserialização.
	 * 
	 * @return
	 */
	public static Type getSerializedCollectionType()
	{
		return new TypeToken<ArrayList<Message>>(){}.getType();
	}
}
