package net.mv.meuespaco.model.integracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.StatusCliente;

/**
 * Equipe do cliente.
 * 
 * @author sidronio
 * @created 06/02/16
 */
@Entity
@Table(name="equipe")
public class Equipe implements Serializable
{
	private static final long serialVersionUID = -442815671859966437L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cliente_codigo")
	private Cliente cliente;
	
	@Enumerated(EnumType.STRING)
	@Column(name="equipe_status")
	private StatusCliente statusEquipe;
	
	@Column(name="equipe_codigo")
	private String codigoEquipe;
	
	@Column(name="equipe_nome")
	private String nomeEquipe;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="regiao_codigo")
	private Regiao regiaoEquipe;
	
	public Equipe() {	}
	
	public Equipe(Cliente cliente, 
			StatusCliente statusEquipe,
			String codigoEquipe,
			String nomeEquipe,
			Regiao regiaoEquipe) 
	{
		this.cliente = cliente;
		this.statusEquipe = statusEquipe;
		this.nomeEquipe = nomeEquipe;
		this.codigoEquipe = codigoEquipe;
		this.regiaoEquipe = regiaoEquipe;
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public StatusCliente getStatusEquipe() {
		return statusEquipe;
	}
	public void setStatusEquipe(StatusCliente statusEquipe) {
		this.statusEquipe = statusEquipe;
	}
	
	public String getCodigoEquipe() {
		return codigoEquipe;
	}
	public void setCodigoEquipe(String codigoEquipe) {
		this.codigoEquipe = codigoEquipe;
	}

	public String getNomeEquipe() {
		return nomeEquipe;
	}
	public void setNomeEquipe(String nomeEquipe) {
		this.nomeEquipe = nomeEquipe;
	}

	public Regiao getRegiaoEquipe() {
		return regiaoEquipe;
	}
	public void setRegiaoEquipe(Regiao regiaoEquipe) {
		this.regiaoEquipe = regiaoEquipe;
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
		Equipe other = (Equipe) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
