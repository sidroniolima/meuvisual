package net.mv.meuespaco.model.consulta;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Tamanho;

/**
 * Encapsulamento das informações de quantidade do produto 
 * por grade e almoxarifado.
 * 
 * @author Sidronio
 * 12/01/2016
 */
public class EstoqueDoProdutoConsulta {

	private BigInteger codigoProduto;
	private String almoxarifado;
	private String tipoGrade;
	private String cor;
	private String tamanho;
	private String letra;
	private String simbolo;
	private BigDecimal qtd;
	
	public EstoqueDoProdutoConsulta() {	}
	
	public EstoqueDoProdutoConsulta(
			BigInteger codigoProduto,
			String almoxarifado, 
			String tipoGrade, 
			String cor, 
			String tamanho, 
			String letra,
			String simbolo,
			BigDecimal qtd) {

		this.codigoProduto = codigoProduto;
		this.almoxarifado = almoxarifado;
		this.tipoGrade = tipoGrade;
		this.cor = cor;
		this.tamanho = tamanho;
		this.letra = letra;
		this.simbolo = simbolo;
		this.qtd = qtd;
	}

	/**
	 * @return the codigoProduto
	 */
	public BigInteger getCodigoProduto() {
		return codigoProduto;
	}
	/**
	 * @param codigoProduto the codigoProduto to set
	 */
	public void setCodigoProduto(BigInteger codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	/**
	 * @return the almoxarifado
	 */
	public String getAlmoxarifado() {
		return almoxarifado;
	}
	/**
	 * @param almoxarifado the almoxarifado to set
	 */
	public void setAlmoxarifado(String almoxarifado) {
		this.almoxarifado = almoxarifado;
	}

	/**
	 * @return the tipoGrade
	 */
	public String getTipoGrade() {
		return TipoGrade.valueOf(tipoGrade).getDescricao();
	}
	/**
	 * @param tipoGrade the tipoGrade to set
	 */
	public void setTipoGrade(String tipoGrade) {
		this.tipoGrade = tipoGrade;
	}
	
	/**
	 * @return the cor
	 */
	public String getCor() {
		
		if (null == cor || cor.isEmpty()) {
			return "";
		}
		
		return Cor.valueOf(cor).getDescricao();
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
		
		if (null == tamanho || tamanho.isEmpty()) {
			return "";
		}		
		
		return Tamanho.valueOf(tamanho).getDescricao();
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
	
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
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

}
