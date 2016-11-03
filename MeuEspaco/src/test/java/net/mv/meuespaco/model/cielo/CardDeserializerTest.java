package net.mv.meuespaco.model.cielo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CardDeserializerTest {

	CardDeserializer deserializer = new CardDeserializer();
	private final String json = "{\"CardNumber\":\"0000000000000001\",\"Holder\":\"Sidronio Lima\",\"ExpirationDate\":\"07/2017\",\"SecurityCode\":\"123\",\"Brand\":\"Visa\"}";
	private final Card card = new Card("0000000000000001", "Sidronio Lima", "07/2017", "123", Brand.Visa);
		
	@Test
	public void deveDeserializar() 
	{
		JsonElement jsonElement = new JsonParser().parse(json);
		
		Card cardDeserializado = deserializer.deserialize(jsonElement, null, null);
		
		assertEquals("Número", card.getCardNumber(), cardDeserializado.getCardNumber());
		assertEquals("Holder", card.getHolder(), cardDeserializado.getHolder());
		assertEquals("Exp. Date", card.getExpirationDate(), cardDeserializado.getExpirationDate());
		assertEquals("Brand", card.getBrand(), cardDeserializado.getBrand());
		assertTrue("SecCode", null == cardDeserializado.getSecurityCode());
	}

}
