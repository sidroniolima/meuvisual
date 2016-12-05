package net.mv.meuespaco.model.cielo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Pagamento {

	@SerializedName("MerchantOrderId")
	private String merchandOrderId;
	
	@SerializedName("Customer")
	private Customer customer;
	
	@SerializedName("Payment")
	private Payment payment;
	
	public Pagamento() {	
		customer = new Customer();
	}
	
	/**
	 * Cria um pagamento com o código da venda (merchandId) e valor
	 * (amount).
	 * 
	 * @param merchanOrderId
	 * @param amount
	 */
	public Pagamento(String merchanOrderId, String customerName, int amount)
	{
		this();
		this.merchandOrderId = merchanOrderId;
		this.payment = new Payment(amount);
		customer = new Customer(customerName);
	}
	
	public Pagamento(String merchandOrderId, Customer customer, Payment payment) 
	{
		this();
		this.merchandOrderId = merchandOrderId;
		this.customer = customer;
		this.payment = payment;
	}

	/**
	 * Converte o objeto pagamento para Json.
	 * 
	 * @return objeto json.
	 */
	public String converterToJson()
	{
		return new Gson().toJson(this);
	}
	
	/**
	 * Cria um Pagamento a partir de um Json.
	 * 
	 * @param json
	 */
	public static Pagamento parseJson(String json)
	{
		return new Gson().fromJson(json, Pagamento.class);
	}
	
	/**
	 * Verifica se o pagament foi autorizado.
	 * 
	 * @return autorizado ou não.
	 */
	public boolean isAutorizado()
	{
		return this.payment.isAutorizado();
	}
	
	/**
	 * Retorna o paymentId do Payment.
	 * 
	 * @return
	 */
	public String paymentId() 
	{
		if (null != this.getPayment().getPaymentId())
		{
			return this.payment.getPaymentId().toString();
		}
		
		return null;
	}
	
	/**
	 * Retorna a data e hora do pagamento.
	 * 
	 * @return data e hora do pagamento.
	 */
	public LocalDateTime horarioDoPagamento()
	{
		String strDateRecieved = this.getPayment().getRecievedDate();
		
		if (strDateRecieved.isEmpty())
		{
			return null;
		}

		return LocalDateTime.parse(strDateRecieved, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}
	
	public String getMerchandOrderId() {
		return merchandOrderId;
	}
	public void setMerchandOrderId(String merchandOrderId) {
		this.merchandOrderId = merchandOrderId;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Payment getPayment() {
		return payment;
	}
	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((merchandOrderId == null) ? 0 : merchandOrderId.hashCode());
		result = prime * result + ((payment == null) ? 0 : payment.hashCode());
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
		Pagamento other = (Pagamento) obj;
		if (customer == null) {
			if (other.customer != null)
				return false;
		} else if (!customer.equals(other.customer))
			return false;
		if (merchandOrderId == null) {
			if (other.merchandOrderId != null)
				return false;
		} else if (!merchandOrderId.equals(other.merchandOrderId))
			return false;
		if (payment == null) {
			if (other.payment != null)
				return false;
		} else if (!payment.equals(other.payment))
			return false;
		return true;
	}
	
}
