package net.mv.meuespaco.controller.filtro;

import net.mv.meuespaco.model.loja.Cpf;

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
	
	/**
	 * Verifica se o filtro tem algum campo preenchido.
	 * 
	 * @return
	 */
	public boolean isPreenchido() {
		return (null != codigoSiga && !codigoSiga.isEmpty()) || (null != nome && !nome.isEmpty()) || (null != cpf);
	}
	
	/**
	 * @return the codigoSiga
	 */
	public String getCodigoSiga() {
		return codigoSiga;
	}
	/**
	 * @param codigoSiga the codigoSiga to set
	 */
	public void setCodigoSiga(String codigoSiga) {
		this.codigoSiga = codigoSiga;
	}
	
	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * @return the cpf
	 */
	public Cpf getCpf() {
		return cpf;
	}
	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(Cpf cpf) {
		this.cpf = cpf;
	}

	
}