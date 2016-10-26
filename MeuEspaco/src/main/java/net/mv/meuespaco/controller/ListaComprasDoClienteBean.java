package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.VendaService;

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
	
	@Override
	@PostConstruct
	public void init() {
		this.listarComPaginacao();
	}

	@Override
	public void listarComPaginacao() {
		this.vendas = vendaSrvc.vendasDoCliente(cliente);
	}

	public List<Venda> getVendas() {
		return vendas;
	}
}
