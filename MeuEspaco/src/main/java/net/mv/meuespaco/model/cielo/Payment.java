package net.mv.meuespaco.model.cielo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import com.google.gson.annotations.SerializedName;

public class Payment {

	@SerializedName("Type")
	private PaymentType type;
	
	@SerializedName("Amount")
	private float amount;
	
	@SerializedName("ReceivedDate")
	private String recievedDate;
	
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
	private String softDescriptor;
	
	@SerializedName("PaymentId")
	private UUID paymentId;
	
	@SerializedName("Status")
	private int status;
	
	@SerializedName("ReturnCode")
	private String returnCode;
	
	@SerializedName("ReturnMessage")
	private String returnMessage;
	
	/**
	 * Padrão com installments (parcela) única.
	 */
	public Payment() 
	{	
		this.installments = 1;
		this.type = PaymentType.CreditCard;
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
		this.setAmount(amount);
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
		this.setAmount(amount);
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
			UUID paymentId, int status,
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
		return this.status == 2;
	}
	
	/**
	 * Verifica se o cancelamento foi bem sucedido.
	 * 
	 * @return cancelado ou não.
	 */
	public boolean isCancelamentoEfetuado() 
	{
		return this.status == 10;
	}
	
	public PaymentType getType() {
		return type;
	}
	public void setType(PaymentType type) {
		this.type = type;
	}
	
	public String getRecievedDate() {
		return recievedDate;
	}
	public void setRecievedDate(String recievedDate) {
		this.recievedDate = recievedDate;
	}

	public float getAmount() {
		return new BigDecimal(amount).setScale(2, RoundingMode.UNNECESSARY).floatValue();
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

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
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
	public void setSoftDescriptor(String softDescriptor) {
		this.softDescriptor = softDescriptor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(amount);
		result = prime * result + ((authorizationCode == null) ? 0 : authorizationCode.hashCode());
		result = prime * result + ((creditCard == null) ? 0 : creditCard.hashCode());
		result = prime * result + installments;
		result = prime * result + ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result + ((proofOfSale == null) ? 0 : proofOfSale.hashCode());
		result = prime * result + ((recievedDate == null) ? 0 : recievedDate.hashCode());
		result = prime * result + ((returnCode == null) ? 0 : returnCode.hashCode());
		result = prime * result + ((returnMessage == null) ? 0 : returnMessage.hashCode());
		result = prime * result + ((softDescriptor == null) ? 0 : softDescriptor.hashCode());
		result = prime * result + status;
		result = prime * result + ((tId == null) ? 0 : tId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		if (Float.floatToIntBits(amount) != Float.floatToIntBits(other.amount))
			return false;
		if (authorizationCode == null) {
			if (other.authorizationCode != null)
				return false;
		} else if (!authorizationCode.equals(other.authorizationCode))
			return false;
		if (creditCard == null) {
			if (other.creditCard != null)
				return false;
		} else if (!creditCard.equals(other.creditCard))
			return false;
		if (installments != other.installments)
			return false;
		if (paymentId == null) {
			if (other.paymentId != null)
				return false;
		} else if (!paymentId.equals(other.paymentId))
			return false;
		if (proofOfSale == null) {
			if (other.proofOfSale != null)
				return false;
		} else if (!proofOfSale.equals(other.proofOfSale))
			return false;
		if (recievedDate == null) {
			if (other.recievedDate != null)
				return false;
		} else if (!recievedDate.equals(other.recievedDate))
			return false;
		if (returnCode == null) {
			if (other.returnCode != null)
				return false;
		} else if (!returnCode.equals(other.returnCode))
			return false;
		if (returnMessage == null) {
			if (other.returnMessage != null)
				return false;
		} else if (!returnMessage.equals(other.returnMessage))
			return false;
		if (softDescriptor == null) {
			if (other.softDescriptor != null)
				return false;
		} else if (!softDescriptor.equals(other.softDescriptor))
			return false;
		if (status != other.status)
			return false;
		if (tId == null) {
			if (other.tId != null)
				return false;
		} else if (!tId.equals(other.tId))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Payment [type=" + type + ", amount=" + amount + ", installments=" + installments + ", creditCard="
				+ creditCard + ", proofOfSale=" + proofOfSale + ", tId=" + tId + ", authorizationCode="
				+ authorizationCode + ", softDescriptor=" + softDescriptor + ", paymentId=" + paymentId + ", status="
				+ status + ", returnCode=" + returnCode + ", returnMessage=" + returnMessage + "]";
	}

}
