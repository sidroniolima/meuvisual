package net.mv.meuespaco.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.CreditoDAO;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.integracao.ClasseCredito;
import net.mv.meuespaco.model.integracao.Credito;
import net.mv.meuespaco.model.integracao.ListagemCreditos;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.CreditoService;
import net.mv.meuespaco.util.ParseCsv;

@Stateless
public class CreditoServiceImpl extends SimpleServiceLayerImpl<Credito, Long> implements CreditoService, Serializable
{
	private static final long serialVersionUID = -8574670731232468030L;

	@Inject
	private CreditoDAO creditoDAO;
	
	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	private ExportPropertiesProxy props;

	@Override
	public ListagemCreditos listagemDeCreditoDoClientePorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim) 
	{
		List<Credito> creditos = creditoDAO.buscaCreditosDoClientePorPeriodo(cliente, inicio, fim);
		
		return new ListagemCreditos(creditos);
	}

	@Override
	public void atualizaCreditosDoERP() throws IOException 
	{
		this.removeRegistros();
		
		this.adicionaNovosRegistros();
	}
	
	/**
	 * Adiciona os registros de creditos importados do ERP.
	 * 
	 * @throws IOException
	 */
	private void adicionaNovosRegistros() throws IOException 
	{
		BufferedReader reader = 
				ParseCsv.getBufferedReaderFromUrl(new URL(props.absolutePathToCreditos()));
		
		List<Optional<Credito>> creditos = reader
			.lines()
			.map(line -> (line.split(";")))
			.map(this::build)
			.collect(Collectors.toList());
		
		creditos.parallelStream().filter(Optional::isPresent).forEach(c -> {
			try 
			{
				this.salva(c.get());
			} catch (RegraDeNegocioException e) 
			{
				e.printStackTrace();
			}
		});
	}
	
	/**
	 * Remove os registros da tabela de crédito para 
	 * evitar a duplição e manter os dados atualizados.
	 */
	private void removeRegistros() 
	{
		this.creditoDAO.removerRegistros();
	}

	/**
	 * Cria um Credito dados argumentos String, utilizado 
	 * na integração com o ERP.
	 * 
	 * @param strings
	 * @return
	 */
	private Optional<Credito> build(String[] strings)
	{
		Optional<Cliente> optCliente = Optional.ofNullable(clienteSrvc.buscaPeloCodigoSiga(strings[0]));
		
		if (!optCliente.isPresent())
			return Optional.empty();
		
		double valor = Double.valueOf(strings[1]);
		String nome = strings[2];
		ClasseCredito classe = ClasseCredito.valueOf(strings[3]);
		String historico = strings[4];
		LocalDate baixa = LocalDate.parse(strings[5], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
		return Optional.of(new Credito(optCliente.get(), valor, nome, classe, historico, baixa));
	}

	@Override
	public void validaInsercao(Credito entidade) throws RegraDeNegocioException 
	{
		return;
	}

	@Override
	public void validaExclusao(Credito entidade) throws RegraDeNegocioException 
	{
		return;
	}

	@Override
	public GenericDAO<Credito, Long> getDAO() {
		return this.creditoDAO;
	}

}
