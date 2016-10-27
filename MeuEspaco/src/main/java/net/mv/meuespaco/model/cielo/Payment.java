package net.mv.meuespaco.model.cielo;

import java.util.UUID;

import com.google.gson.annotations.SerializedName;

public abstract class Payment {

	@SerializedName("Amount")
	private float amount;

	@SerializedName(value="CreditCard", alternate={"DebitCard"})
	private Card card;
	
	@SerializedName("Tid")
	private String tId;
	
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
		this.paymentId = UUID.randomUUID();
		this.card = new Card();
	}
	
	/**
	 * Criação do pagamento;
	 * 
	 * @param amount
	 * @param card
	 */
	public Payment(float amount, Card card) 
	{
		this();
		this.amount = amount;
		this.card = card;
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
	public Payment(float amount, Card card, String tId, UUID paymentId, String status, String returnCode,
			String returnMessage) {
		this();
		this.amount = amount;
		this.card = card;
		this.tId = tId;
		this.paymentId = paymentId;
		this.status = status;
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}

	/**
	 * Define o tipo do Payment.
	 * 
	 * @return
	 */
	public abstract PaymentType getType();
	
	/**
	 * Serializa o objeto.
	 * 
	 * @return json.
	 */
	public abstract String toJson();
	
	/**
	 * Cria um objeto pelo Json.
	 */
	public abstract Payment fromJson(String json);
	
	/**
	 * Verifica se um pagamento foi autorizado, 
	 * com código de retorno 4.
	 * 
	 * @return se autorizado ou não.
	 */
	public boolean isAutorizado()
	{
		return this.status.equals("1");
	}
	
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}

	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
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
