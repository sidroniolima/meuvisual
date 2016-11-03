package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DebitPaymentDeserializer implements JsonDeserializer<DebitPayment> {

	@Override
	public DebitPayment deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException 
	{
		JsonObject jsonPayment = json.getAsJsonObject();
		
		final JsonObject jsonObject = json.getAsJsonObject();

		final JsonElement jsonAmount = jsonObject.get("Amount");
		final float amount = jsonAmount.getAsFloat()/100;
		
		final JsonElement jsonTId = jsonObject.get("Tid");
		final String tId = jsonTId.getAsString();

		final JsonElement jsonPaymentId = jsonObject.get("PaymentId");
		final String paymentId = jsonPaymentId.getAsString();
		
		final JsonElement jsonStatus = jsonObject.get("Status");
		final String status = jsonStatus.getAsString();

		final JsonElement jsonReturnCode = jsonObject.get("ReturnCode");
		final String returnCode = jsonReturnCode.getAsString();
		
		JsonElement jsonAuthenticationUrl = jsonPayment.get("AuthenticationUrl");
		final String authenticationUrl = jsonAuthenticationUrl.getAsString();
		
		final JsonObject jsonCard = jsonPayment.getAsJsonObject("DebitCard");
		final Card card = new Card().fromJson(jsonCard.toString());
		
		return new DebitPayment(
				amount, 
				tId, 
				paymentId, 
				status, 
				returnCode, 
				authenticationUrl, 
				card);

	}

}
