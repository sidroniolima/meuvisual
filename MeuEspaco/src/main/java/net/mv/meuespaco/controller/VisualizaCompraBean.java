package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.cdi.Param;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.VendaService;

/**
 * Bean para visualização dos detalhes de uma Venda (compra) 
 * e seus itens. 
 * 
 * @author Sidronio
 * @created 30/08/2016
 */
@Named
@ViewScoped
public class VisualizaCompraBean implements Serializable{

	private static final long serialVersionUID = 3758763087560229820L;
	
	private final Logger log = Logger.getLogger(VisualizaCompraBean.class.getName());

	@Inject @Param
	private Long paramCodigo;
	
	@Inject
	private VendaService vendaSrvc;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	private Venda venda;
	
	@PostConstruct
	public void init() {
		
		if (null != paramCodigo)
		{
			venda = vendaSrvc.buscaCompletaPeloCodigo(paramCodigo);
			venda.setItens(vendaSrvc.buscaItensCompleto(paramCodigo));
			
			if (null != venda)
			{
				if (!this.isVendaDoCliente())
				{
					this.negaAcesso();
				}
			}
			
		}
		
	}

	/**
	 * Verifica se o cliente solicitou uma venda dele.
	 * Caso não, é negado o acesso e redirecionado para a
	 * página 403.
	 * 
	 * @return
	 */
	private boolean isVendaDoCliente() {
		return venda.getCliente().equals(clienteLogado);
	}

	/**
	 * Redireciona para a página 403 de venda.
	 */
	private void negaAcesso() 
	{
		try 
		{
			FacesContext.getCurrentInstance().getExternalContext().redirect("/private/site/venda/403.xhtml");
		} catch (IOException e) {
			log.error(
					String.format("Não foi possível negar o acesso para a venda %s para o cliente %s",
							paramCodigo,
							clienteLogado.getCodigo())
					);
		}		
	}

	public Venda getVenda() {
		return venda;
	}
}
