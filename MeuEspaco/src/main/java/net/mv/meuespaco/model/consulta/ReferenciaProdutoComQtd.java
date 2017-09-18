package net.mv.meuespaco.model.consulta;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReferenciaProdutoComQtd 
{
	private Timestamp horario;
	private String codigoInterno;
	private String descricao;
	private String grupo;
	private BigDecimal qtd;
	
	public Timestamp getHorario() {
		return horario;
	}
	public void setHorario(Timestamp horario) {
		this.horario = horario;
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
	public BigDecimal getQtd() {
		return qtd;
	}
	public void setQtd(BigDecimal qtd) {
		this.qtd = qtd;
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
}
