package net.mv.meuespaco.controller.filtro;

import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.loja.Cpf;
import net.mv.meuespaco.model.loja.StatusCliente;

/**
 * Filtro de Cliente pelos atributos codigoSiga, 
 * Nome e Cpf.
 * 
 * @author Sidronio
 * 08/03/2016
 */
public class FiltroCliente {
	
	private String codigoSiga;
	private String nome;
	private Cpf cpf;
	private Regiao regiao;
	private Semana semana;
	private StatusCliente status;
	
	public FiltroCliente() {	}
	
	public FiltroCliente(String codigoSiga, String nome, Cpf cpf, Regiao regiao, Semana semana) 
	{
		this.codigoSiga = codigoSiga;
		this.nome = nome;
		this.cpf = cpf;
		this.regiao = regiao;
		this.semana = semana;
	}

	public FiltroCliente(String codigoSiga, Regiao regiao, Semana semana) 
	{
		this.codigoSiga = codigoSiga;
		this.regiao = regiao;
		this.semana = semana;
	}
	
	public FiltroCliente(String codigoSiga, Regiao regiao, Semana semana, StatusCliente status) 
	{
		this.codigoSiga = codigoSiga;
		this.regiao = regiao;
		this.semana = semana;
		this.status = status;
	}

	/**
	 * Verifica se o filtro tem algum campo preenchido.
	 * 
	 * @return
	 */
	public boolean isPreenchido() 
	{
		return (null != codigoSiga && !codigoSiga.isEmpty()) 
				|| (null != nome && !nome.isEmpty()) 
				|| (null != cpf)
				|| (null != regiao)
				|| (null != semana)
				|| (null != status);
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
	
	public Cpf getCpf() {
		return cpf;
	}
	public void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}

	public Regiao getRegiao() {
		return regiao;
	}
	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}

	public Semana getSemana() {
		return semana;
	}
	public void setSemana(Semana semana) {
		this.semana = semana;
	}

	public StatusCliente getStatus() {
		return status;
	}
	public void setStatus(StatusCliente status) {
		this.status = status;
	}
}