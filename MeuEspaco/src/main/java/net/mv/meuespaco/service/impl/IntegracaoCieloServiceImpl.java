package net.mv.meuespaco.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.google.gson.Gson;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.cielo.CieloError;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.DebitPayment;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.cielo.PaymentType;
import net.mv.meuespaco.service.IntegracaoCieloService;

/**
 * Implementação do serviço de integração Cielo.
 * 
 * @author sidronio
 * @created 10/10/2016
 */
@Stateless
public class IntegracaoCieloServiceImpl implements IntegracaoCieloService, Serializable {

	private static final long serialVersionUID = -257503943599691685L;
	
	private final Logger log = Logger.getLogger(IntegracaoCieloServiceImpl.class.getName());;
	private final String apiUrl = "https://apisandbox.cieloecommerce.cielo.com.br/1/sales/";
	
	private Client client;
	private WebTarget target;
	
	@PostConstruct
	public void init()
	{
		client = new ResteasyClientBuilder()
				.hostnameVerification(ResteasyClientBuilder.HostnameVerificationPolicy.ANY)
				.build();
		
		target = this.client.target(apiUrl);
	}
	
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	public Pagamento efetuaPagamentoCredito(Pagamento pagamento) throws CieloException, IntegracaoException 
	{
		Pagamento respostaPagamento = new Pagamento().fromJson(this.efetuaPagamento(pagamento), PaymentType.CreditCard);
		
		if (!respostaPagamento.isAutorizado())
		{
			throw new CieloException("Pagamento não autorizado. Tente novamente com outro cartão.");
		}
		
		return respostaPagamento;
	}

	@Override
	public Pagamento efetuaPagamentoDebito(Pagamento pagamento) throws CieloException, IntegracaoException, IOException 
	{
		Pagamento respostaPagamento = new Pagamento().fromJson(this.efetuaPagamento(pagamento), PaymentType.DebitCard);
		
		DebitPayment payment = (DebitPayment) respostaPagamento.getPayment();
		
		return respostaPagamento;
	}

	@Override
	public String efetuaPagamento(Pagamento pagamento) throws CieloException, IntegracaoException {
		
		CieloError[] erros = null;
		Response clientResponse;
		String jsonPagamento = pagamento.converterToJson();
		
		clientResponse = this.target
				.request(MediaType.APPLICATION_JSON)
				.header("MerchantId", "fc0ee470-05c5-49f3-8199-6feef8fe3880")
				.header("MerchantKey", "QVFSNMALCAUVPPYZLTCBJESLIWNZWBPTVSNLLFCN")
				.post(Entity.json(jsonPagamento), Response.class);
		
		if (clientResponse.getStatus() == 500)
		{
			throw new IntegracaoException("Não foi possível acessar a API Cielo, "
					+ "tente mais tarde por favor.");
		}
		
		log.info("Enviando Pagamento: " + pagamento.converterToJson());
		
		String resposta = clientResponse.readEntity(String.class);
		
		log.info("Recebendo Reposta: " + resposta);
		
		if (clientResponse.getStatus() == 400)
		{
			erros = new Gson().fromJson(resposta, CieloError[].class);
			throw new CieloException(erros[0].toString());
		}
		
		return resposta;
	}

	@Override
	public Pagamento consultaPagamento(String paymentId, PaymentType type) 
	{
		Response clientResponse = this.target
				.path(paymentId)
				.request(MediaType.APPLICATION_JSON)
				.header("MerchantId", "fc0ee470-05c5-49f3-8199-6feef8fe3880")
				.header("MerchantKey", "QVFSNMALCAUVPPYZLTCBJESLIWNZWBPTVSNLLFCN")
				.get();
		
		Pagamento respostaDaAutenticacao = 
				new Pagamento().fromJson(clientResponse.readEntity(String.class), type);
		
		return respostaDaAutenticacao;
	}

}
