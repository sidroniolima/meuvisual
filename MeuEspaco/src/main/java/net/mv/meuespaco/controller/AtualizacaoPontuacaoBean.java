package net.mv.meuespaco.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.service.PontuacaoService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@RequestScoped
public class AtualizacaoPontuacaoBean implements Serializable
{
	private static final long serialVersionUID = 6728298413619072247L;
	
	//private final Logger log = Logger.getLogger(AtualizacaoPontuacaoBean.class.getSimpleName());
	
	@Inject
	private PontuacaoService pontoSrvc;

	/**
	 * Atualiza a pontuação do arquivo de pontuação do Erp.
	 */
	public void atualiza()
	{
		try 
		{
			this.pontoSrvc.atualizaPontuacaoDoErp();
			FacesUtil.addSuccessMessage("Pontuação de clientes atualizada com sucesso.");
			
		} catch (IOException e) 
		{
			FacesUtil.addErrorMessage("Não foi possível importar a pontuação dos clientes. Tente novamente mais tarde.");
		}
	}
}
