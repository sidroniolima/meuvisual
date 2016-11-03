package net.mv.meuespaco.model.cielo;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class DebitPayment extends Payment {

	@SerializedName("ReturnUrl")
	private static final String returnUrl = "http://www.meuvisualsemijoias.com/private/site/index.xhtml";
	
	@SerializedName("AuthenticationUrl")
	private String authenticationUrl;

	public DebitPayment() 
	{
		super();
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
	public Payment fromJson(String json) 
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DebitPayment.class, new DebitPaymentDeserializer());
		
		Gson gson = gsonBuilder.serializeNulls().create();
		
		return gson.fromJson(json, DebitPayment.class);
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public String getAuthenticationUrl() {
		return authenticationUrl;
	}
	public void setAuthenticationUrl(String authenticationUrl) {
		this.authenticationUrl = authenticationUrl;
	}
	
}
