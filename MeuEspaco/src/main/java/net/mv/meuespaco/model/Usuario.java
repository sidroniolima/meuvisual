package net.mv.meuespaco.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Vetoed;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import net.mv.meuespaco.exception.RegraDeNegocioException;

/**
 * Entidade para acesso ao site. O usuário pode estar 
 * associado a um cliente ou pode ser criado para acesso às 
 * funcionalidades administrativas do site. 
 * 
 * @author Sidronio
 * 05/01/2015
 */
@Entity
@Vetoed
@Table(name="usuario")
public class Usuario extends EntidadeModel implements Serializable {

	private static final long serialVersionUID = 8041055875247832587L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(length=50)
	private String login;
	
	@Column(length=200)
	private String senha;
	
	@Column(length=200)
	private String nome;
	
	@Column(length=200)
	private String email;
	
	@CollectionTable(name="usuario_permissoes", joinColumns = { @JoinColumn(name="usuario_codigo")})
	@ElementCollection(targetClass=Permissao.class, fetch=FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@Column(name="permissao_codigo")
	private List<Permissao> permissoes = new ArrayList<Permissao>();
	
	@Column(columnDefinition = "boolean default true")
	private boolean ativo;
	
	public Usuario() {	
		ativo = true;
	}
	
	public Usuario(String login, String senha) {
		this();
		this.login = login;
		this.senha = senha;
	}
	
	public Usuario(String login, String senha, String nome, String email) {
		this();
		this.login = login;
		this.nome = nome;
		this.senha = senha;
		this.email = email;
		
	}
	
	/**
	 * Adiciona permissão ao Usuário.
	 * 
	 * @param roleCliente
	 */
	public void adicionaPermissao(Permissao role) {
		if (!permissoes.contains(role)) {
			permissoes.add(role);
		}
	}

	public Usuario(long codigo) {
		this.codigo = codigo;
	}
	
	public Usuario(String login) {
		this.login = login;
	}

	public Usuario(long codigo, String login) 
	{
		this.codigo = codigo;
		this.login = login;
	}

	@Override
	public void valida() throws RegraDeNegocioException {
		
		if (null == this.nome || this.nome.isEmpty()) {
			throw new RegraDeNegocioException("O nome do usuário deve ser informado.");
		}	
		
		if (null == this.login || this.login.isEmpty()) {
			throw new RegraDeNegocioException("O usuário deve possuir um login.");
		}
		
		if (null == this.senha || this.senha.isEmpty()) {
			throw new RegraDeNegocioException("A senha do usuário deve ser informada.");
		}		
		
		if (!this.temPermissoes()) {
			throw new RegraDeNegocioException("Ao menos uma permissão deve ser concedida ao usuário.");
		}
 	}
	
	/**
	 * Verifica que o usuário tem Permissões.
	 * 
	 * @return
	 */
	public boolean temPermissoes() {
		return this.permissoes.size() > 0;
	}
	
	/**
	 * Verifica se um usuário é Cliente.
	 * 
	 * @return True se for cliente.
	 */
	public boolean isCliente() 
	{
		return this.permissoes.contains(Permissao.ROLE_CLIENTE);
	}
	
	/**
	 * Verifica se um cliente é apenas para Venda.
	 * 
	 * @return venda apenas ou consignado.
	 */
	public boolean isVendaApenas()
	{
		return this.permissoes.contains(Permissao.ROLE_VENDA) 
				&& !this.isCliente();
	}
	
	/**
	 * Verifica se um usuário é Administrador.
	 * 
	 * @return True se for Admin.
	 */
	public boolean isAdmin() {
		return this.permissoes.contains(Permissao.ROLE_ADMIN);
	}
	
	/**
	 * Retorna o primeiro nome do usuário.
	 * 
	 * @return Primeiro nome.
	 */
	public String primeiroNome() {
		int index = nome.indexOf(' ');

		if (index >= 0) {
			return nome.substring(0, nome.indexOf(' '));
		}
		
		return nome;
	}
	
	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * @return the senha
	 */
	public String getSenha() {
		return senha;
	}
	/**
	 * @param senha the senha to set
	 */
	public void setSenha(String senha) {
		this.senha = senha;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the permissoes
	 */
	public List<Permissao> getPermissoes() {
		return permissoes;
	}
	/**
	 * @param permissoes the permissoes to set
	 */
	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
	
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
