package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import net.mv.meuespaco.annotations.CarrinhoConsignadoBeanAnnotation;
import net.mv.meuespaco.annotations.EstadoDeNavegacaoConsignadoAnnotation;
import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.exception.QtdInsuficienteParaEscolhaException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.exception.ValorInsuficienteParaEscolhaException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.EstadoDeNavegacao;
import net.mv.meuespaco.util.FacesUtil;
import net.mv.meuespaco.util.FiltroListaProdutoConsignadoAnnotation;
import net.mv.meuespaco.util.IConstants;

/**
 * Implementação da listagem de produtos para consignados.
 * 
 * @author Sidronio
 * @created 10/08/2016
 */
@Named
@ViewScoped
public class ListaProdutosConsignadosBean extends ListaProdutosAbstractBean implements Serializable{

	private static final long serialVersionUID = -1044183808309706397L;
	
	@Inject
	@FiltroListaProdutoConsignadoAnnotation
	private FiltroListaProduto filtro;
	
	@Inject
	@EstadoDeNavegacaoConsignadoAnnotation
	private EstadoDeNavegacao estadoDeNavegacao;

	@Inject
	private ProdutoService produtoSrvc;
	
	@Inject
	@CarrinhoConsignadoBeanAnnotation
	private CarrinhoAbstractBean carrinho;
	
	@Inject
	private ClienteService clienteSrvc;
	
	private Produto produtoSelecionado;
	
	private String msgModal;

	/**
	 * Lista os registros de produtos para consignação de forma paginada.
	 */
	public void listarComPaginacao() {
			
		super.setProdutos(
				super.getProdutoService().listaProdutosPelaNavegacao(
						super.getDep(), 
						super.getGrupo(), 
						super.getSubgrupo(), 
						this.getFiltro(), 
						super.getPaginator()));
		
	}	
	
	@Override
	public boolean verificaDisponibilidadeDaEscolha()
	{
		try 
		{
			this.clienteSrvc.verificaSeOUsuarioLogadoPodeEscolher();
			return true;
			
		} catch (RegraDeNegocioException e) 
		{ 
			return false;
		}
	}
	
	/**
	 * Adiciona o produto selecionado ao carrinho, isto é, 
	 * o   OneClick.
	 */
	public void addToChart()
	{
		this.setHabilitaEscolha(true);
		msgModal = "";
		
		String paramProduto = Faces.getRequestParameter("produto");

		Produto produto = this.produtoSrvc.buscaPeloCodigoComRelacionamentos(new Long(paramProduto));
		
		try 
		{
			carrinho.adicionaProduto(produto, BigDecimal.ONE, produto.valor(), produto.gradeUnica());
		}
		catch (QtdInsuficienteParaEscolhaException e)
		{
			msgModal = IConstants.WARN_ATINGIU_QTD;
			this.setHabilitaEscolha(false);
		}
		catch (ValorInsuficienteParaEscolhaException e)
		{
			BigDecimal saldo = ((CarrinhoConsignadoBean) this.carrinho).saldoValor();
			
			String saldoFormatado = NumberFormat.getCurrencyInstance().format(saldo.doubleValue());
			msgModal = String.format(IConstants.WARN_ATINGIU_VALOR, saldoFormatado);
			
			this.setHabilitaEscolha(false);
		}
		catch (RegraDeNegocioException e) 
		{
			FacesUtil.addErrorMessage("Não foi possível adicionar o produto ao carrinho.");
		}
	}
	
	@Override
	public EstadoDeNavegacao getEstadoDeNavegacao() 
	{
		return this.estadoDeNavegacao;
	}
	
	@Override
	public FiltroListaProduto getFiltro() {
		return filtro;
	}
	
	@Override
	public void setFiltro(FiltroListaProduto filtro) {
		this.filtro = filtro;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}
	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}
	
	public String getMsgModal() 
	{
		return msgModal;
	}
	
	public ClienteService getClienteSrvc()
	{
		return this.clienteSrvc;
	}
	
	@Override
	public boolean isHabilitaEscolha() 
	{
		return super.isHabilitaEscolha();
	}
}
