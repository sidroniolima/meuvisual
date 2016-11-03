package net.mv.meuespaco.model.cielo;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class CardDeserializer implements JsonDeserializer<Card> 
{

	@Override
	public Card deserialize(JsonElement json, Type type, JsonDeserializationContext context)
			throws JsonParseException 
	{
		final JsonObject jsonObject = json.getAsJsonObject();

		final JsonElement jsonCardNumber = jsonObject.get("CardNumber");
		final String number = jsonCardNumber.getAsString();
		
		final JsonElement jsonHolder = jsonObject.get("Holder");
		final String holder = jsonHolder.getAsString();
		
		final JsonElement jsonExpDate = jsonObject.get("ExpirationDate");
		final String expDate = jsonExpDate.getAsString();
		
		final JsonElement jsonBrand = jsonObject.get("Brand");
		final String brand = jsonBrand.getAsString();
		
		return new Card(number, holder, expDate, Brand.valueOf(brand));
		
	}

}
