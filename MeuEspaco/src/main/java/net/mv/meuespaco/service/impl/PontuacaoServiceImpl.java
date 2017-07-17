package net.mv.meuespaco.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.controller.filtro.FiltroPontuacao;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.PontuacaoDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.integracao.Pontuacao;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.PontuacaoService;
import net.mv.meuespaco.service.ResgateBrindeService;
import net.mv.meuespaco.util.Paginator;
import net.mv.meuespaco.util.ParseCsv;

/**
 * Implementação da camanda Service da entidade Pontuacao. 
 * 
 * @author sidronio
 * @created 25/04/2017
 */
@Stateless
public class PontuacaoServiceImpl extends SimpleServiceLayerImpl<Pontuacao, Long> implements PontuacaoService, Serializable
{
	private static final long serialVersionUID = 2918436079756822451L;
	
	private final Logger log = Logger.getLogger(PontuacaoServiceImpl.class.getSimpleName());

	@Inject
	private PontuacaoDAO pontoDAO;
	
	@Inject
	private ResgateBrindeService resgateSrvc;
	
	@Inject
	private PropertiesLoad props;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	@Override
	public void validaInsercao(Pontuacao entidade) throws RegraDeNegocioException 
	{
		
	}

	@Override
	public void validaExclusao(Pontuacao entidade) throws RegraDeNegocioException 
	{
		
	}

	@Override
	public Pontuacao buscaPeloCodigo(Long codigo) 
	{
		return this.pontoDAO.buscarPeloCodigo(codigo);
	}

	@Override
	public List<Pontuacao> pontuacaoDoClienteLogado(Paginator paginator) 
	{
		return this.pontoDAO.buscarPeloCodigoCliente(this.clienteLogado.getCodigoSiga(), paginator);
	}

	@Override
	public Long pontosAcumuladosDoClienteLogado() 
	{
		Long pontos = this.pontoDAO
				.buscarSomaDosPontosAcumuladosPeloCodigoCliente(this.clienteLogado.getCodigoSiga());
		
		return null == pontos ? 0L : pontos;
	}

	@Override
	public void atualizaPontuacaoDoErp() throws IOException 
	{
		try
		{
			this.removeRegistros();
			this.adicionaRegistros();
		} catch (Exception e) 
		{
			log.log(Level.SEVERE, "A atualização da pontuação não foi realizada.", e);
		}
	}
	
	/**
	 * Remove os registros para a atualização.
	 */
	private void removeRegistros()
	{
		this.pontoDAO.removerRegistros();
		log.info("Registros de pontuação removidos com sucesso.");
	}
	
	/**
	 * Adiciona os registros importados no arquivo gerado do Erp.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private void adicionaRegistros() throws IOException
	{
		BufferedReader reader = 
				ParseCsv.getBufferedReaderFromUrl(new URL(this.props.getProperty("pontuacao-path")));
		
		List<Optional<Pontuacao>> pontuacao = 
				reader.lines()
					.map(line -> line.split(";"))
					.map(this::build)
					.collect(Collectors.toList());
		
		pontuacao
			.parallelStream()
			.filter(Optional::isPresent)
			.forEach(p -> 
				{
					try 
					{
						this.salva(p.get());
						
					} catch (RegraDeNegocioException e) 
					{
						log.log(
								Level.WARNING, 
								String.format("Não foi possível salvar a pontuação %s.", p.get().getCodigo()), e);
					} catch (Exception e)
					{
						log.log(
								Level.SEVERE, 
								String.format("Não foi possível salvar a pontuação %s.", p.get().getCodigo()), e);
						
					}
				});
		
		log.info("Pontuação atualizada com sucesso.");
	}
	
	/**
	 * Cria a pontuação de acordo com os campos.
	 * 
	 * @param campos
	 * @return
	 */
	private Optional<Pontuacao> build(String ...campos)
	{
		return Optional.of(new Pontuacao(
				Long.valueOf(campos[0]), 
				LocalDate.parse(campos[1], DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
				campos[2], 
				Integer.valueOf(campos[3]), 
				campos[4], 
				campos[5]));
	}
	
	@Override
	public List<Pontuacao> filtraPeloModoEspecifico(FiltroPontuacao filtro, Paginator paginator) 
	{
		return this.pontoDAO.filtrarPeloModoEspecifico(filtro, paginator);
	}
	
	@Override
	public Long saldoDePontosDoClienteLogado() 
	{
		Long pontosAcumulados = this.pontosAcumuladosDoClienteLogado();
		Long pontosResgatados = this.pontosResgatadosDoClienteLogado();
		
		return pontosAcumulados - pontosResgatados;
	}
	
	@Override
	public Long pontosResgatadosDoClienteLogado() 
	{
		return this.resgateSrvc.totalDePontosResgatadosDoClienteLogado();
	}
	
	@Override
	public GenericDAO<Pontuacao,Long> getDAO() 
	{
		return this.pontoDAO;
	}
}
