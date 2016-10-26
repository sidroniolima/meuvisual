package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;

import net.mv.meuespaco.model.loja.StatusVenda;

/**
 * Filtro da pesquisa de vendas.
 * 
 * @author Sidronio
 * @created 01/09/2016
 */
public class FiltroPesquisaVenda {

	private Long codigo;
	private StatusVenda status;
	private String codigoCliente;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	
	/**
	 * Verifica se algum critério foi preenchido.
	 * 
	 * @return se preenchido ou não.
	 */
	public boolean isPreenchido()
	{
		return 
				null != this.codigo ||
				null != status ||
				(null != codigoCliente && !codigoCliente.isEmpty()) ||
				null != dataInicial ||
				null != dataFinal;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public StatusVenda getStatus() {
		return status;
	}
	public void setStatus(StatusVenda status) {
		this.status = status;
	}
	
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

	@Override
	public String toString() {
		return "FiltroPesquisaVenda [codigo=" + codigo + ", status=" + status + ", codigoCliente=" + codigoCliente
				+ ", dataInicial=" + dataInicial + ", dataFinal=" + dataFinal + "]";
	}
}
