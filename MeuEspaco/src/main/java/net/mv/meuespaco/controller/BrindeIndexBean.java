package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.service.ResgateBrindeService;
import net.mv.meuespaco.util.Encryptor;

@Named
@ViewScoped
public class BrindeIndexBean implements Serializable 
{
	private static final long serialVersionUID = 6037118588738503053L;

	@Inject
	private ResgateBrindeService resgateSrvc;

	@Inject
	private ProdutoService produtoSrvc;
	
	@Inject @Param(name="resgate-criado")
	private String resgateCriado;
	
	private Long resgateCriadoCodigo;
		
	String messageResgateCriado = "Parabéns!!! Você acabou de resgatar %s estrelas em exclusivos produtos.";
	String messageFormatada;
	
	private List<ResgateBrinde> ultimosResgates;
	
	private List<Produto> destaques;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	@PostConstruct
	public void init() 
	{
		if (null != resgateCriado)
		{
			resgateCriadoCodigo = Long.valueOf(Encryptor.decrypt(resgateCriado));
			ResgateBrinde resgate = resgateSrvc.buscarComItensPeloCodigo(resgateCriadoCodigo);
			
			messageFormatada = String.format(this.messageResgateCriado, resgate.valor().intValue());
		}
		
		ultimosResgates = resgateSrvc.utlimosResgatesDoCliente(clienteLogado);
		destaques = produtoSrvc.listaBrindesEmDestaque(4);
	}
	
	/**
	 * Verifica se a mensagem deve ser exibida.
	 * 
	 * @return
	 */
	public boolean isShowMessage()
	{
		return null != messageFormatada;
	}
	
	public String getResgateCriado() 
	{
		return resgateCriado;
	}
	
	public String getMessageFormatada() 
	{
		return messageFormatada;
	}
	
	public List<ResgateBrinde> getUltimosResgates() 
	{
		return ultimosResgates;
	}
	
	public List<Produto> getDestaques() 
	{
		return destaques;
	}
}
