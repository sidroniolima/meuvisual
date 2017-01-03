package net.mv.meuespaco.integracao;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import net.mv.meuespaco.converter.LocalDateDBConverter;

/**
 * Cŕedito de comissão de acordo com os dados do ERP.
 * 
 * @author sidronio
 * @creted 02/01/2017
 */
public class Credito {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(name="codigo_siga")
	@ManyToOne(fetch=FetchType.LAZY)
	private String codigoSiga;
	
	private String nome;
	private double valor;
	private ClasseCredito classe;
	private String historio;
	
	@Convert(converter=LocalDateDBConverter.class)
	@Column(columnDefinition="DATE")
	private LocalDate baixa;
	
	public Credito() {	}
	
	public Credito(Long codigo, String codigoSiga, String nome, double valor, String classe, String historio,
			LocalDate baixa) 
	{
		this.codigo = codigo;
		this.codigoSiga = codigoSiga;
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
		return classe.isSoma() && this.historio.isEmpty();
	}
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigoSiga() {
		return codigoSiga;
	}
	public void setCodigoSiga(String codigoSiga) {
		this.codigoSiga = codigoSiga;
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
	
}
