package net.mv.meuespaco.model.consulta;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

import net.mv.meuespaco.model.Composicao;

public class MovimentoPorComposicaoSubgrupo 
{
	private Date data;
	private String subgrupo;
	private String composicao;
	private String caracteristica;
	private BigDecimal qtd;
	private BigInteger qtdItens;
	
	public MovimentoPorComposicaoSubgrupo() {	}
	
	public MovimentoPorComposicaoSubgrupo(String subgrupo, String composicao, BigDecimal qtd) 
	{
		this.subgrupo = subgrupo;
		this.composicao = composicao;
		this.qtd = qtd;
	}
	
	public String descricaoComposicao()
	{
		return Composicao.valueOf(this.composicao).getDescricao();
	}

	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

	public String getSubgrupo() {
		return subgrupo;
	}
	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
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

	public BigDecimal getQtd() {
		return qtd;
	}
	public void setQtd(BigDecimal qtd) {
		this.qtd = qtd;
	}

	public BigInteger getQtdItens() {
		return qtdItens;
	}
	public void setQtdItens(BigInteger qtdItens) {
		this.qtdItens = qtdItens;
	}
}