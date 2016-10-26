package net.mv.meuespaco.model.cielo;

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
	public Pagamento(String merchanOrderId, String customerName, float amount)
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
	 * Verifica se o pagament foi autorizado.
	 * 
	 * @return autorizado ou não.
	 */
	public boolean isAutorizado()
	{
		return this.payment.isAutorizado();
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
	
}
