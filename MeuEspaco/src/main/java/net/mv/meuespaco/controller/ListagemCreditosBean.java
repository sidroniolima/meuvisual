package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.integracao.ListagemCreditos;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.CreditoService;

/**
 * Controller da listagem de créditos do cliente dos meses.
 * 
 * @author sidronio
 * @created 02/01/2017
 */
@Named
@ViewScoped
public class ListagemCreditosBean implements Serializable {

	private static final long serialVersionUID = 902226128387000515L;

	private final String [] meses = {"Outubro", "Novembro", "Dezembo", "Janeiro", "Fevereiro", "Março"};
	
	@Inject
	private CreditoService creditoSrvc;
	
	private ListagemCreditos listagem;
	
	@ClienteLogado
	private Cliente clienteLogado;
	
	@PostConstruct
	public void init()
	{
		listagem = creditoSrvc.listagemDeCreditoDoClientePorPeriodo(
						clienteLogado, LocalDate.of(2016, 8, 01), LocalDate.of(2016, 8, 30));
	}

	public String[] getMeses() {
		return meses;
	}

	public ListagemCreditos getListagem() {
		return listagem;
	}
	
}
