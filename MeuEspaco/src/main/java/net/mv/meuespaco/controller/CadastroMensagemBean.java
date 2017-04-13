package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.controller.filtro.EscopoMensagem;
import net.mv.meuespaco.controller.filtro.FiltroCliente;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.integracao.Message;
import net.mv.meuespaco.model.integracao.MessageLevel;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.StatusCliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.DashboardService;
import net.mv.meuespaco.service.MessageService;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.service.SemanaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller da Inclusão e alteração de mensagens.
 * 
 * @author sidronio
 * @created 28/03/2017
 */
@Named
@ViewScoped
public class CadastroMensagemBean extends CadastroSingle implements Serializable {

	private static final long serialVersionUID = 826547159806631261L;

	private Message message;
	
	@Inject
	private MessageService msgSrvc;
	
	@Inject
	private SemanaService semanaSrvc;
	
	@Inject
	private DashboardService dashSrvc;
	
	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	private RegiaoService regiaoSrvc;
	
	@Inject @Param
	private Long paramId;
	
	private List<Semana> semanas;
	private List<Regiao> regioes;
	
	private EscopoMensagem escopo;
	
	private boolean escopoSelecionado;
	
	@Override
	@PostConstruct
	public void init() 
	{
		if (null != this.paramId)
		{
			try 
			{
				message = this.msgSrvc.findByCodigo(paramId);
			} catch (IntegracaoException e) 
			{
				FacesUtil.addErrorMessage("Não foi possível localizar a mensagem. " + e.getMessage());
			}
			
		} else 
		{
			message = new Message();
			semanas = semanaSrvc.buscaTodas();
			escopo = new EscopoMensagem();
			escopoSelecionado = false;
		}
	}

	@Override
	void novoRegistro() 
	{
		message = new Message();
	}
	
	/**
	 * Define o escopo e libera ou não a mensagem.
	 */
	public void defineEscopo()
	{
		try 
		{
			escopo.valida();
			FacesUtil.addSuccessMessage("Escopo definido.");
			
		} catch (RegraDeNegocioException e) 
		{
			FacesUtil.addErrorMessage(e.getMessage());
		}
	}

	@Override
	public void salvar() 
	{
		
		if (null == escopo)
		{
			FacesUtil.addErrorMessage("Não foi possível definir o escopo.");
			return;
		}
		
		List<Cliente> usuarios = this.criaListaDeUsuarios(escopo);
		List<Message> messages = this.criaMessages(usuarios);
		
		int qtdCriada = 0;
		
		try 
		{
			qtdCriada = this.msgSrvc.createMessages(messages);
			FacesUtil.addSuccessMessage(this.getMensagemDeInclusao(String.valueOf(qtdCriada)));
			
		} catch (IntegracaoException e) 
		{
			FacesUtil.addErrorMessage("Não foi possível gerar as mensagens. " + e.getMessage());
		}
	}
	
	/**
	 * Set a Região ao escopo de acordo com o código digitado.
	 * 
	 * @param value
	 */
	public void selecionaRegiaoListener(ValueChangeEvent value)
	{
		if (null != value)
		{
			escopo.setRegiao(regiaoSrvc.buscaPeloCodigoInterno(value.getNewValue().toString()));
		}
	}
	
	public void selecionaClienteListener(ValueChangeEvent value)
	{
		if (null != value)
		{
			escopo.setCliente(clienteSrvc.buscaPeloCodigoSiga(value.getNewValue().toString()));
		}
	}

	/**
	 * Cria a mensagem para cada cliente (usuário) gerado pelo escopo.
	 * 
	 * @param usuarios clientes gerados pelo escopo.
	 * @return lista de mensagem de cada usuário.
	 */
	private List<Message> criaMessages(List<Cliente> usuarios) 
	{
		List<Message> messages = new ArrayList<Message>();
		
		usuarios
			.parallelStream()
			.map(Cliente::getCodigoSiga)
			.forEach(usuario -> messages.add(new Message(this.message.getMessage(), usuario, this.message.getNivel())));

		return messages;
	}

	/**
	 * De acordo com o escopo cria a lista de Clientes (Usuários) 
	 * para geração das mensagens.
	 * 
	 * @param escopo para criação da mensagem.
	 * @return lista de clientes.
	 */
	private List<Cliente> criaListaDeUsuarios(EscopoMensagem escopo) 
	{
		if (escopo.isUsuariosLogados())
		{
			return dashSrvc.getClientesLogados();
		}
		
		return this.clienteSrvc.filtraPeloModoEspecifico(
				new FiltroCliente(
						(null == escopo.getCliente() ? null : escopo.getCliente().getCodigoSiga()),
						escopo.getRegiao(),
						escopo.getSemana(),
						StatusCliente.ATIVO));
	}

	@Override
	String getMensagemDeInclusao(String registro) 
	{
		return String.format("Foram criadas %s mensagens para o espoco definido.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) 
	{
		return String.format("A mensagem do cliente %s foi alterada.", registro);
	}

	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}

	public MessageLevel[] getNiveis() 
	{
		return MessageLevel.values();
	}

	public List<Semana> getSemanas() {
		return semanas;
	}
	
	public List<Regiao> getRegioes() {
		return regioes;
	}
	
	public EscopoMensagem getEscopo() {
		return escopo;
	}
	public void setEscopo(EscopoMensagem escopo) {
		this.escopo = escopo;
	}
	
	public boolean isEscopoSelecionado() 
	{
		return escopoSelecionado;
	}
	
}
