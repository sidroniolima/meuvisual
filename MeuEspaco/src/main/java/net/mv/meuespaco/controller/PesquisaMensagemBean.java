package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.controller.filtro.FiltroMensagem;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
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
	void init() 
	{
		filtro = new FiltroMensagem();
		semanas = semanaSrvc.buscaTodas();
	}

	@Override
	public void excluir() 
	{
		
	}

	@Override
	public void listarComPaginacao() 
	{
		
		if (filtro.isPreenchido())
		{
			Regiao regiaoDoFiltro = null;
			
			if (null != filtro.getCodigoRegiao())
			{
				regiaoDoFiltro = this.regiaoSrvc.buscaPeloCodigoInterno(filtro.getCodigoRegiao());
			}
			
			List<String> codigosDosClientes = 
					clienteSrvc.filtraCliente(new FiltroCliente(filtro.getCodigoCliente(), regiaoDoFiltro, filtro.getSemana()))
					.stream().map(Cliente::getCodigoSiga).collect(Collectors.toList());
			
			try {
				
				mensagens = this.messageSrvc.getAll();
				
				this.getPaginator().setTotalDeRegistros(mensagens.size());
				
				mensagens
					.stream()
					.filter(codigosDosClientes::contains)
					.skip(this.getPaginator().getFirstResult())
					.limit(this.getPaginator().getQtdPorPagina());
				
			} catch (IntegracaoException e) 
			{
				FacesUtil.addErrorMessage(e.getMessage());
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
