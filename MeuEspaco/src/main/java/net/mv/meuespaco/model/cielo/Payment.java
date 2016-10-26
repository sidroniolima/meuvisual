package net.mv.meuespaco.model.cielo;

import java.util.UUID;

import com.google.gson.annotations.SerializedName;

public class Payment {

	@SerializedName("Type")
	private PaymentType type;
	
	@SerializedName("Amount")
	private float amount;
	
	@SerializedName("Installments")
	private int installments;
	
	@SerializedName("CreditCard")
	private CreditCard creditCard;
	
	@SerializedName("ProofOfSale")
	private String proofOfSale;
	
	@SerializedName("Tid")
	private String tId;
	
	@SerializedName("AuthorizationCode")
	private String authorizationCode;
	
	@SerializedName("SoftDescriptor")
	private final String softDescriptor = "Meu Espaço";
	
	@SerializedName("PaymentId")
	private UUID paymentId;
	
	@SerializedName("Status")
	private String status;
	
	@SerializedName("ReturnCode")
	private String returnCode;
	
	@SerializedName("ReturnMessage")
	private String returnMessage;
	
	/**
	 * Padrão com installments (parcela) única.
	 */
	public Payment() {	
		this.installments = 1;
		this.type = PaymentType.CreditCard;
		this.paymentId = UUID.randomUUID();
		this.creditCard = new CreditCard();
	}
	
	/**
	 * Criação do pagamento;
	 * 
	 * @param type
	 * @param amount
	 * @param installments
	 * @param creditCard
	 */
	public Payment(PaymentType type, float amount, int installments, CreditCard creditCard) 
	{
		this();
		this.type = type;
		this.amount = amount;
		this.installments = installments;
		this.creditCard = creditCard;
	}
	
	/**
	 * Cria um pagamento com valor (amount).
	 * 
	 * @param amount
	 */
	public Payment(float amount) 
	{
		this();
		this.amount = amount;
	}

	/**
	 * Criaçã da Resposta.
	 * 
	 * @param proofOfSale
	 * @param tId
	 * @param authorizationCode
	 * @param paymanetId
	 * @param status
	 * @param returnCode
	 * @param returnMessage
	 */
	public Payment(String proofOfSale, String tId, String authorizationCode, 
			UUID paymentId, String status,
			String returnCode, String returnMessage) 
	{
		this();
		
		this.proofOfSale = proofOfSale;
		this.tId = tId;
		this.authorizationCode = authorizationCode;
		this.paymentId = paymentId;
		this.status = status;
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}
	
	/**
	 * Verifica se um pagamento foi autorizado, 
	 * com código de retorno 4.
	 * 
	 * @return se autorizado ou não.
	 */
	public boolean isAutorizado()
	{
		return this.returnCode.equals("4");
	}
	
	public PaymentType getType() {
		return type;
	}
	public void setType(PaymentType type) {
		this.type = type;
	}
	
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public int getInstallments() {
		return installments;
	}
	public void setInstallments(int installments) {
		this.installments = installments;
	}
	
	public CreditCard getCreditCard() {
		return creditCard;
	}
	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public String getProofOfSale() {
		return proofOfSale;
	}
	public void setProofOfSale(String proofOfSale) {
		this.proofOfSale = proofOfSale;
	}

	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public UUID getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(UUID paymentId) {
		this.paymentId = paymentId;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	public String getSoftDescriptor() {
		return softDescriptor;
	}

}
