package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;
import java.util.UUID;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class CreditPaymentDeserializer implements JsonDeserializer<CreditPayment>
{

	@Override
	public CreditPayment deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException 
	{
		String proof = null;
		final JsonObject jsonObject = json.getAsJsonObject();

		final JsonElement jsonAmount = jsonObject.get("Amount");
		final float amount = jsonAmount.getAsFloat()/100;
		
		final JsonElement jsonTId = jsonObject.get("Tid");
		final String tId = jsonTId.getAsString();
		/*
		final JsonElement jsonAuthorizationCode = jsonObject.get("AuthorizationCode");
		final String authorizationCode = jsonAuthorizationCode.getAsString();
		*/
		final JsonElement jsonProof = jsonObject.get("ProofOfSale");
		
		if (null != jsonProof)
		{
			proof = jsonProof.getAsString();	
		}
		
		final JsonElement jsonPaymentId = jsonObject.get("PaymentId");
		final String paymentId = jsonPaymentId.getAsString();
		
		final JsonElement jsonInstallments = jsonObject.get("Installments");
		final int installments = jsonInstallments.getAsInt();

		final JsonElement jsonStatus = jsonObject.get("Status");
		final String status = jsonStatus.getAsString();

		final JsonElement jsonReturnCode = jsonObject.get("ReturnCode");
		final String returnCode = jsonReturnCode.getAsString();
		
		final JsonElement jsonReturnMessage = jsonObject.get("ReturnMessage");
		final String returnMessage = jsonReturnMessage.getAsString();
		
		/*
		final JsonElement jsonProofOfSale = jsonObject.get("ProofOfSale");
		final String proofOfSale = jsonProofOfSale.getAsString();
		*/
	
		final JsonObject jsonCard = jsonObject.getAsJsonObject("CreditCard");
		
		Card card = new Card().fromJson(jsonCard.toString());

		CreditPayment payment = new 
				CreditPayment(
						amount, 
						card, 
						tId, 
						proof,
						UUID.fromString(paymentId), 
						status, 
						returnCode, 
						returnMessage, 
						installments);
		
		return payment;
	}

}