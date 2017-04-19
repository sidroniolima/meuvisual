package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.controller.filtro.FiltroMensagem;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.integracao.CustomPageImpl;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.MessageService;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.service.SemanaService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class PesquisaMensagemBean extends PesquisaSingle implements Serializable 
{
	private static final long serialVersionUID = 4047656994279919615L;
	
	@Inject
	private MessageService messageSrvc;

	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	private RegiaoService regiaoSrvc;
	
	@Inject
	private SemanaService semanaSrvc;

	private Message messageSelecionada;
	
	private FiltroMensagem filtro;
	
	private List<Message> mensagens;
	
	private List<Semana> semanas;
	
	@Override
	@PostConstruct
	void init() 
	{
		filtro = new FiltroMensagem();
		semanas = semanaSrvc.buscaTodas();
		
		this.listarComPaginacao();
	}

	@Override
	public void excluir() 
	{
		if (null != messageSelecionada)
		{
			try 
			{
				this.messageSrvc.deleteMessage(messageSelecionada.getId());
				
				FacesUtil.addSuccessMessage("A mesnagem selecionada foi excluída.");
				
				this.mensagens.remove(messageSelecionada);
				
				this.listarComPaginacao();
				
			} catch (IntegracaoException e) 
			{			
				FacesUtil.addErrorMessage("Não foi possível excluir a mensagem. " + e.getMessage());
			}
		}
	}

	@Override
	public void listarComPaginacao() 
	{
		if (filtro.isPreenchido())
		{

			try 
			{
				mensagens = this.messageSrvc.getAll();
				
				Regiao regiaoDoFiltro = null;
				
				if (null != filtro.getCodigoRegiao())
				{
					regiaoDoFiltro = this.regiaoSrvc.buscaPeloCodigoInterno(filtro.getCodigoRegiao());
				}
				
				List<String> codigosDosClientes = 
						clienteSrvc.filtraPeloModoEspecifico(new FiltroCliente(filtro.getCodigoCliente(), regiaoDoFiltro, filtro.getSemana()))
						.stream().map(Cliente::getCodigoSiga).collect(Collectors.toList());
				
				mensagens = mensagens
					.stream()
					.filter(m -> codigosDosClientes.contains(m.getUsuario()))
					.collect(Collectors.toList());					
			
			} catch (IntegracaoException e) 
			{
				FacesUtil.addErrorMessage("Não foi possível listar as mensagens. " + e.getMessage());
			}
			
		} else 
		{
			CustomPageImpl<Message> page = null;
			
			try 
			{
				System.out.println(this.getPaginator());
				page = this.messageSrvc.listAllByPagination(this.getPaginator().getPage(), this.getPaginator().getQtdPorPagina());
				
				this.getPaginator().setTotalDeRegistros(page.getTotalElements());
				this.getPaginator().setTotalPages(page.getTotalPages());
				
				mensagens = page.getContent();
				mensagens.stream().forEach(System.out::println);
			
			} catch (IntegracaoException e) 
			{
				FacesUtil.addErrorMessage("Não foi possível listar as mensagens. " + e.getMessage());
			}
		}
			
	}

	@Override
	String getMensagemDeExclusaoOk() 
	{
		return null;
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) 
	{
		return null;
	}

	@Override
	public String descricaoDoRegistro() 
	{
		return null;
	}

	public Message getMessageSelecionada() {
		return messageSelecionada;
	}
	public void setMessageSelecionada(Message messageSelecionada) {
		this.messageSelecionada = messageSelecionada;
	}

	public FiltroMensagem getFiltro() {
		return filtro;
	}
	public void setFiltro(FiltroMensagem filtro) {
		this.filtro = filtro;
	}

	public List<Message> getMensagens() {
		return mensagens;
	}

	public List<Semana> getSemanas() {
		return semanas;
	}
	
}
