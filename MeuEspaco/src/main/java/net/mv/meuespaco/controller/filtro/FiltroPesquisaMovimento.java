package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;
import java.time.LocalDateTime;

import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.estoque.TipoMovimento;

/**
 * Filtra o movimento pelo Tipo, Origem, Usuário, Produto e 
 * período.
 * 
 * @author Sidronio
 * @created 06/07/2016
 */
public class FiltroPesquisaMovimento implements IFiltro, FiltroDePesquisa 
{
	private TipoMovimento tipo;
	private OrigemMovimento origem;
	private String nomeUsuario;
	private String codigoInterno;
	private LocalDateTime dataHoraInicial;
	private LocalDateTime dataHoraFinal;
	
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	
	public FiltroPesquisaMovimento() 
	{
		this.tipo = TipoMovimento.ENTRADA;
		this.origem = OrigemMovimento.AJUSTE;
	}
	
	/**
	 * Se o período é apenas uma data.
	 * 
	 * @return se simples ou não.
	 */
	public boolean isPeriodoSimples() {
		return (this.dataFinal == null && this.dataInicial != null)
				|| (this.dataHoraFinal== null && this.dataHoraInicial != null);
	}
	
	/**
	 * Define se o perído é entre duas datas.
	 * 
	 * @return composto ou não.
	 */
	public boolean isPeriodoComposto() {
		return (this.dataFinal != null && this.dataInicial != null)
				|| (this.dataHoraInicial != null && this.dataHoraFinal != null);
	}
	
	@Override
	public boolean isPreenchido() 
	{
		return null != this.tipo &&
				null != this.origem &&
				(this.isPeriodoSimples() || this.isPeriodoComposto());
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

	public LocalDateTime getDataHoraInicial() {
		return dataHoraInicial;
	}
	public void setDataHoraInicial(LocalDateTime dataHoraInicial) {
		this.dataHoraInicial = dataHoraInicial;
	}

	public LocalDateTime getDataHoraFinal() {
		return dataHoraFinal;
	}
	public void setDataHoraFinal(LocalDateTime dataHoraFinal) {
		this.dataHoraFinal = dataHoraFinal;
	}
}
