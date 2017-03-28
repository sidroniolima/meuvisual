package net.mv.meuespaco.service;

import java.util.List;

import net.mv.meuespaco.model.Message;

public interface MessageService {

	public List<Message> findByUsuario(String usuario);
	
	public Message findByCodigo(Long id);
	
	public Message createMessage(Message message);

	public Message updateMessage(Message message);
	
	public void deleteMessage(Long id);
	
	public List<Message> getAll();
	
	public List<Message> findByUsuarioNaoLidas(String usuario);
	
	public Message read(Message message);
}
