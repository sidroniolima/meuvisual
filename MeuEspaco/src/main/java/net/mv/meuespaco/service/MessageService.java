package net.mv.meuespaco.service;

import java.util.List;

import javax.ws.rs.core.Response;

import net.mv.meuespaco.controller.filtro.FiltroMensagem;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.integracao.CustomPageImpl;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.util.Paginator;

public interface MessageService {

	public List<Message> findByUsuario(String usuario) throws IntegracaoException;
	
	public Message findByCodigo(Long id) throws IntegracaoException;
	
	public Message createMessage(Message message) throws IntegracaoException;

	public Message updateMessage(Message message) throws IntegracaoException;
	
	public void deleteMessage(Long id) throws IntegracaoException;
	
	public List<Message> getAll() throws IntegracaoException;
	
	public List<Message> findByUsuarioNaoLidas(String usuario) throws IntegracaoException;
	
	public Message read(Message message) throws IntegracaoException;

	/**
	 * Verifica o status da resposta do servidor e lança 
	 * as respectivas exceções. 
	 * 
	 * @param clientResponse resposta do servidor.
	 * @throws IntegracaoException exceção de integração correspondente ao erro.
	 */
	void verificaResposta(Response clientResponse) throws IntegracaoException;

	/**
	 * Cria uma lista de mensagens.
	 * 
	 * @param messages para serem criadas.
	 * @return quantidade criada.
	 * @throws IntegracaoException 
	 */
	public int createMessages(List<Message> messages) throws IntegracaoException;

	/**
	 * Lista Mensagem de acordo com o filtro selecionado de forma 
	 * paginada.
	 * 
	 * @param filtro
	 * @param paginator
	 * @return lista de mensagens.
	 */
	public List<Message> listByFilterPagination(FiltroMensagem filtro, Paginator paginator);
	
	public CustomPageImpl<Message> listAllByPagination(int page, int size) throws IntegracaoException;

}
