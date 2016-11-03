package net.mv.meuespaco.model.cielo;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardSerializerTest {

	CardSerializer serializer = new CardSerializer();
	private final String json = "{\"CardNumber\":\"0000000000000001\",\"Holder\":\"Sidronio Lima\",\"ExpirationDate\":\"07/2017\",\"SecurityCode\":\"123\",\"Brand\":\"Visa\"}";
	private final Card card = new Card("0000000000000001", "Sidronio Lima", "07/2017", "123", Brand.Visa);
	
	@Test
	public void deveSerializar() 
	{
		String jsonCard = serializer.serialize(card, null, null).toString();
		
		assertEquals("Card para Json", jsonCard, json);
	}

}
