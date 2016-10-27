package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PagamentoCreditPaymentDeserializer implements JsonDeserializer<Pagamento> 
{

	@Override
	public Pagamento deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException 
	{
		Pagamento pagamento;
		Payment payment;
		
		final JsonObject jsonPagamento = json.getAsJsonObject();
		
		final JsonElement jsonMerchantId = jsonPagamento.get("MerchantOrderId");
		final String merchantId = jsonMerchantId.getAsString();
		
		final JsonObject jsonCustomer = jsonPagamento.getAsJsonObject("Customer");
		final JsonElement jsonName = jsonCustomer.get("Name");
		final String name = jsonName.getAsString();
		
		final JsonObject jsonPayment = jsonPagamento.getAsJsonObject("Payment");
		
		payment = new CreditPayment().fromJson(jsonPayment.getAsJsonObject().toString());
		
		pagamento = new Pagamento(merchantId, new Customer(name), payment); 
		
		return pagamento;
	}

}
