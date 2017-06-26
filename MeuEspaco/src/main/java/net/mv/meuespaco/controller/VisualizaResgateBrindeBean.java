package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.service.ResgateBrindeService;
import net.mv.meuespaco.util.Encryptor;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Visualização do resgate de brinde.
 * 
 * @author sidronio
 * @created 26/06/2017
 */
@Named
@ViewScoped
public class VisualizaResgateBrindeBean implements Serializable 
{
	private static final String ERRO_NAO_ENCONTRADO = "Não foi possível carregar o resgate. Tente novamente.";
	private static final String MSG_FINALIZADA = "O resgate foi finalizado corretamente.";
	private static final String ERRO_NAO_PODE_FINALIZAR = "Não foi possível finalizar o resgate. Apenas novos resgates podem ser finalizados.";
	private static final String ERRO_NAO_PODE_SALVAR = "Não foi possível finalizar o resgate. %s.";
	
	private static final long serialVersionUID = -1779718702104638004L;

	@Inject
	private ResgateBrindeService resgateSrvc;
	
	private ResgateBrinde resgate;
	
	@Inject @Param(name="param-codigo")
	private String paramCodigo;
	
	@PostConstruct
	public void init()
	{
		Long codigo;
		
		if (null == paramCodigo)
		{
			geraErroResgateNaoEncontrado();
			return;
		}
		
		try
		{
			codigo = Long.valueOf(Encryptor.decrypt(this.paramCodigo));	
		} catch (NumberFormatException e) 
		{
			this.geraErroResgateNaoEncontrado();
			return;
		}
		
		resgate = resgateSrvc.buscarCompletaPeloCodigo(codigo);
		
		if (null == resgate)
		{
			geraErroResgateNaoEncontrado();
			return;
		}
	}
	
	/**
	 * Gera uma mensagem de erro se o resgate não for encontrado 
	 * ou não for passado uma codigo por parâmetro.
	 */
	private void geraErroResgateNaoEncontrado()
	{
		FacesUtil.addErrorMessage(ERRO_NAO_ENCONTRADO);
	}
	
	/**
	 * Finaliza o resgate após a separação dos itens.
	 */
	public void finaliza()
	{
		if (this.resgate.isPodeSerAtendido())
		{
			this.resgate.finaliza();
			
			try 
			{
				this.resgateSrvc.salva(resgate);
				FacesUtil.addSuccessMessage(MSG_FINALIZADA);
				
			} catch (RegraDeNegocioException e) 
			{
				FacesUtil.addErrorMessage(String.format(ERRO_NAO_PODE_SALVAR, e.getMessage()));
			}
		} else {
			FacesUtil.addErrorMessage(ERRO_NAO_PODE_FINALIZAR);
		}
			
	}

	public ResgateBrinde getResgate() {
		return resgate;
	}
}
