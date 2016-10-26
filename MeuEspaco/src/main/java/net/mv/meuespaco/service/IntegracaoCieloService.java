package net.mv.meuespaco.service;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;

/**
 * Interface de integração Cielo.
 * 
 * @author sidronio
 * @created 10/10/2016
 */
public interface IntegracaoCieloService {
	
	public void init();
	public Pagamento efetuaPagamento(Pagamento pagamento) throws CieloException, IntegracaoException;
}
