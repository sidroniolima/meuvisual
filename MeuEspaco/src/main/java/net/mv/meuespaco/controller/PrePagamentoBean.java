package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import net.mv.meuespaco.model.loja.Venda;

/**
 * Registra a venda atual, que aguarda o pagamento. Evita  
 * que o código da venda seja passado na URL no acesso ao Controller 
 * do pagamento.
 * 
 * @author sidronio
 * @created 26/10/2016
 */
@SessionScoped
public class PrePagamentoBean implements Serializable {

	private static final long serialVersionUID = -7480114408471377475L;
	
	private Venda venda;
	
	/**
	 * Registra uma venda para pagamento.
	 * 
	 * @param venda a pagar.
	 */
	public void registrarVenda(Venda venda)
	{
		this.venda = venda;
	}
	
	/**
	 * Remove o registro da venda aguardando o pagamento. 
	 * Utilizado quando de fato efetuar o pagamento.
	 */
	public void removerVenda()
	{
		this.venda = null;
	}
	
	/**
	 * Retorna a venda atual para ser paga.
	 * 
	 * @return venda atual não paga.
	 */
	public Venda vendaParaPagamento()
	{
		return this.venda;
	}

}
