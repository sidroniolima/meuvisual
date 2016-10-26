package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Escolha;
import net.mv.meuespaco.model.loja.ItemEscolha;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class AtendeEscolhaBean implements Serializable {

	private static final long serialVersionUID = 8528041941088923796L;
	
	@Inject
	private EscolhaService escolhaSrvc;
	
	private Escolha escolha;
	
	private ItemEscolha itemSelecionado;
	
	@Inject @Param
	private Long paramCodigo;
	
	/**
	 * Busca a escolha pelo Código informado no parâmetro.
	 */
	@PostConstruct
	public void init() {
		
		if (null != paramCodigo) {
			
			this.escolha = escolhaSrvc.buscarParaAtendimento(paramCodigo);
			
			if (null == this.escolha || (null != this.escolha && !this.escolha.isPodeSerAtendida()))
			{
				FacesUtil.addErrorMessage("Não foi possível localizar a escolha pelo código solicitado.");
				return;
			}
		}
		
	}
	
	/**
	 * Atende a escolha do cliente.
	 */
	public String atendeEscolha() {
		
		try {
			
			escolha.atende();
			this.escolhaSrvc.salva(escolha);
			FacesUtil.addSuccessMessage("Escolha atendida com sucesso.");
			
			return "pesquisa-escolha";
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(
					String.format("Não foi possível atender a escolha. %s.", e.getMessage()));
		}
		
		return null;
		
	}
	
	/**
	 * Separa o item atual;
	 * 
	 * @param event
	 */
	public void separaItem() {
		
		if (null == itemSelecionado) {
			
			FacesUtil.addErrorMessage("Selecione um item válido para atender.");
		} else {
			itemSelecionado.atendeOuCancelaAtendimento();
		}
	}
	
	/**
	 * Atende todos os itens.
	 */
	public void atendeTodos() {
		
		if (null != escolha) {
			escolha.atendeTodosItens();
		}
		
	}
	
	/**
	 * Informa se pode atender ou não uma escolha.
	 * 
	 * @return se pode atender.
	 */
	public boolean isPodeAtender()
	{
		return null != escolha;
	}

	/**
	 * @return the escolha
	 */
	public Escolha getEscolha() {
		return escolha;
	}
	/**
	 * @param escolha the escolha to set
	 */
	public void setEscolha(Escolha escolha) {
		this.escolha = escolha;
	}

	/**
	 * @return the itemSelecionado
	 */
	public ItemEscolha getItemSelecionado() {
		return itemSelecionado;
	}
	/**
	 * @param itemSelecionado the itemSelecionado to set
	 */
	public void setItemSelecionado(ItemEscolha itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}
	
}
