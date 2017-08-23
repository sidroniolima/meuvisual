package net.mv.meuespaco.controller;

import java.math.BigInteger;

import net.mv.meuespaco.model.Caracteristica;
import net.mv.meuespaco.model.Composicao;

public class ProdutosEQtdPorSubgrupo 
{
	private String grupoDescricao;
	private String subgrupoDescricao;
	private String departamentoDescricao;
	private String composicao;
	private String caracteristica;
	private BigInteger qtd;
	
	public String leDescricaoComposicao()
	{
		return Composicao.valueOf(this.composicao).getDescricao();
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
	public String getDepartamentoDescricao() {
		return departamentoDescricao;
	}
	public void setDepartamentoDescricao(String departamentoDescricao) {
		this.departamentoDescricao = departamentoDescricao;
	}
	public BigInteger getQtd() {
		return qtd;
	}
	public void setQtd(BigInteger qtd) {
		this.qtd = qtd;
	}
	public String getComposicao() {
		return composicao;
	}
	public void setComposicao(String composicao) {
		this.composicao = composicao;
	}
	public String getCaracteristica() {
		return caracteristica;
	}
	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}
}
