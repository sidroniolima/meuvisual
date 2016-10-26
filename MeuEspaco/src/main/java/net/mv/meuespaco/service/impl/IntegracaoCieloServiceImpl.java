package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
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
	public Pagamento efetuaPagamento(Pagamento pagamento) throws CieloException, IntegracaoException {

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
		
		log.info("Enviando pagamento: " + pagamento.converterToJson());
		
		String resposta = clientResponse.readEntity(String.class);
		
		Pagamento respostaJson;
		CieloError[] erros;		
		
		try 
		{
			respostaJson = new Gson().fromJson(resposta, Pagamento.class);
		} catch (JsonSyntaxException ex)
		{
			erros = new Gson().fromJson(resposta, CieloError[].class);
			throw new CieloException(erros[0].toString());
		}
		
		log.info(resposta);
		return respostaJson;
	}

}
