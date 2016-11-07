package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CreditPaymentSerializer implements JsonSerializer<CreditPayment> {

	@Override
	public JsonElement serialize(CreditPayment payment, Type type, JsonSerializationContext jsonContext) 
	{
		final JsonObject jsonPayment = new JsonObject();
		
		jsonPayment.addProperty("Type", payment.getType().toString());
		jsonPayment.addProperty("Amount", new BigDecimal(payment.getAmount() * 100).setScale(0, RoundingMode.HALF_UP));
		jsonPayment.addProperty("Installments", payment.getInstallments());

		jsonPayment.add("CreditCard", new JsonParser().parse(payment.getCard().toJson()));
		
		return jsonPayment;
	}

}
