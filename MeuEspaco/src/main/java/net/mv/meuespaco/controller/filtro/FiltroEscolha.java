package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;

import net.mv.meuespaco.model.loja.StatusEscolha;

/**
 * Classe para filtro da Entidade Escolha na Pesquisa.
 * 
 * @author Sidronio
 * 13/01/2016
 */
public class FiltroEscolha {

	private Long codigo;
	private StatusEscolha status;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	private String codigoCliente;
	private String codigoRegiao;
	
	
	
	/**
	 * Construtor padrão.
	 */
	public FiltroEscolha() {	}

	/**
	 * Construtor com os campos da Classe.
	 * 
	 * @param codigo
	 * @param status
	 * @param dataInicial
	 * @param dataFinal
	 * @param cliente
	 */
	public FiltroEscolha(
			Long codigo, 
			StatusEscolha status, 
			LocalDate dataInicial, 
			LocalDate dataFinal,
			String codigoCliente,
			String codigoRegiao) {
		
		this.codigo = codigo;
		this.status = status;
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		this.codigoCliente = codigoCliente;
		this.codigoRegiao = codigoRegiao;
	}

	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the status
	 */
	public StatusEscolha getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusEscolha status) {
		this.status = status;
	}

	/**
	 * @return the dataInicial
	 */
	public LocalDate getDataInicial() {
		return dataInicial;
	}
	/**
	 * @param dataInicial the dataInicial to set
	 */
	public void setDataInicial(LocalDate dataInicial) {
		this.dataInicial = dataInicial;
	}

	/**
	 * @return the dataFinal
	 */
	public LocalDate getDataFinal() {
		return dataFinal;
	}
	/**
	 * @param dataFinal the dataFinal to set
	 */
	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}

	/**
	 * @return the codigoCliente
	 */
	public String getCodigoCliente() {
		return codigoCliente;
	}
	/**
	 * @param codigoCliente the codigoCliente to set
	 */
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}

	/**
	 * @return the codigoRegiao
	 */
	public String getCodigoRegiao() {
		return codigoRegiao;
	}
	/**
	 * @param codigoRegiao the codigoRegiao to set
	 */
	public void setCodigoRegiao(String codigoRegiao) {
		this.codigoRegiao = codigoRegiao;
	}

	/**
	 * Verifica se o filtro está preenchido.
	 * @return True se preenchido.
	 */
	public boolean isPreenchido() {
		
		if (null != codigo
				|| null != dataInicial
				|| null != dataFinal
				|| null != status
				|| (null != codigoCliente && !codigoCliente.isEmpty())
				|| (null != codigoRegiao && !codigoRegiao.isEmpty()) ) {
			
			return true;
		}
			
		
		return false;
	}

	/**
	 * Verifica se o filtro considera o status.
	 * @return
	 */
	public boolean forPeloStatus() {
		return null != status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FiltroEscolha [codigo=" + codigo + ", status=" + status + ", dataInicial=" + dataInicial
				+ ", dataFinal=" + dataFinal + ", cliente=" + codigoCliente + ", regiao= " + codigoRegiao + "]";
	}

}
