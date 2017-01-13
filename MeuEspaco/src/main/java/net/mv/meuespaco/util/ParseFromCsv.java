package net.mv.meuespaco.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.primefaces.model.UploadedFile;

public class ParseFromCsv {

	private static String fileName = "data/exportacao.csv";
	
	private static String filePathExportacao = IConstants.CAMINHO_DO_ARQUIVO_DE_EXPORTACAO;

	private static String fileNameExportacao = IConstants.NOME_DO_ARQUIVO_DE_EXPORTACAO;
	
	@SuppressWarnings("resource")
	public static Map<String, ValoresDoErp> leArquivo(){
		
		Map<String, ValoresDoErp> registros = new HashMap<String, ValoresDoErp>();
		
		try {
			
			Stream<String> stream = Files.lines(Paths.get(fileName));
		
			stream.forEach(line -> {
				String[] columns = line.split(",");
				registros.put(columns[0], new ValoresDoErp(new Float(columns[4]), new Float(columns[5])) );
			});
			
			return registros;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Map<String, ValoresDoErp> leArquivoDoUpload(){
		
		Map<String, ValoresDoErp> registros = new HashMap<String, ValoresDoErp>();
			
			try {
				
				URL url = new URL(filePathExportacao + fileNameExportacao);
	
		        URLConnection connection = url.openConnection();
		        connection.setReadTimeout(5000);
		        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        
		        String line = reader.readLine();
				
				while (line != null) {
					
					String[] columns = line.split(";");
					
					registros.put(columns[0], 
							new ValoresDoErp(new Float(columns[4]), new Float(columns[5])) );
					
					line = reader.readLine();
				}
				
				reader.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		
		return registros;
	}
	
	/**
	 * Lê o conteúdo de um Arquivo de Uploado no formato CSV e
	 * gera as informações das colunas.
	 * 
	 * @param file
	 * @return
	 */
	public static List<String[]> leArquivoDoUpload(UploadedFile file) {
		
		List<String[]> linesSplitted = new ArrayList<String[]>();
		
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputstream()));
			
			String line = reader.readLine();
			
			while (line != null) {
				
				String[] columns = line.split(",");
				linesSplitted.add(columns);
				
				line = reader.readLine();
			}
			
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return linesSplitted;
		
	}
		
	
	/**
	 * Le arquivo do ERP com as informações:
	 * 		Código Siga
	 * 		Nome
	 * 		Cpf
	 * 		Status
	 * 		Qtd de acréscimo
	 * 
	 * @param file Arquivo de exportação do ERP.
	 * @return Mapa com o Código siga e as demais informações.
	 */
	public static Map<String, List<String>> geraInformaçõesDaExportaçãoDoERP(UploadedFile file) {
		
		Map<String, List<String>> registros = new HashMap<String, List<String>>();
		
		List<String[]> linesSplited = leArquivoDoUpload(file);
		
		for (String[] columns : linesSplited) {
			
			if (!columns[0].isEmpty()) {
				
				registros.put(columns[0], 
						Arrays.asList(
								columns[1],
								columns[2],
								columns[4]));
				
			}
			
		}
		
		
		return registros;
	}
	
	/**
	 * Le arquivo do sistema legado com as informações:
	 * 		Cpf
	 * 		Senha
	 * 
	 * @param file Arquivo de exportação do ERP.
	 * @return Mapa com o Código siga e as demais informações.
	 */
	public static Map<String, List<String>> coletaASenhaDoSistemaLegado(UploadedFile file) {
		
		Map<String, List<String>> registros = new HashMap<String, List<String>>();
		
		List<String[]> linesSplited = leArquivoDoUpload(file);
		
		for (String[] columns : linesSplited) {
			
			registros.put(columns[0], 
					Arrays.asList(
							columns[1]));
			
		}
		
		return registros;
	}
	
	/**
	 * Lê o arquivo do ERP com os Clientes e sua Região.
	 * 		Cpf
	 * 		Código Região
	 * 
	 * @param file Arquivo de exportação do ERP.
	 * @return Mapa com o Código siga e as demais informações.
	 */
	public static Map<String, List<String>> atualizaARegiaoDosClientes(UploadedFile file) {
		
		Map<String, List<String>> registros = new HashMap<String, List<String>>();
		
		List<String[]> linesSplited = leArquivoDoUpload(file);
		
		for (String[] columns : linesSplited) {
		
			if (columns.length > 1) {
				
				registros.put(columns[0], 
						Arrays.asList(
								columns[1]));
			} else {
			}
				
		}
		
		return registros;
	}
	
	/**
	 * Lê o arquivo dado um Path e retorna as linhas como Stream.
	 * 
	 * @param path
	 * @return Stream das linhas do arquivo.
	 * @throws IOException
	 */
	public static Stream<String> leArquivoCsvDoERP(URL url) throws IOException
	{
		try (BufferedReader reader = ParseFromCsv.getBufferedReaderFromUrl(url) )
		{
		
		return reader.lines();
			
		}
	}
	
	/**
	 * Retorna o BufferedReader do arquivo pela Url.
	 * 
	 * @param url
	 * @throws IOException 
	 */
	public static BufferedReader getBufferedReaderFromUrl(URL url) throws IOException
	{
        URLConnection connection = url.openConnection();
        connection.setReadTimeout(5000);
		
        InputStreamReader input = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);
		
		return new BufferedReader(input);
	}
}
