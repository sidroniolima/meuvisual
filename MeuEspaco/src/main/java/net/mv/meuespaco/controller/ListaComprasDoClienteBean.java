package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.controller.filtro.FiltroDePesquisa;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Lista as vendas do cliente com opção para visualização das mesmas.
 * 
 * @author Sidronio
 * @created 30/08/2016
 */
@Named("minhasComprasBean")
@ViewScoped
public class ListaComprasDoClienteBean extends ListaSimples implements Serializable {

	private static final long serialVersionUID = 4862338844512490251L;
	
	@Inject
	@ClienteLogado
	private Cliente cliente;
	
	@Inject
	private VendaService vendaSrvc;
	
	private List<Venda> vendas;
	
	private Venda vendaSelecionada;
	
	@Override
	@PostConstruct
	public void init() {
		this.listarComPaginacao();
	}

	/**
	 * Cancela uma venda paga ou não. Se paga 
	 * apenas poderá ser cancelada no mesmo dia.
	 */
	public void cancelaVenda()
	{
		String message = "Sua compra foi cancelada com sucesso.";
		
		if (null == this.vendaSelecionada)
		{
			FacesUtil.addErrorMessage("Selecione a compra para cancelamento.");
			return;
		}
		
		try 
		{
			if (this.vendaSelecionada.isPaga())
			{
				message = "Sua compra foi cancelada e o valor não será cobrado em sua fatura. "
						+ "Caso haja dúvidas entre contato via chat ou telefone.";
			}
		
			this.vendaSrvc.cancelaVenda(vendaSelecionada);
			FacesUtil.addSuccessMessage(message);
				
		} catch (RegraDeNegocioException | IntegracaoException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		} catch (CieloException e) {
			FacesUtil.addErrorMessage("Não foi possível cancelar a venda. Entre em contato via chat ou telefone.");
		}

	}
	
	@Override
	public void listarComPaginacao() {
		this.vendas = vendaSrvc.vendasDoCliente(cliente);
	}

	public List<Venda> getVendas() {
		return vendas;
	}

	public Venda getVendaSelecionada() {
		return vendaSelecionada;
	}

	public void setVendaSelecionada(Venda vendaSelecionada) {
		this.vendaSelecionada = vendaSelecionada;
	}
	
	@Override
	public FiltroDePesquisa getFiltro() 
	{
		return null;
	}
}

