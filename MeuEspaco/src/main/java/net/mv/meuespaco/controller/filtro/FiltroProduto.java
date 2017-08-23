package net.mv.meuespaco.controller.filtro;

import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Subgrupo;

public class FiltroProduto implements IFiltro
{
	private String descricao;
	private Subgrupo subgrupo;
	private Composicao composicao;
	
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
	
	@Override
	public boolean isPreenchido() 
	{
		return  ((null != descricao || (null != descricao && this.descricao.isEmpty())) || null != this.subgrupo || null != this.composicao);
	}
	
	@Override
	public String toString() {
		return "FiltroProduto [descricao=" + descricao + ", subgrupo=" + subgrupo.getCodigo() + ", composicao=" + composicao + "]";
	}
}
