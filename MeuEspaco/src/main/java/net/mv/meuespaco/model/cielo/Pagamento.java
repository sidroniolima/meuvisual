package net.mv.meuespaco.model.cielo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Pagamento {

	@SerializedName("MerchantOrderId")
	private String merchantOrderId;
	
	@SerializedName("Customer")
	private Customer customer;
	
	@SerializedName("Payment")
	private Payment payment;
	
	public Pagamento() {	
		customer = new Customer();
	}
	
	/**
	 * Cria um pagamento com o código da venda (merchanyId) e valor
	 * (amount).
	 * 
	 * @param merchanOrderId
	 * @param amount
	 */
	public Pagamento(String merchantOrderId, String customerName)
	{
		this();
		this.merchantOrderId = merchantOrderId;
		customer = new Customer(customerName);
	}
	
	public Pagamento(String merchantOrderId, Customer customer, Payment payment) 
	{
		this();
		this.merchantOrderId = merchantOrderId;
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
		return this.generateGson().toJson(this);
	}
	
	/**
	 * Gera um pagamento pelo Json.
	 * 
	 * @param json
	 * @return
	 */
	public Pagamento fromJson(String json)
	{
		return this.generateGson().fromJson(json, Pagamento.class);
	}
	
	/**
	 * Gera o Gson registrando os TypeAdapter para o serializador e 
	 * deserializador.
	 * 
	 * @return
	 */
	private Gson generateGson()
	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Pagamento.class, new PagamentoCreditPaymentDeserializer());
		gsonBuilder.registerTypeAdapter(Pagamento.class, new PagamentoCreditPaymentSerializer());
		
		return gsonBuilder.create();
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
	
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
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
