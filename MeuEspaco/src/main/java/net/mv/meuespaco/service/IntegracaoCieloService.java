package net.mv.meuespaco.service;

import java.io.IOException;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.cielo.PaymentType;

/**
 * Interface de integração Cielo.
 * 
 * @author sidronio
 * @created 10/10/2016
 */
public interface IntegracaoCieloService {
	
	public void init();
	public Pagamento efetuaPagamentoCredito(Pagamento pagamento) throws CieloException, IntegracaoException;
	public String efetuaPagamento(Pagamento pagamento) throws CieloException, IntegracaoException;
	public Pagamento efetuaPagamentoDebito(Pagamento pagamento) throws CieloException, IntegracaoException, IOException;
	
	/**
	 * Consulta o pagamento na API Cielo pelo código do pagamento UUID 
	 * retornado quando efetuado o pagamento.
	 * 
	 * @param paymentId, , PaymentType type UUID do pagamento gerado pela Cielo no momento 
	 * transação
	 * @param type tipo de pagamento: crédito ou débito.
	 * @return informações do pagamento.
	 */
	public Pagamento consultaPagamento(String paymentId, PaymentType type);
}
