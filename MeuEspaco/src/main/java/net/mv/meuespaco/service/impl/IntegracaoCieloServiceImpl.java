package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.model.cielo.CieloError;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
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
	
	private final Logger log = Logger.getLogger(IntegracaoCieloServiceImpl.class.getName());
	
	private final String apiUrlPagamento = "https://apisandbox.cieloecommerce.cielo.com.br/1/sales/";
	private final String apiUrlConsulta = "https://apiquerysandbox.cieloecommerce.cielo.com.br/1/sales/";
	
	private Client client;
	private WebTarget target;
	
	@PostConstruct
	public void init()
	{
		client = new ResteasyClientBuilder()
				.hostnameVerification(ResteasyClientBuilder.HostnameVerificationPolicy.ANY)
				.build();
	}
	
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	public Pagamento efetuaPagamento(Pagamento pagamento) throws CieloException, IntegracaoException {

		this.target = this.client.target(apiUrlPagamento);
		
		Response clientResponse = this.target
				.request(MediaType.APPLICATION_JSON)
				.header("MerchantId", "fc0ee470-05c5-49f3-8199-6feef8fe3880")
				.header("MerchantKey", "QVFSNMALCAUVPPYZLTCBJESLIWNZWBPTVSNLLFCN")
				.post(Entity.json(pagamento.converterToJson()), Response.class);
		
		if (clientResponse.getStatus() == 500)
		{
			throw new IntegracaoException("Não foi possível acessar a API Cielo, "
					+ "tente mais tarde por favor.");
		}
		
		log.info("Enviando pagamento da compra: " + pagamento.getMerchandOrderId());
		
		String respostaJson = clientResponse.readEntity(String.class);
		
		log.info(respostaJson);
		
		Pagamento resposta;
		CieloError[] erros;		
		
		try 
		{
			resposta = Pagamento.parseJson(respostaJson);
		} catch (JsonSyntaxException ex)
		{
			erros = new Gson().fromJson(respostaJson, CieloError[].class);
			throw new CieloException(erros[0].toString());
		}
		
		return resposta;
	}
	
	@Override
	@GET
	public Pagamento consultaPagamento(String paymentId) throws CieloException, IntegracaoException 
	{
		Pagamento resposta;
		
		this.target = this.client.target(apiUrlConsulta + paymentId);

		Response clientResponse = this.target
				.request(MediaType.APPLICATION_JSON)
				.header("Content-Type", "application/json")
				.header("MerchantId", "fc0ee470-05c5-49f3-8199-6feef8fe3880")
				.header("MerchantKey", "QVFSNMALCAUVPPYZLTCBJESLIWNZWBPTVSNLLFCN")	
				.header("PaymentId", paymentId)
				.get(Response.class);
		
		if (clientResponse.getStatus() == 500)
		{
			log.warning("CIELO Error 500");
			
			throw new IntegracaoException("Não foi possível consultar a venda neste momento. "
					+ "Tente novamente mais tarde por favor.");
		}
		
		String respostaJson = clientResponse.readEntity(String.class);
		
		log.info(respostaJson);
		
		resposta = Pagamento.parseJson(respostaJson);
		
		return resposta;
	}
}
