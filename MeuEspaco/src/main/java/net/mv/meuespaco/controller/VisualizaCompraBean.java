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
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.IntegracaoCieloService;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Bean para visualização dos detalhes de uma Venda (compra), seus itens, 
 * com fotos e informações do pagamento. 
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
	private IntegracaoCieloService cieloSrvc;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	private Pagamento pagamento;
	
	private Venda venda;
	
	@PostConstruct
	public void init() throws IOException {
		
		if (null != paramCodigo)
		{
			venda = vendaSrvc.buscaCompletaPeloCodigo(paramCodigo);
		
			if (null == venda)
			{
				FacesUtil.addErrorMessage("Não foi possível localizar a venda.");
				return;
			}
			
			venda.setItens(vendaSrvc.buscaItensCompleto(paramCodigo));
			
			if (!venda.getCliente().equals(clienteLogado))
			{
				FacesContext.getCurrentInstance().getExternalContext().redirect("/private/site/venda/403.xhtml");
			}
			
			if (venda.isPaga())
			{
				try 
				{
					pagamento = cieloSrvc.consultaPagamento(venda.getPaymentId());
				
				} catch (CieloException | IntegracaoException e) 
				{
					FacesUtil.addErrorMessage("Não foi possível recuperar as informações do pagamento. Tente novamente mais tarde.");
				}
			}
			
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

	public Pagamento getPagamento() {
		return pagamento;
	}
}
