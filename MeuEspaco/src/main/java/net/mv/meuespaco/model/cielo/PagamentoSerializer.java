package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class PagamentoSerializer implements JsonSerializer<Pagamento>{

	@Override
	public JsonElement serialize(Pagamento pagamento, Type type, JsonSerializationContext jsonContext) 
	{
		final JsonObject jsonPagamento = new JsonObject();
		
		jsonPagamento.addProperty("MerchantOrderId", pagamento.getMerchantOrderId());
		
		JsonObject jsonCustomer = new JsonObject();
		jsonCustomer.addProperty("Name", pagamento.getCustomer().getName());
		
		jsonPagamento.add("Customer", jsonCustomer);
		jsonPagamento.add("Payment", new JsonParser().parse(pagamento.getPayment().toJson()));
		
		return jsonPagamento;
	}

}
