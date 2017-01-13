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
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.integracao.ListagemCreditos;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.CreditoService;
import net.mv.meuespaco.util.DataDoSistema;
import net.mv.meuespaco.util.FacesUtil;

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
	
	protected int mesAtual;
	protected int anoAtual;
	
	private DataDoSistema relogio;
	
	public ListagemCreditosBean() {	
		this.relogio = new DataDoSistema();
	}
	
	public ListagemCreditosBean(CreditoService creditoSrvc, Cliente clienteLogado, DataDoSistema relogio) 
	{
		this.creditoSrvc = creditoSrvc;
		this.clienteLogado = clienteLogado;
		this.relogio = relogio;
	}

	@PostConstruct
	public void init()
	{
		this.mesAtual = relogio.hoje().getMonthValue();
		this.anoAtual = relogio.hoje().getYear();
		
		mesSelecionado = meses.get(mesAtual-1);
		
		mesesVisiveis = meses
				.stream()
				.skip(mesAtual >=6 ? mesAtual-6 : 0)
				.limit(mesAtual < 6 ? mesAtual : 6)
				.collect(Collectors.toList());
		
		if (mesesVisiveis.size() < 6)
		{
			mesesVisiveis.addAll(
					0,
					meses
						.stream()
						.skip(mesesVisiveis.size() + 6)
						.collect(Collectors.toList())
					);
		}
		
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
		LocalDate primeiroDia;
		
		try 
		{
			primeiroDia = primeiroDiaDoMesSelecionado();
			
			LocalDate ultimoDia = ultimoDiaDoMesSelecionado(primeiroDia);
			
			ListagemCreditos listCred = creditoSrvc.listagemDeCreditoDoClientePorPeriodo(clienteLogado, primeiroDia, ultimoDia);
			
			return listCred;
			
		} catch (RegraDeNegocioException e) 
		{
			FacesUtil.addErrorMessage(e.getMessage());
		}
		
		return new ListagemCreditos();
	}

	/**
	 * Calcula o último dia do mês selecionado, baseado na 
	 * criação do primeiro.
	 * 
	 * @param primeiroDia
	 * @return último dia do mês.
	 */
	protected LocalDate ultimoDiaDoMesSelecionado(LocalDate primeiroDia) {
		return primeiroDia.withDayOfMonth(primeiroDia.lengthOfMonth());
	}

	/**
	 * Calcula o primeiro dia do mês selecionado.
	 * 
	 * @return dia 01 do mês selecionado.
	 * @throws RegraDeNegocioException 
	 */
	protected LocalDate primeiroDiaDoMesSelecionado() throws RegraDeNegocioException 
	{
		if (null == mesSelecionado || mesSelecionado.isEmpty())
		{
			throw new RegraDeNegocioException("Não é possível listar os créditos pois nenhum mês "
					+ "foi selecionado. Selecione o mês e tente novamente.");
		}
		
		int ano = anoAtual;
		int mes = meses.indexOf(mesSelecionado);
		
		if (++mes > mesAtual)
		{
			ano = this.anoAtual - 1;
		}
			
		return LocalDate.of(ano, mes, 01);
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
		this.mesSelecionado = mesSelecionado;
		this.listagem();
	}
	
}
