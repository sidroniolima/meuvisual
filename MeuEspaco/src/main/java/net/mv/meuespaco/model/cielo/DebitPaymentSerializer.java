package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DebitPaymentSerializer implements JsonSerializer<DebitPayment>{

	@Override
	public JsonElement serialize(DebitPayment debit, Type type, JsonSerializationContext context) 
	{
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("Type", debit.getType().toString());
		jsonObject.addProperty("Amount", new BigDecimal(debit.getAmount() * 100).setScale(0, RoundingMode.HALF_UP));
		jsonObject.addProperty("ReturnUrl", debit.getReturnUrl());
		
		jsonObject.add("DebitCard", 
				new JsonParser().parse(debit.getCard().toJson()));
		
		return jsonObject;
	}

}
