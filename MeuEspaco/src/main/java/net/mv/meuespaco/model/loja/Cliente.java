package net.mv.meuespaco.model.loja;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Vetoed;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.mv.meuespaco.converter.JpaCpfConverter;
import net.mv.meuespaco.converter.JpaRgConverter;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Permissao;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.Semana;
import net.mv.meuespaco.model.Usuario;

/**
 * @author sidronio
 *
 */
@Entity
@Table(name="cliente")
@Vetoed
public class Cliente implements Serializable {

	private static final long serialVersionUID = -6448932856591570116L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Column(name="codigo_siga", columnDefinition="CHAR(6)")
	private String codigoSiga;
	
	@Enumerated(EnumType.STRING)
	private StatusCliente status;
	
	@Column(columnDefinition="VARCHAR(200)")
	private String nome;
	
	@Convert(converter=JpaCpfConverter.class)
	private Documento cpf;
	
	@Convert(converter=JpaRgConverter.class)
	private Documento rg;
	
	@Column(columnDefinition="VARCHAR(200)")
	private String email;
	
	@Column(columnDefinition="VARCHAR(200)")
	private String senha;
	
	@Column(name="telefone_fixo", columnDefinition="VARCHAR(100)")
	private String telefoneFixo;
	
	@Column(columnDefinition="VARCHAR(100)")
	private String celular;

	@ManyToOne(fetch=FetchType.LAZY)
	private Regiao regiao;
	
	@OneToOne(fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL)
	@JoinColumn(name="usuario_codigo")
	private Usuario usuario;
	
	@Column(name="qtd_de_pecas_para_escolha")
	private Float qtdDePecasParaEscolha;
	
	@Column(name="valor_para_escolha")
	private Float valorParaEscolha;
	
	/**
	 * Construtor padrão.
	 */
	public Cliente() {
		this.cpf = new Cpf();
		this.rg = new Rg();
		this.status = StatusCliente.PRE_CADASTRO;
		this.qtdDePecasParaEscolha = 0f;
		this.valorParaEscolha = 0f;
	}

	/**
	 * Cria um cliente pelo Cpf e Senha para utilização 
	 * no Login.
	 * 
	 * @param cpf Cpf para login.
	 * @param senha Senha de login.
	 */
	public Cliente(Documento cpf, String senha) {
		this();
		this.cpf = cpf;
		this.senha = senha;
	}

	/**
	 * Cria um Cliente pelo Cpf.
	 * 
	 * @param cpf
	 */
	public Cliente(Cpf cpf) {
		this();
		this.cpf = cpf;
	}

	/**
	 * Cria um Cliente pelo Nome e Cpf.
	 * 
	 * @param nome
	 * @param cpf
	 */
	public Cliente(String nome, Cpf cpf) 
	{
		this();
		
		this.nome = nome;
		this.cpf = cpf;
	}

	public Cliente(Long codigo, String nome) 
	{
		this();
		
		this.codigo = codigo;
		this.nome = nome;
	}

	/**
	 * Cria um cliente com código siga.
	 * 
	 * @param codigo
	 * @param nome
	 * @param codigoSiga
	 */
	public Cliente(Long codigo, String nome, String codigoSiga) 
	{
		this();
		
		this.codigo = codigo;
		this.codigoSiga = codigoSiga;
		this.nome = nome;
	}

	/**
	 * Cria um cliente pelo status.
	 * 
	 * @param status
	 */
	public Cliente(StatusCliente status) {
		this();
		
		this.status = status;
	}
	
	/**
	 * Cria um cliente pelo código siga.
	 * 
	 * @param codigoSiga
	 */
	public Cliente(String codigoSiga) {
		this();
		
		this.codigoSiga = codigoSiga;
	}

	/**
	 * Valida um cliente com as regras a seguir:
	 * 
	 * Cpf válido;
	 * Rg válido;
	 * Região;
	 * Email;
	 * Nome;
	 * Ao menos um telefone;
	 */
	public void valida() throws RegraDeNegocioException {
		
		this.validaPreCadastro();
		
		if (null == this.codigoSiga) {
			throw new RegraDeNegocioException("O cliente deve estar associado ao seu código Siga.");
		}
		
		if (null == this.regiao) {
			throw new RegraDeNegocioException("O cliente deve pertencer a uma Região.");
		}
		
		if (null == this.qtdDePecasParaEscolha)
		{
			throw new RegraDeNegocioException(
					"Não há a quantidade permitida de peças para o usuário. Execute a rotina de atualização.");
		}
		
		if (null == this.valorParaEscolha)
		{
			throw new RegraDeNegocioException(
					"Não há o valor permitida de peças para o usuário. Execute a rotina de atualização.");
		}
	}
	
	/**
	 * Utilizado na validação do pré-cadastro, com 
	 * informações fornecidas pelo Consultor.
	 * 
	 * Cpf válido;
	 * Email;
	 * Senha;
	 * Nome;
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void validaPreCadastro() throws RegraDeNegocioException {
		
		if (null == this.cpf) {
			throw new RegraDeNegocioException("É necessário o CPF do cliente.");
		}
		
		if (null == this.nome || this.nome.isEmpty()) {
			throw new RegraDeNegocioException("O nome do cliente é necessário.");
		}
	}

	/**
	 * Efetiva um cliente se for pré-cadastro, criando 
	 * um login com senha pré-definida.
	 * @throws RegraDeNegocioException 
	 */
	public void efetivaCadastro(List<Permissao> permissoes) throws RegraDeNegocioException {
		
		this.valida();
		
		this.status = StatusCliente.ATIVO;
		
		this.criaUsuario(permissoes);
	}
	
	/**
	 * Cria usuário para o cliente.
	 */
	public void criaUsuario(List<Permissao> permissoes)
	{
		this.usuario = new Usuario(
				this.cpf.getValor(),
				this.senha,
				this.nome, 
				this.getEmail());
		
		this.usuario.getPermissoes().addAll(permissoes);
	}
	
	/**
	 * Efetiva o cadastro atualizando o código siga e a região.
	 * 
	 * @param codigoSiga
	 * @param codigoRegiao
	 * @throws RegraDeNegocioException 
	 */
	public void efetivaCadastro(String codigoSiga, Regiao regiao, List<Permissao> permissoes) throws RegraDeNegocioException 
	{
		this.codigoSiga = codigoSiga;
		this.regiao = regiao;
		
		this.efetivaCadastro(permissoes);
	}
	
	/**
	 * Retorna o primeiro nome do cliente.
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
	 * Inativa um cliente alterando seu status para INATIVO.
	 */
	public void inativa() {
		this.setStatus(StatusCliente.INATIVO);
	}
	
	/**
	 * Parse de Cliente pela lista de atributos vindos 
	 * do arquivo de importação.
	 * 
	 * @param p Código do cliente.
	 * @param v Informações: nome, cpf, rg, email, senha e qtd para 
	 * acréscimo.
	 * @return Novo Cliente com código e informações.
	 */
	public static Cliente parse(String p, List<String> v) {
		Cliente cliente = new Cliente();
		
		cliente.setStatus(StatusCliente.ATIVO);
		cliente.setCodigoSiga(p);
		cliente.setNome(v.get(0));
		cliente.setCpf(new Cpf(v.get(1)));
		cliente.setQtdDePecasParaEscolha(new Float(v.get(2)));
		
		//cliente.criaUsuario(Permissao.ROLE_CLIENTE);
		
		return cliente;
	}
	
	/**
	 * Cria o usuário do Cliente com as próprias informações.
	 * 
	 * @param roleCliente
	 */
	public void criaUsuario(Permissao roleCliente) {
		Usuario usuario = new Usuario(this.getCpf().getValor(), this.getSenha(), this.getNome(), this.getEmail());
		usuario.adicionaPermissao(roleCliente);
		
		this.setUsuario(usuario);
	}

	/**
	 * Caso um cliente esteja inativo ele é ativado. Isso 
	 * ocorre na atualização de peças para escolha na atualização 
	 * de clientes com os dados vindos do sistema ERP.
	 */
	public void ativaCliente() {
		if (status.equals(StatusCliente.INATIVO)) {
			this.status = StatusCliente.ATIVO;
		}
	}
	
	/**
	 * Atualiza a quantidade de peças permitidas para escolha 
	 * e o valor permitido. Além de ativa o cliente.
	 * 
	 * @param qtdPermitida Qtd de peças permitida na escolha;
	 * @param valorPermitido Valor de peças permitido na escolha.
	 */
	public void atualizaValoresDoErp(float qtdPermitida, float valorPermitido) {
		this.setQtdDePecasParaEscolha(qtdPermitida);
		this.setValorParaEscolha(valorPermitido);
		
		ativaCliente();
	}
	
	/**
	 * Informa a data inicial do ciclo atual do cliente, 
	 * de acordo com a região e semana.
	 * 
	 * @return data de início do ciclo atual.
	 * @throws RegraDeNegocioException 
	 */
	public LocalDate dataInicialDoCicloAtual() throws RegraDeNegocioException
	{
		return this.semanaAtualDoCliente().getDataInicial();
	}

	/**
	 * Informa a data final do ciclo atual do cliente.
	 * 
	 * @return data final do ciclo atual.
	 * @throws RegraDeNegocioException 
	 */
	public LocalDate dataFinalDoCicloAtual() throws RegraDeNegocioException 
	{
		return this.semanaAtualDoCliente().getDataFinal();
	}
	
	/**
	 * Informa a semana atual do cliente.
	 * 
	 * @return semana atual.
	 * @throws RegraDeNegocioException 
	 */
	public Semana semanaAtualDoCliente() throws RegraDeNegocioException
	{
		Optional<Regiao> regiao = Optional.ofNullable(this.getRegiao());
		
		if (regiao.isPresent())
		{
			Optional<Semana> semana = Optional.ofNullable(regiao.get().getSemana());
			
			if (semana.isPresent())
			{
				return semana.get();
			}
		}
		
		throw new RegraDeNegocioException("Não foi possível identificar a semana atual do cliente.");
	}
	
	/**
	 * Verifica se o cliente é um pré-cadastro.
	 * 
	 * @return
	 */
	public boolean isPreCadastro() {
		return this.status.equals(StatusCliente.PRE_CADASTRO);
	}
	
	/**
	 * Verifica se um cliente tem uma permissão específica. 
	 * 
	 * @param permissao
	 * @return se tem ou não.
	 */
	public boolean temPermissao(Permissao permissao)
	{
		return this.getUsuario().getPermissoes().contains(permissao);
	}
	

	/**
	 * Adiciona permissão ao usuário.
	 * @param role
	 */
	public void addPermissao(Permissao role) 
	{
		this.getUsuario().adicionaPermissao(role);
	}

	/**
	 * Remove permissão do usuário.
	 * @param role
	 */
	public void removePermissao(Permissao role) 
	{
		if (this.temPermissao(role))
			this.getUsuario().getPermissoes().remove(role);
	}

	/**
	 * Atualiza as permissões do cliente. Utilizado na atualização.
	 * @param permissoes.
	 */
	public void atualizaPermissoes(List<Permissao> permissoes) 
	{
		this.usuario.atualizaPermissoes(permissoes);
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
	 * @return the status
	 */
	public StatusCliente getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusCliente status) {
		this.status = status;
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
	public Documento getCpf() {
		return cpf;
	}
	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(Documento cpf) {
		this.cpf = cpf;
	}

	/**
	 * @return the rg
	 */
	public Documento getRg() {
		return rg;
	}
	/**
	 * @param rg the rg to set
	 */
	public void setRg(Documento rg) {
		this.rg = rg;
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
	 * @return the telefoneFixo
	 */
	public String getTelefoneFixo() {
		return telefoneFixo;
	}
	/**
	 * @param telefoneFixo the telefoneFixo to set
	 */
	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}

	/**
	 * @return the celular
	 */
	public String getCelular() {
		return celular;
	}
	/**
	 * @param celular the celular to set
	 */
	public void setCelular(String celular) {
		this.celular = celular;
	}
	
	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the regiao
	 */
	public Regiao getRegiao() {
		return regiao;
	}
	/**
	 * @param regiao the regiao to set
	 */
	public void setRegiao(Regiao regiao) {
		this.regiao = regiao;
	}
	
	/**
	 * @return the qtdDePecasParaEscolha
	 */
	public Float getQtdDePecasParaEscolha() {
		return qtdDePecasParaEscolha;
	}
	/**
	 * @param qtdDePecasParaEscolha the qtdDePecasParaEscolha to set
	 */
	public void setQtdDePecasParaEscolha(Float qtdDePecasParaEscolha) {
		this.qtdDePecasParaEscolha = qtdDePecasParaEscolha;
	}

	public Float getValorParaEscolha() {
		return valorParaEscolha;
	}
	public void setValorParaEscolha(Float valorParaEscolha) {
		this.valorParaEscolha = valorParaEscolha;
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
		Cliente other = (Cliente) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
}
