package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;

/**
 * Filtro de Pontuação pelo cliente, data e tipo.
 * 
 * @author sidronio
 * @created 27/04/2017
 */
public class FiltroPontuacao implements FiltroDePesquisa
{
	private String codigoCliente;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	private String tipo;
	
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
