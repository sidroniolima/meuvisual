package net.mv.meuespaco.relatorio;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Tamanho;

/**
 * Relatório com as informações do Cliente, Região, Escolha e seus itens.
 * 
 * @author Sidronio
 * 29/03/2016
 */
public class RelatorioEscolha {

	private BigInteger codigo;
	private String codigoCliente;
	private String nomeCliente;
	private String codigoRegiao;
	private String descricaoRegiao;
	
	private Date data;
	
	private String codigoProduto;
	private String descricao;
	private String localizacao;
	
	private String cor;
	private String tamanho;
	private String letra;
	
	private BigDecimal qtd;
	private BigDecimal qtdAtendido;
	
	/**
	 * Retorna a Cor com descrição correta.
	 * 
	 * @return
	 */
	public String getCorFormatada() {
		if (null == this.cor || this.cor.isEmpty()) {
			return "";
		}
		
		return Cor.valueOf(this.cor).getDescricao();
	}
	
	/**
	 * Retorna o tamanho formatado.
	 * 
	 * @return
	 */
	public String getTamanhoFormatado() {
		
		if (null == this.tamanho || this.tamanho.isEmpty()) {
			return "";
		}
		
		return Tamanho.valueOf(this.tamanho).getDescricao();
	}
	
	/**
	 * @return the codigo
	 */
	public BigInteger getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(BigInteger codigo) {
		this.codigo = codigo;
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
	 * @return the nomeCliente
	 */
	public String getNomeCliente() {
		return nomeCliente;
	}
	/**
	 * @param nomeCliente the nomeCliente to set
	 */
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
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
	 * @return the descricaoRegiao
	 */
	public String getDescricaoRegiao() {
		return descricaoRegiao;
	}
	/**
	 * @param descricaoRegiao the descricaoRegiao to set
	 */
	public void setDescricaoRegiao(String descricaoRegiao) {
		this.descricaoRegiao = descricaoRegiao;
	}
	
	/**
	 * @return the data
	 */
	public Date getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Date data) {
		this.data = data;
	}
	
	/**
	 * @return the codigoProduto
	 */
	public String getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/**
	 * @return the localizacao
	 */
	public String getLocalizacao() {
		return localizacao;
	}
	/**
	 * @param localizacao the localizacao to set
	 */
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	/**
	 * @return the cor
	 */
	public String getCor() {
		return cor;
	}
	/**
	 * @param cor the cor to set
	 */
	public void setCor(String cor) {
		this.cor = cor;
	}
	
	/**
	 * @return the tamanho
	 */
	public String getTamanho() {
		return tamanho;
	}
	/**
	 * @param tamanho the tamanho to set
	 */
	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}
	
	/**
	 * @return the letra
	 */
	public String getLetra() {
		return letra;
	}
	/**
	 * @param letra the letra to set
	 */
	public void setLetra(String letra) {
		this.letra = letra;
	}
	
	/**
	 * @return the qtd
	 */
	public BigDecimal getQtd() {
		return qtd;
	}
	/**
	 * @param qtd the qtd to set
	 */
	public void setQtd(BigDecimal qtd) {
		this.qtd = qtd;
	}
	
	/**
	 * @return the qtdAtendido
	 */
	public BigDecimal getQtdAtendido() {
		return qtdAtendido;
	}
	/**
	 * @param qtdAtendido the qtdAtendido to set
	 */
	public void setQtdAtendido(BigDecimal qtdAtendido) {
		this.qtdAtendido = qtdAtendido;
	}
	
}
