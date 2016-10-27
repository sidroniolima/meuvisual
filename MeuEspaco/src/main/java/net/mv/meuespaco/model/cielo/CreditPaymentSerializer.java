package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CreditPaymentSerializer implements JsonSerializer<CreditPayment> {

	@Override
	public JsonElement serialize(CreditPayment payment, Type type, JsonSerializationContext jsonContext) 
	{
		final JsonObject jsonPayment = new JsonObject();
		
		jsonPayment.addProperty("Type", payment.getType().toString());
		jsonPayment.addProperty("Amount", payment.getAmount()*100);
		jsonPayment.addProperty("Installments", payment.getInstallments());
		jsonPayment.addProperty("Installments", payment.getInstallments());
		
		JsonObject jsonCreditCard = new JsonObject();
		jsonCreditCard.addProperty("CardNumber", payment.getCard().getCardNumber());
		jsonCreditCard.addProperty("Holder", payment.getCard().getHolder());
		jsonCreditCard.addProperty("ExpirationDate", payment.getCard().getExpirationDate());
		jsonCreditCard.addProperty("SecurityCode", payment.getCard().getSecurityCode());
		jsonCreditCard.addProperty("Brand", payment.getCard().getBrand().toString());
		
		jsonPayment.add("CreditCard", jsonCreditCard);
		
		return jsonPayment;
	}

}
