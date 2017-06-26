package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroResgateBrinde;
import net.mv.meuespaco.controller.filtro.IFiltroPesquisaAcao;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.model.loja.StatusEscolha;
import net.mv.meuespaco.service.ResgateBrindeService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller de pesquisa do resgate.
 * 
 * @author sidronio
 * @create 23/03/2017
 */
@Named
@ViewScoped
public class PesquisaResgateBean extends PesquisaSingle implements Serializable 
{
	private static final long serialVersionUID = 2016354830391058456L;
	
	private Logger log = Logger.getLogger(PesquisaResgateBean.class.getSimpleName());
	
	private final String ERROR_EXCLUSAO = "Não foi possível exluir o Resgate %s. %s";
	private final String SUCCESS_EXCLUSAO = "O resgate %s foi excluído.";
	private final String ERROR_SELECAO = "Nenhum resgate foi selecionado. Tente novamente.";
	
	@Inject
	private ResgateBrindeService resgateSrvc;
	
	private List<ResgateBrinde> resgates;
	
	private ResgateBrinde resgateSelecionado;
	
	private IFiltroPesquisaAcao filtro;
	
	private StatusEscolha [] status;
	
	@Override
	@PostConstruct
	void init() 
	{
		filtro = new FiltroResgateBrinde();
		status = StatusEscolha.values();

		if (null == resgates)
		{
			this.listarComPaginacao();	
		}
	}

	@Override
	public void excluir() 
	{
		if (null == this.resgateSelecionado)
		{
			FacesUtil.addErrorMessage(ERROR_SELECAO);
			log.warning(ERROR_SELECAO);
		}
		
		try 
		{
			this.resgateSrvc.exclui(this.resgateSelecionado.getCodigo());
			FacesUtil.addSuccessMessage(this.getMensagemDeExclusaoOk());
			
			this.resgates.remove(resgateSelecionado);
		
		} catch (RegraDeNegocioException | DeleteException e) 
		{
			String msg = this.getMensagemDeErroDeExclusao(e.getMessage());
			FacesUtil.addErrorMessage(msg);
			log.warning(msg);
		}
	}

	@Override
	public void listarComPaginacao() 
	{
		if (filtro.isPreenchido())
		{
			this.filtrar();
		} else 
		{
			resgates = resgateSrvc.listarComPaginacao(
					this.getPaginator(), 
					Arrays.asList("codigo"), 
					Arrays.asList("itens","cliente","cliente.regiao"), 
					null);
		}
	}

	/**
	 * Filtra os registros de acordo com os dados de pesquisa. 
	 */
	private void filtrar() 
	{
		resgates = this.resgateSrvc.filtraPelaPesquisa(filtro, getPaginator());
	}
	
	@Override
	String getMensagemDeExclusaoOk() 
	{
		return String.format(SUCCESS_EXCLUSAO, this.descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) 
	{
		return String.format(ERROR_EXCLUSAO, this.descricaoDoRegistro(), msgError);
	}

	@Override
	public String descricaoDoRegistro() 
	{
		return this.resgateSelecionado.codigoFormatado();
	}

	public ResgateBrinde getResgateSelecionado() {
		return resgateSelecionado;
	}
	public void setResgateSelecionado(ResgateBrinde resgateSelecionado) {
		this.resgateSelecionado = resgateSelecionado;
	}

	public List<ResgateBrinde> getResgates() {
		return resgates;
	}
	
	public IFiltroPesquisaAcao getFiltro() {
		return filtro;
	}
	public void setFiltro(IFiltroPesquisaAcao filtro) {
		this.filtro = filtro;
	}
	
	public StatusEscolha[] getStatus() {
		return status;
	}
}
