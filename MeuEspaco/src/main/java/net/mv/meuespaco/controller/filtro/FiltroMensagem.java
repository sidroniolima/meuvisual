package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;

import net.mv.meuespaco.model.Semana;

/**
 * Filtro utilizado na pesquisa de Mensagens para listagem.
 * 
 * @author sidronio
 * @created 11/04/2017
 */
public class FiltroMensagem 
{
	private String codigoRegiao;
	private String codigoCliente;
	
	private Semana semana;
	
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	
	public FiltroMensagem() {	}
	
	public FiltroMensagem(String codigoRegiao, String codigoCliente, Semana semana, LocalDate dataInicial,
			LocalDate dataFinal) 
	{
		this.codigoRegiao = codigoRegiao;
		this.codigoCliente = codigoCliente;
		this.semana = semana;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
	}
	
	/**
	 * Ao menos um campo é considerado preenchido. 
	 * 
	 * @return preenchido ou não.
	 */
	public boolean isPreenchido() 
	{
		return this.codigoCliente.isEmpty() || 
				this.codigoRegiao.isEmpty() ||
				null == this.semana ||
				null == this.dataInicial ||
				null == this.dataFinal;
	}

	public String getCodigoRegiao() {
		return codigoRegiao;
	}
	public void setCodigoRegiao(String codigoRegiao) {
		this.codigoRegiao = codigoRegiao;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public Semana getSemana() {
		return semana;
	}
	public void setSemana(Semana semana) {
		this.semana = semana;
	}
	public LocalDate getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(LocalDate dataInicial) {
		this.dataInicial = dataInicial;
	}
	public LocalDate getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}

}
