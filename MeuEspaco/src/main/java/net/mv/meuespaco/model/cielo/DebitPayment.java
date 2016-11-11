package net.mv.meuespaco.model.cielo;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.UUID;

import org.apache.http.client.utils.URIBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class DebitPayment extends Payment {

	@SerializedName("ReturnUrl")
	private final String returnUrl 
		= "http://www.meuvisualsemijoias.com/private/site/venda/consulta_pagamento.xhtml";
	
	@SerializedName("AuthenticationUrl")
	private String authenticationUrl;

	public DebitPayment() 
	{
		super();
	}
	
	public DebitPayment(float amount)
	{
		super(amount);
	}
	
	public DebitPayment(float amount, Card card) {
		super(amount, card);
	}

	public DebitPayment(float amount, String authenticationUrl, Card card) 
	{
		super(amount, card);
		this.authenticationUrl = authenticationUrl;
	}

	public DebitPayment(float amount, String tId, String paymentId, String status, String returnCode,
			String authenticationUrl, Card card) 
	{
		super(amount, card, tId, UUID.fromString(paymentId), status, returnCode);
		this.authenticationUrl = authenticationUrl;
	}

	public DebitPayment(float amount, String tId, String proofOfSale, String paymentId, String status, String returnCode,
			String authenticationUrl, Card card) 
	{
		super(amount, card, tId, proofOfSale, UUID.fromString(paymentId), status, returnCode);
		this.authenticationUrl = authenticationUrl;
	}
	
	@Override
	public PaymentType getType() {
		return PaymentType.DebitCard;
	}
	
	/**
	 * Verifica se um pagamento foi autorizado, 
	 * com código de retorno 0.
	 * 
	 * @return se autorizado ou não.
	 */
	@Override
	public boolean isAutorizado()
	{
		return this.getStatus().equals("0");
	}

	@Override
	public String toJson() 
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DebitPayment.class, new DebitPaymentSerializer());
		gsonBuilder.setPrettyPrinting();
		
		Gson gson = gsonBuilder.create();
		
		return gson.toJson(this);
	}

	@Override
	public DebitPayment fromJson(String json) 
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DebitPayment.class, new DebitPaymentDeserializer());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.serializeNulls().create();
		
		return gson.fromJson(json, DebitPayment.class);
	}
	
	/**
	 * Gera a url de retorno com o id do pagamento e 
	 * o tipo.
	 * 
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException 
	 */
	public String geraUrlRetorno() throws URISyntaxException, MalformedURLException
	{
		URIBuilder uriBuilder = new URIBuilder(returnUrl);
		uriBuilder.addParameter("pay-id", this.getPaymentId().toString());
		uriBuilder.addParameter("type", this.getType().toString());
		
		return uriBuilder.build().toURL().toString();
	}
	
	public String getAuthenticationUrl() {
		return authenticationUrl;
	}
	public void setAuthenticationUrl(String authenticationUrl) {
		this.authenticationUrl = authenticationUrl;
	}

	public String getReturnUrl() {
		return returnUrl;
	}
}
