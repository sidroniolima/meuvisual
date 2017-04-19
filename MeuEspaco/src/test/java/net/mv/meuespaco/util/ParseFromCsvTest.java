package net.mv.meuespaco.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.mv.meuespaco.model.loja.Cliente;

public class ParseFromCsvTest {

	Map<String, ValoresDoErp> registros = new HashMap<String, ValoresDoErp>();
	
	public void deveLerOArquivo() {
		
		registros = ParseFromCsv.leArquivo();
		
		if (registros.isEmpty()) {
			fail("Não foi possível ler o arquivo.");
		}
		
		assertEquals("Número correto de registros.", 11528, registros.size());
		
	}
	
	public void deveListarOsClientesAusentes() {
		
		List<Cliente> clientes = new ArrayList<Cliente>();
		List<Cliente> clientesInativos = new ArrayList<Cliente>();
		
		clientes.add(new Cliente(1L, "Cliente 1", "000001"));
		clientes.add(new Cliente(2L, "Cliente 2", "000002"));
		clientes.add(new Cliente(3L, "Cliente 3", "000003"));
		clientes.add(new Cliente(4L, "Cliente 4", "000004"));
		clientes.add(new Cliente(5L, "Cliente 5", "072796"));
		clientes.add(new Cliente(6L, "Cliente 6", "072797"));
		
		registros = ParseFromCsv.leArquivo();
		
		if (registros.isEmpty()) {
			fail("Não foi possível ler o arquivo.");
		}
		
		clientesInativos = clientes.stream()
				.filter(c -> !registros.containsKey(new Integer(c.getCodigoSiga()).toString()))
				.map(c -> c)
				.collect(Collectors.toList());
				
		assertEquals("Número correto de clientes inativos.", 4, clientesInativos.size());
		
		System.out.println(clientesInativos);
		
	}
	
}
