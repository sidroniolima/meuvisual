package net.mv.meuespaco.model.integracao;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.reflect.TypeToken;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.EntidadeModel;

/**
 * Modelo de Message para integração com API para 
 * CRUD de mensagens para clientes.
 * 
 * @author sidronio
 * @created 115/03/2017
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message extends EntidadeModel implements Serializable {

	private static final long serialVersionUID = -4299853234574177542L;
	
	@XmlAttribute
	private Long id;
	
	@XmlAttribute
	private String message;
	
	@XmlAttribute
	private String usuario;
	
	@XmlAttribute
	private MessageLevel nivel;
	
	@XmlAttribute
	private LocalDateTime horarioCriacao;
	
	@XmlAttribute
	private LocalDateTime horarioLeitura;
	
	public Message() {	}
	
	public Message(Long id, String message, String usuario, MessageLevel nivel, LocalDateTime horarioCriacao,
			LocalDateTime horarioLeitura) 
	{
		this.id = id;
		this.message = message;
		this.usuario = usuario;
		this.nivel = nivel;
		this.horarioCriacao = horarioCriacao;
		this.horarioLeitura = horarioLeitura;
	}

	public Message(Long id, String message, String usuario, MessageLevel nivel) 
	{
		this.id = id;
		this.message = message;
		this.usuario = usuario;
		this.nivel = nivel;
	}

	public Message(String message, String usuario, MessageLevel nivel) 
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
		return this.nivel.equals(MessageLevel.PRIORITARIA);
	}
	
	/**
	 * Verifica se é uma mensagem não salva, portanto 
	 * nova.
	 * 
	 * @return se salva ou não. Nova ou não.
	 */
	public boolean isNew() 
	{
		return this.id == null;
	}
	
	@Override
	public void valida() throws RegraDeNegocioException 
	{
		if (null != this.message && !this.message.isEmpty() || null != this.usuario &&
				!this.usuario.isEmpty())
		{
			throw new RegraDeNegocioException("Todos os campos da mensagem deve ser preenchidos.");
		}
	}

	@Override
	public Long getCodigo() {
		return id;
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
	
	public MessageLevel getNivel() {
		return nivel;
	}
	public void setNivel(MessageLevel nivel) {
		this.nivel = nivel;
	}
	
	public LocalDateTime getHorarioCriacao() {
		return horarioCriacao;
	}
	public void setHorarioCriacao(LocalDateTime horarioCriacao) {
		this.horarioCriacao = horarioCriacao;
	}

	public LocalDateTime getHorarioLeitura() {
		return horarioLeitura;
	}
	public void setHorarioLeitura(LocalDateTime horarioLeitura) {
		this.horarioLeitura = horarioLeitura;
	}

	@Override
	public String toString() 
	{
		return "Message \n"+
				"id=" + id + "\t" + 
				"usuario=" + usuario + "\t" + 
				"message=" + message + "\t" +
				"nivel=" + nivel.getDescricao() +
				"horario de criação= " + horarioCriacao + 
				"horario de leitura= " + horarioLeitura;
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
