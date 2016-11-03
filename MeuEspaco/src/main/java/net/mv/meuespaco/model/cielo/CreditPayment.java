package net.mv.meuespaco.model.cielo;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class CreditPayment extends Payment {

	@SerializedName("Installments")
	private int installments;
	
	@SerializedName("ProofOfSale")
	private String proofOfSale;
	
	@SerializedName("AuthorizationCode")
	private String authorizationCode;
	
	/**
	 * Construtor padrão com parcela única.
	 */
	public CreditPayment() {	
		super();
		this.installments = 1;
	}
	
	/**
	 * Criação do pagamento via cartão de crédito com valor, parcelas e 
	 * cartão.;
	 * 
	 * @param amount
	 * @param installments
	 * @param creditCard
	 */
	public CreditPayment(float amount, int installments, Card creditCard) 
	{
		super(amount, creditCard);		
		this.installments = installments;
	}
	
	/**
	 * Cria um pagamento com valor (amount).
	 * 
	 * @param amount
	 */
	public CreditPayment(float amount) 
	{
		super(amount);
		this.installments = 1;
	}
	
	/**
	 * Construtor com todos os membros.
	 * 
	 * @param amount
	 * @param card
	 * @param tId
	 * @param paymentId
	 * @param status
	 * @param returnCode
	 * @param returnMessage
	 */
	public CreditPayment(float amount, Card card, String tId, UUID paymentId, String status, 
			String returnCode, String returnMessage, int installments) 
	{
		super(amount, card, tId, paymentId, status, returnCode, returnMessage);
		this.installments = installments;
	}
	
	@Override
	public PaymentType getType() {
		return PaymentType.CreditCard;
	}
	
	/**
	 * Verifica se um pagamento foi autorizado, 
	 * com código de retorno 1.
	 * 
	 * @return se autorizado ou não.
	 */
	@Override
	public boolean isAutorizado()
	{
		return this.getStatus().equals("1");
	}
	
	@Override
	public String toJson()
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CreditPayment.class, new CreditPaymentSerializer());
		gsonBuilder.setPrettyPrinting();
		
		Gson gson = gsonBuilder.create();
		
		return gson.toJson(this);
	}
	
	@Override
	public CreditPayment fromJson(String json) 
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CreditPayment.class, new CreditPaymentDeserializer());
		
		Gson gson = gsonBuilder.serializeNulls().create();
		
		return gson.fromJson(json, CreditPayment.class);
	}

	public int getInstallments() {
		return installments;
	}
	public void setInstallments(int installments) {
		this.installments = installments;
	}

	public String getProofOfSale() {
		return proofOfSale;
	}
	public void setProofOfSale(String proofOfSale) {
		this.proofOfSale = proofOfSale;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

}
