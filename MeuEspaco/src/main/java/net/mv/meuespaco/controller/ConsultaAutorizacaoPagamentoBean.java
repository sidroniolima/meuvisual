package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.service.IntegracaoCieloService;

/**
 * Consulta as informações da venda que necessita de aprovação
 * exibindo as informações.
 * 
 * @author sidronio
 * @create 09/11/16
 */
public class ConsultaAutorizacaoPagamentoBean implements Serializable {

	private static final long serialVersionUID = 3799992020378032525L;

	@Inject
	private IntegracaoCieloService cielo;
	
	@Inject @Param(name="pay-id")
	private String paramPaymentId;
	
	@PostConstruct
	public void init()
	{
		if (null != paramPaymentId)
		{
			//cielo.consultaPagamento(paymentId, type);
		}
	}
}
