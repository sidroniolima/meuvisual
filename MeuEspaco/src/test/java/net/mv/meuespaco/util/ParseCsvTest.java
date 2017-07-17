package net.mv.meuespaco.util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.net.ftp.FTPClient;
import org.dbunit.util.concurrent.SynchronousChannel;
import org.junit.Test;

import net.mv.meuespaco.model.loja.Cliente;

public class ParseCsvTest {

	Map<String, ValoresDoErp> registros = new HashMap<String, ValoresDoErp>();
	
	public void deveLerOArquivo() {
		
		registros = ParseCsv.leArquivo();
		
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
		
		registros = ParseCsv.leArquivo();
		
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
	
	@Test
	public void deveGerarArquivo() throws IOException
	{
		String item1 = "000001;10;10;10";
		String item2 = "000001;10;10;10";
		
		File file = null;
		
		file = ParseCsv.criaArquivoCsvFromStream(Arrays.asList(item1, item2).stream(), "teste.csv");

		assertFalse("File criado.",  null == file);
		BufferedReader bf = new BufferedReader(new FileReader(file));
		
		String line;
		StringBuilder strBuilder = new StringBuilder();
		
		while ((line = bf.readLine()) != null)
		{
			strBuilder.append(line.concat("\n"));
		}
		
		String fileLines = strBuilder.toString();
		
		assertTrue("Reading file...", fileLines.contains(item1));
		System.out.println(fileLines);
	}
	
	@Test
	public void deveEnviarArquivoPorFtp() throws IOException
	{
		String item1 = "000001;10;10;10";
		String item2 = "000001;10;10;10";
		
		String server = "meuvisualsemijoias.net";
		String path = "/var/static/integracao/";
		String user = "meuvisua";
		String pass = "M@elSA33mv";
		
		File file = null;	
		file = ParseCsv.criaArquivoCsvFromStream(Arrays.asList(item1, item2).stream(), "teste.csv");
		
		ParseCsv.sendFtp(file, server, path, user, pass, file.getName());
	}
	
}
