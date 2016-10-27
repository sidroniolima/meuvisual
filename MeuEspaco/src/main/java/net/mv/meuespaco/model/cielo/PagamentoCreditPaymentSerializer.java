package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PagamentoCreditPaymentSerializer implements JsonSerializer<Pagamento>{

	@Override
	public JsonElement serialize(Pagamento pagamento, Type type, JsonSerializationContext jsonContext) 
	{
		final JsonObject jsonPagamento = new JsonObject();
		
		jsonPagamento.addProperty("MerchantOrderId", pagamento.getMerchantOrderId());
		
		JsonObject jsonCustomer = new JsonObject();
		jsonCustomer.addProperty("Name", pagamento.getCustomer().getName());
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(CreditPayment.class, new CreditPaymentSerializer());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();
		
		jsonPagamento.add("Customer", jsonCustomer);
		jsonPagamento.add("Payment", gson.toJsonTree(pagamento.getPayment()));
		
		return jsonPagamento;
	}

}
