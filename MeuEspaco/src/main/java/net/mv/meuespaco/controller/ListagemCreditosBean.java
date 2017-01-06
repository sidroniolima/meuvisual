package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

	private final List<String> meses = Arrays.asList("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", 
			"Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");
	
	@Inject
	private CreditoService creditoSrvc;
	
	@ClienteLogado
	@Inject
	private Cliente clienteLogado;

	private List<String> mesesVisiveis;
	
	private String mesSelecionado;
	private int mesAtual;
	private int anoAtual;
	
	@PostConstruct
	public void init()
	{
		this.mesAtual = LocalDate.now().getMonthValue();
		this.anoAtual = LocalDate.now().getYear();
		
		mesSelecionado = meses.get(mesAtual-1);
		
		mesesVisiveis = meses
				.stream()
				.skip(mesAtual >=6 ? mesAtual-6 : 0)
				.limit(mesAtual < 6 ? mesAtual : 6)
				.collect(Collectors.toList());
		
		mesesVisiveis.addAll(
				0,
				meses
					.stream()
					.skip(mesesVisiveis.size() + 6)
					.collect(Collectors.toList())
				);
		
		this.listagem();
	}
	
	/**
	 * Retorna a listagem de Creditos para o período,
	 * do cliente logado.
	 * 
	 * @return
	 */
	public ListagemCreditos listagem()
	{
		if (null == mesSelecionado | mesSelecionado.isEmpty())
		{
			return null;
		}
		
		int ano = anoAtual;
		int mes = meses.indexOf(mesSelecionado);
		
		if (++mes > mesAtual)
		{
			ano = this.anoAtual - 1;
		}
			
		LocalDate primeiroDia = LocalDate.of(ano, mes, 01);
		LocalDate ultimoDia = primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());
		
		System.out.println(primeiroDia + ", " + ultimoDia);
		
		ListagemCreditos listCred = creditoSrvc.listagemDeCreditoDoClientePorPeriodo(clienteLogado, primeiroDia, ultimoDia);
		listCred.getCreditos().stream().peek(c -> System.out.println(c.getNome()));
		
		return listCred;
	}

	public List<String> getMeses() {
		return meses;
	}

	public List<String> getMesesVisiveis() {
		return mesesVisiveis;
	}

	public String getMesSelecionado() {
		return mesSelecionado;
	}
	public void setMesSelecionado(String mesSelecionado) {
		System.out.println(mesSelecionado);
		this.mesSelecionado = mesSelecionado;
		this.listagem();
	}
	
}
