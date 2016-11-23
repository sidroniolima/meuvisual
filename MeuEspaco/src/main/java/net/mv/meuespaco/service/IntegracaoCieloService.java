package net.mv.meuespaco.service;

import java.math.BigDecimal;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.cielo.Payment;

/**
 * Interface de integração Cielo.
 * 
 * @author sidronio
 * @created 10/10/2016
 */
public interface IntegracaoCieloService {
	
	public void init();
	public Pagamento efetuaPagamento(Pagamento pagamento) throws CieloException, IntegracaoException;
	public Pagamento consultaPagamento(String paymentId) throws CieloException, IntegracaoException;
	Payment cancelaCompra(String paymentId, BigDecimal valor) throws RegraDeNegocioException, IntegracaoException, CieloException;
}
