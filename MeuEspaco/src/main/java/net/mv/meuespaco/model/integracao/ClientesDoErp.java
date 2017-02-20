package net.mv.meuespaco.model.integracao;

/**
 * Informações dos clientes vindos do ERP.
 * 
 * @author sidronio
 * @created 15/02/17
 */
public class ClientesDoErp {

	private String codigoSiga;
	private String nome;
	private String cpf;
	private int qtd;
	private float valor;
	private String codigoRegiao;
	
	public ClientesDoErp(String codigoSiga, String nome, String cpf, int qtd, float valor, String codigoRegiao) {
		this.codigoSiga = codigoSiga;
		this.nome = nome;
		this.cpf = cpf;
		this.qtd = qtd;
		this.valor = valor;
		this.codigoRegiao = codigoRegiao;
	}

	/**
	 * Cria um cliente do erp de acordo com as colunas.
	 * 
	 * @param colunas
	 * @return
	 */
	public static ClientesDoErp build(String... colunas)
	{
		return new ClientesDoErp(colunas[0], 
				colunas[1], 
				colunas[2], 
				Integer.parseInt(colunas[4]), 
				Float.parseFloat(colunas[5]), 
				colunas[6]);
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public int getQtd() {
		return qtd;
	}

	public void setQtd(int qtd) {
		this.qtd = qtd;
	}

	public float getValor() {
		return valor;
	}

	public void setValor(float valor) {
		this.valor = valor;
	}

	public String getCodigoRegiao() {
		return codigoRegiao;
	}

	public void setCodigoRegiao(String codigoRegiao) {
		this.codigoRegiao = codigoRegiao;
	}

	
	
}
