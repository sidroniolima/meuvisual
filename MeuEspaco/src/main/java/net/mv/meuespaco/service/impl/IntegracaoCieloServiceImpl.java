package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.cielo.CieloError;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.cielo.Payment;
import net.mv.meuespaco.service.IntegracaoCieloService;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.DataDoSistema;
import net.mv.meuespaco.util.ValorEmCentavos;

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
	
	//private final String apiUrlPagamento = "https://apisandbox.cieloecommerce.cielo.com.br/1/sales/";
	//private final String apiUrlConsulta = "https://apiquerysandbox.cieloecommerce.cielo.com.br/1/sales/";
	
	private final String apiUrlTransacao = "https://api.cieloecommerce.cielo.com.br/1/sales/";
	private final String apiUrlConsulta = "https://apiquery.cieloecommerce.cielo.com.br/1/sales/";
	
	private final String msgErroIntegracao = "Não foi possível acessar os dados do pagamento. "
			+ "Tente novamente mais tarde por favor.";
	
	private final String merchantId = "edbe2843-5292-4468-bdb1-7d9548cd6747";
	private final String merchantKey = "IzbLJP5EZqTZ0iozhq0OCgw9dA85GsIwp9sGpold";
	
	private Client client;
	private WebTarget target;
	
	@Inject
	private VendaService vendaSrvc;
	
	private DataDoSistema relogio;

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

		this.target = this.client.target(apiUrlTransacao);
		
		Response clientResponse = this.target
				.request(MediaType.APPLICATION_JSON)
				.header("MerchantId", merchantId)
				.header("MerchantKey", merchantKey)
				.post(Entity.json(pagamento.converterToJson()), Response.class);
		
		if (clientResponse.getStatus() == 500)
		{
			throw new IntegracaoException(msgErroIntegracao);
		}
		
		log.info("Enviando pagamento da compra: " + pagamento.getMerchandOrderId());
		
		String respostaJson = clientResponse.readEntity(String.class);
		
		log.info("Resposta Cielo: " + respostaJson);
		
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
				.header("MerchantId", merchantId)
				.header("MerchantKey", merchantKey)
				.header("PaymentId", paymentId)
				.get(Response.class);
		
		if (clientResponse.getStatus() == 500)
		{
			log.warning("CIELO Error 500");
			
			throw new IntegracaoException(msgErroIntegracao);
		}
		
		String respostaJson = clientResponse.readEntity(String.class);
		
		log.info("Consulta de pagamento: " + respostaJson);
		
		resposta = Pagamento.parseJson(respostaJson);
		
		return resposta;
	}
	
	@Override
	public Payment cancelaCompra(String paymentId, BigDecimal valorDaVenda) throws RegraDeNegocioException, IntegracaoException, CieloException
	{
		if (null == paymentId || paymentId.isEmpty())
		{
			throw new RegraDeNegocioException("Não foi possívelo localizar o pagamento.");
		}
		
		BigDecimal valorEmCentavos = ValorEmCentavos.toCentavos(valorDaVenda);
		
		this.target = this.client.target(this.apiUrlTransacao + paymentId + "/void?amount="+valorEmCentavos);
		
		Response clientResponse = this.target
				.request(MediaType.APPLICATION_JSON)
				.header("Content-Type", "application/json")
				.header("MerchantId", merchantId)
				.header("MerchantKey", merchantKey)
				.header("PaymentId", paymentId)
				.put(Entity.json(""), Response.class);
		
		if (clientResponse.getStatus() == 500)
		{
			log.warning("CIELO Error 500");
			
			throw new IntegracaoException(msgErroIntegracao);
		}
		
		String respostaJson = clientResponse.readEntity(String.class);
		
		log.info("Cancelamento de pagamento: " + respostaJson);
		
		Payment resposta;
		CieloError[] erros;		
		
		try 
		{
			resposta = new Gson().fromJson(respostaJson, Payment.class);
		} catch (JsonSyntaxException ex)
		{
			erros = new Gson().fromJson(respostaJson, CieloError[].class);
			throw new CieloException(erros[0].toString());
		}
		
		if (!resposta.isCancelamentoEfetuado())
		{
			throw new RegraDeNegocioException("Algo deu errado no cancelamento da Compra. "
					+ "Entre em contato por telefone ou chat.");
		}
		
		return resposta;
	}
}
