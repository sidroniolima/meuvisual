package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;

import net.mv.meuespaco.model.loja.StatusEscolha;

/**
 * Implementação do filtro de resgate de brinde;
 * 
 * @author sidronio
 * @created 26/06/2017
 */
public class FiltroResgateBrinde implements IFiltroPesquisaAcao 
{
	private Long codigo;
	private String codigoCliente;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	private StatusEscolha status;
	
	/**
	 * Verifica se o filtro está preenchido.
	 * @return True se preenchido.
	 */
	public boolean isPreenchido() {
		
		if (null != codigo
				|| null != dataInicial
				|| null != dataFinal
				|| null != status
				|| (null != codigoCliente && !codigoCliente.isEmpty())) {
			
			return true;
		}
		
		return false;
	}	
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
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
	public StatusEscolha getStatus() {
		return status;
	}
	public void setStatus(StatusEscolha status) {
		this.status = status;
	}
}
