package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class CardSerializer implements JsonSerializer<Card> 
{

	@Override
	public JsonElement serialize(Card card, Type type, JsonSerializationContext context) 
	{
		JsonObject jsonCreditCard = new JsonObject();
		
		jsonCreditCard.addProperty("CardNumber", card.getCardNumber());
		jsonCreditCard.addProperty("Holder", card.getHolder());
		jsonCreditCard.addProperty("ExpirationDate", card.getExpirationDate());
		jsonCreditCard.addProperty("SecurityCode", card.getSecurityCode());
		jsonCreditCard.addProperty("Brand", card.getBrand().toString());
		
		return jsonCreditCard;
	}

}
