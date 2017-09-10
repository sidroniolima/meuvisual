package net.mv.meuespaco.controller.filtro;

import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Subgrupo;

public class FiltroProduto implements IFiltro
{
	private String codigoInterno;
	private String descricao;
	private Subgrupo subgrupo;
	private Composicao composicao;
	private String departamentoDescricao;
	private String grupoDescricao;
	private String subgrupoDescricao;
	private String caracteristica;
	private Finalidade finalidade;
	private Boolean ativo;
	
	public FiltroProduto() {	}
	
	public FiltroProduto(
			String grupoDescricao, 
			String subgrupoDescricao, 
			String departamentoDescricao, 
			Composicao composicao,
			String caracteristica,
			Finalidade finalidade,
			boolean ativo) 
	{
		this.grupoDescricao = grupoDescricao;
		this.subgrupoDescricao = subgrupoDescricao;
		this.departamentoDescricao = departamentoDescricao;
		this.composicao = composicao;
		this.caracteristica = caracteristica;
		this.finalidade = finalidade;
		this.ativo = ativo;
	}
	
	public String getCodigoInterno() {
		return codigoInterno;
	}
	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Subgrupo getSubgrupo() {
		return subgrupo;
	}
	public void setSubgrupo(Subgrupo subgrupo) {
		this.subgrupo = subgrupo;
	}
	public Composicao getComposicao() {
		return composicao;
	}
	public void setComposicao(Composicao composicao) {
		this.composicao = composicao;
	}
	public String getDepartamentoDescricao() {
		return departamentoDescricao;
	}
	public void setDepartamentoDescricao(String departamentoDescricao) {
		this.departamentoDescricao = departamentoDescricao;
	}
	public String getGrupoDescricao() {
		return grupoDescricao;
	}
	public void setGrupoDescricao(String grupoDescricao) {
		this.grupoDescricao = grupoDescricao;
	}
	public String getSubgrupoDescricao() {
		return subgrupoDescricao;
	}
	public void setSubgrupoDescricao(String subgrupoDescricao) {
		this.subgrupoDescricao = subgrupoDescricao;
	}
	public String getCaracteristica() {
		return caracteristica;
	}
	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}
	public Finalidade getFinalidade() {
		return finalidade;
	}
	public void setFinalidade(Finalidade finalidade) {
		this.finalidade = finalidade;
	}
	public Boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public boolean isPreenchido() 
	{
		return  ((null != descricao || (null != descricao && this.descricao.isEmpty())) || null != this.subgrupo || null != this.composicao);
	}

	@Override
	public String toString() {
		return "FiltroProduto [codigoInterno=" + codigoInterno + ", descricao=" + descricao + ", subgrupo=" + (subgrupo == null ? "" : subgrupo.getDescricao())
				+ ", composicao=" + composicao + ", departamento=" + departamentoDescricao + ", grupoDescricao=" + grupoDescricao
				+ ", subgrupoDescricao=" + subgrupoDescricao + ", caracteristica=" + caracteristica + "]";
	}
}
