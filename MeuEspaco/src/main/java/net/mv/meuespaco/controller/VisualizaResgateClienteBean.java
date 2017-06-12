package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.service.ResgateBrindeService;
import net.mv.meuespaco.util.Encryptor;

/**
 * Visualização do Resgate do cliente.
 * 
 * @author sidronio
 * @create 08/07/2017
 */
@Named
@ViewScoped
public class VisualizaResgateClienteBean implements Serializable
{
	private static final long serialVersionUID = -1615301875391091136L;
	
	@Inject
	private ResgateBrindeService resgateSrvc;

	@Inject
	private ProdutoService produtoSrvc;
	
	@Inject @Param(name="param-codigo")
	private String paramCodigo;
	
	private ResgateBrinde resgate;

	@PostConstruct
	public void init()
	{
		if (null != paramCodigo)
		{
			Long codigo = Long.parseLong(Encryptor.decrypt(paramCodigo));
			
			this.resgate = this.resgateSrvc
					.buscarCompletaPeloCodigo(codigo);	
		}
	}

	public String buscaFoto(Object codigo)
	{
		return this.produtoSrvc.buscaProdutoComFotos((Long)codigo).buscaFotoPeloIndice(1);
	}
	
	public ResgateBrinde getResgate() 
	{
		return resgate;
	}
}
