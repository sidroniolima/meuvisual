package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.logging.Logger;

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
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.GradeTamanho;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.service.impl.EscolhaServiceImpl;
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
	private final Logger log = Logger.getLogger(EscolhaServiceImpl.class.getSimpleName());
	
	private static final String MSG_NAO_ADD_PRODUTO = "Add to Chart: Não foi possível add o produto %s"
			+ " com grade %s.";
	
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
	public void listarComPaginacaoEGrade() 
	{
		super.setProdutos(
				super.getProdutoService().listaProdutosPelaNavegacaoPorGrade(
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
		
		Grade grade = null;
		
		try 
		{
			if (produto.getTipoGrade().equals(TipoGrade.UNICA))
			{
				grade = produto.gradeUnica();
			} 
			
			if (this.isTamanhoSelecionado())
			{
				grade = this.produtoSrvc.gradeDoProduto(produto, new GradeTamanho(this.getFiltro().getTamanho()));
			}
			
			if (null == grade)
			{
				FacesUtil.addErrorMessage("Não foi possível adicionar o produto ao carrinho.");
				log.warning(String.format(MSG_NAO_ADD_PRODUTO, produto.getCodigoInterno(), grade));
			} else
			{
				carrinho.adicionaProduto(produto, BigDecimal.ONE, produto.valor(), grade);
			}
			
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
	public boolean permiteOneClick() 
	{
		return this.isTamanhoSelecionado();
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
