package net.mv.meuespaco.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.integracao.CustomPageImpl;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.MessageService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Lista mensagens do usuário logado.
 * 
 * @author sidronio
 * @created 17/04/17
 */
@Named
@RequestScoped
public class ListaMensagensBean extends ListaSimples
{
	@Inject
	private MessageService messageSrvc;

	@Inject
	@ClienteLogado
	private Cliente clienteLogado;

	private CustomPageImpl<Message> page;
	
	@PostConstruct
	public void init()
	{
		this.listarComPaginacao();
	}
	@Override
	public void listarComPaginacao() 
	{
		try 
		{
			this.page = messageSrvc
					.listByUsuarioByPagination(clienteLogado.getCodigoSiga(), 
							this.getPaginator().getPage(), this.getPaginator().getQtdPorPagina());
			
			this.getPaginator().setPaged(this.page.getTotalElements(), this.page.getTotalPages());
			
		} catch (IntegracaoException e) 
		{
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}
	
	/**
	 * Lê a mensagem pelo código.
	 * 
	 * @param message
	 */
	public void readMessage()
	{
		String codigo = Faces.getRequestParameter("id");
		
		if (null != codigo)
		{
			try 
			{
				this.messageSrvc.read(Long.valueOf(codigo));
			
			} catch (IntegracaoException e) 
			{
				FacesUtil.addErrorMessage(e.getMessage());
			}
		}

	}
	
	public CustomPageImpl<Message> getPage() 
	{
		return page;
	}
	
}
