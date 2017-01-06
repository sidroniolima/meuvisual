package net.mv.meuespaco.integracao;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import net.mv.meuespaco.converter.LocalDateDBConverter;
import net.mv.meuespaco.model.loja.Cliente;

/**
 * Cŕedito de comissão de acordo com os dados do ERP.
 * 
 * @author sidronio
 * @creted 02/01/2017
 */
@Entity
@Table(name="credito")
public class Credito {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@JoinColumn(name="cliente_codigo")
	@ManyToOne(fetch=FetchType.LAZY)
	private Cliente cliente;
	
	private String nome;
	private double valor;
	
	@Enumerated(EnumType.STRING)
	private ClasseCredito classe;
	private String historio;
	
	@Convert(converter=LocalDateDBConverter.class)
	@Column(columnDefinition="DATE")
	private LocalDate baixa;
	
	public Credito() {	}
	
	public Credito(Long codigo, Cliente cliente, String nome, double valor, String classe, String historio,
			LocalDate baixa) 
	{
		this.codigo = codigo;
		this.cliente = cliente;
		this.nome = nome;
		this.valor = valor;
		this.classe = ClasseCredito.valueOf(classe);
		this.historio = historio;
		this.baixa = baixa;
	}

	/**
	 * Verifica se é utilizado para soma ou subtração.
	 * 
	 * @return true ou false.
	 */
	public boolean isSoma()
	{
		return classe.isSoma() && this.isPago();
	}
	
	/**
	 * Verifica se foi pago.
	 * 
	 * @return
	 */
	public boolean isPago()
	{
		return this.getHistorio().isEmpty();
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public Cliente getCodigoSiga() {
		return cliente;
	}
	public void setCodigoSiga(Cliente codigoSiga) {
		this.cliente = codigoSiga;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	
	public ClasseCredito getClasse() {
		return classe;
	}
	public void setClasse(ClasseCredito classe) {
		this.classe = classe;
	}
	
	public String getHistorio() {
		return historio;
	}
	public void setHistorio(String historio) {
		this.historio = historio;
	}
	
	public LocalDate getBaixa() {
		return baixa;
	}
	public void setBaixa(LocalDate baixa) {
		this.baixa = baixa;
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
		Credito other = (Credito) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
