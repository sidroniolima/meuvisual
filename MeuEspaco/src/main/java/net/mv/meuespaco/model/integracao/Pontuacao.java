package net.mv.meuespaco.model.integracao;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.mv.meuespaco.converter.LocalDateDBConverter;

/**
 * Pontuação importada do ERP utilizada no resgate de brindes.
 * 
 * @author sidronio
 * @created 25/04/2017
 */
@Entity
@Table(name="pontuacao")
public class Pontuacao implements Serializable 
{
	private static final long serialVersionUID = 1760419419142631212L;

	@Id
	private Long codigo;
	
	@Column(columnDefinition="DATE")
	@Convert(converter=LocalDateDBConverter.class)
	private LocalDate data;
	
	@Column(name="cliente_codigo")
	private String codigoCliente;

	private int pontos;
	
	@Column(length=1)
	private String tipo;

	private String descricao;
	
	public Pontuacao() {	}
	
	public Pontuacao(Long codigo, LocalDate data, String codigoCliente, int pontos, String tipo, String descricao) 
	{
		this.codigo = codigo;
		this.data = data;
		this.codigoCliente = codigoCliente;
		this.pontos = pontos;
		this.tipo = tipo;
		this.descricao = descricao;
	}
	
	public Long getCodigo() 
	{
		return this.codigo;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public int getPontos() {
		return pontos;
	}
	public void setPontos(int pontos) {
		this.pontos = pontos;
	}
	public String getCodigoCliente() {
		return codigoCliente;
	}
	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pontuacao other = (Pontuacao) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	@Override
	public String toString() 
	{
		return "Pontuacao [codigo=" + codigo + ", data=" + data + ", codigoCliente=" + codigoCliente + ", pontos="
				+ pontos + ", tipo=" + tipo + ", descricao=" + descricao + "]";
	}
	
}
