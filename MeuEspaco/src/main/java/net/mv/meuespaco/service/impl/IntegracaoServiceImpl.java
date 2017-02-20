package net.mv.meuespaco.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.model.integracao.ClientesDoErp;
import net.mv.meuespaco.service.IntegracaoService;
import net.mv.meuespaco.util.ParseFromCsv;

/**
 * Implementação para o serviço de integração, encapsulamento este 
 * comportamento nesta interface.
 * 
 * @author sidronio
 * @created 16/02/17
 */
@Stateless
public class IntegracaoServiceImpl implements IntegracaoService {

	@Inject
	private ExportPropertiesProxy props;
	
	@Override
	public List<ClientesDoErp> listaClientesDoErp() throws MalformedURLException, IOException 
	{
		BufferedReader reader = ParseFromCsv.getBufferedReaderFromUrl(new URL(props.absolutePathToClientes()));
		
		List<ClientesDoErp> registrosErp = reader.lines()
			.map(l -> l.split(";"))
			.map(ClientesDoErp::build)
			.collect(Collectors.toList());
	
		return registrosErp;
	}

}
