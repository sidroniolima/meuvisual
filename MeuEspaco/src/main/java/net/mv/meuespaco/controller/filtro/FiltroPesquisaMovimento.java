package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;
import java.util.Date;

import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.estoque.TipoMovimento;
import net.mv.meuespaco.util.UtilDateTimeConverter;

/**
 * Filtra o movimento pelo Tipo, Origem, Usuário, Produto e 
 * período.
 * 
 * @author Sidronio
 * @created 06/07/2016
 */
public class FiltroPesquisaMovimento implements FiltroDePesquisa {

	private TipoMovimento tipo;
	private OrigemMovimento origem;
	private String nomeUsuario;
	private String codigoInterno;
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	
	/**
	 * Se o período é apenas uma data.
	 * 
	 * @return se simples ou não.
	 */
	public boolean isPeriodoSimples() {
		return this.dataFinal == null && this.dataInicial != null;
	}
	
	/**
	 * Define se o perído é entre duas datas.
	 * 
	 * @return composto ou não.
	 */
	public boolean isPeriodoComposto() {
		return this.dataFinal != null && this.dataInicial != null;
	}
	
	/**
	 * Converte a data inicial para o formato Date.
	 * 
	 * @return
	 */
	public Date getDataInicialComoDate()
	{
		return UtilDateTimeConverter.toDate(this.dataInicial);
	}
	
	/**
	 * Converte a data final para o formato Date.
	 * 
	 * @return
	 */
	public Date getDataFinalComoDate()
	{
		return UtilDateTimeConverter.toDate(this.dataFinal);
	}
	
	public TipoMovimento getTipo() {
		return tipo;
	}
	public void setTipo(TipoMovimento tipo) {
		this.tipo = tipo;
	}
	
	public OrigemMovimento getOrigem() {
		return origem;
	}
	public void setOrigem(OrigemMovimento origem) {
		this.origem = origem;
	}
	
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getCodigoInterno() {
		return codigoInterno;
	}
	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
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
