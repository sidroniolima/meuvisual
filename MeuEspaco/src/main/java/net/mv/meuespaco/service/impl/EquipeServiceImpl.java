package net.mv.meuespaco.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.EquipeDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Regiao;
import net.mv.meuespaco.model.integracao.Equipe;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.StatusCliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.EquipeService;
import net.mv.meuespaco.service.RegiaoService;
import net.mv.meuespaco.util.ParseCsv;

/**
 * Implementação Service para Equipe.
 * 
 * @author sidronio
 * @created 06/02/2017
 */
@Stateless
public class EquipeServiceImpl extends SimpleServiceLayerImpl<Equipe, Long> implements EquipeService, Serializable
{
	private static final long serialVersionUID = 2661628860345241427L;

	@Inject
	private EquipeDAO equipeDAO;
	
	@Inject
	private ExportPropertiesProxy propsProxy;

	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	private RegiaoService regiaoSrvc;

	@Override
	public void validaInsercao(Equipe entidade) throws RegraDeNegocioException 
	{
		
	}

	@Override
	public void validaExclusao(Equipe entidade) throws RegraDeNegocioException 
	{
		throw new RegraDeNegocioException("Não é possível excluir uma equipe.");
	}

	@Override
	public GenericDAO<Equipe, Long> getDAO() {
		return equipeDAO;
	}

	@Override
	public void atualizaEquipesDoERP() throws IOException 
	{
		this.removeRegistros();
		this.importaRegistros();
	}
	
	public void importaRegistros() throws IOException
	{
		URL urlImportacao = new URL(this.propsProxy.absolutePathToEquipes());
		
		BufferedReader reader = ParseCsv.getBufferedReaderFromUrl(urlImportacao);
		
		List<Optional<Equipe>> equipes = reader.lines()
			.map(line -> (line.split(";")))
			.map(this::build)
			.collect(Collectors.toList());
		
		equipes.parallelStream()
			.filter(Optional::isPresent)
			.forEach(e -> {
				
				try 
				{
					this.salva(e.get());
				} catch (RegraDeNegocioException ex) 
				{
					ex.printStackTrace();
				}
			});
	}
	
	@Override
	public Optional<Equipe> build(String ...params)
	{
		String codigoCliente = params[0];
		StatusCliente status = StatusCliente.valueOf(params[1]);
		String codigoEquipe = params[2];
		String nomeEquipe = params[3];
		String codigoRegiao = params[4];

		Optional<Cliente> optCliente = Optional.ofNullable(clienteSrvc.buscaPeloCodigoSiga(codigoCliente));
		
		if (!optCliente.isPresent())
			return Optional.empty();
		
		Optional<Regiao> optRegiao = Optional.ofNullable(regiaoSrvc.buscaPeloCodigoInterno(codigoRegiao));
		
		if (!optRegiao.isPresent())
			return Optional.empty();
		
		return Optional.of(new Equipe(optCliente.get(), status, codigoEquipe, nomeEquipe, optRegiao.get()));
	}

	@Override
	public void removeRegistros()
	{
		this.equipeDAO.removerTodosRegistros();
	}
	
	@Override
	public List<Equipe> listaEquipesDoCliente(Cliente cliente) 
	{
		return this.equipeDAO.listarEquipesPorCliente(cliente);
	}
}
