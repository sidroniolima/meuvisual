package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import net.mv.meuespaco.annotations.CarrinhoConsignadoBeanAnnotation;
import net.mv.meuespaco.exception.QtdInsuficienteParaEscolhaException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.exception.ValorInsuficienteParaEscolhaException;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.util.FacesUtil;
import net.mv.meuespaco.util.IConstants;

/**
 * Implementação da Pesquisa de Produto para consignados.
 * 
 * @author sidronio
 * @created 19/01/2017
 */
@Named
@ViewScoped
public class PesquisaProdutoConsignadoBean extends PesquisaProdutoAbstractBean implements Serializable {

	private static final long serialVersionUID = -4901213277675439586L;
	
	@Inject
	private ClienteService clienteSrvc;

	@Inject
	@CarrinhoConsignadoBeanAnnotation
	private CarrinhoAbstractBean carrinho;
	
	private String msgModal;

	@Override
	public Finalidade getFinalidade() {
		return Finalidade.CONSIGNADO;
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

		Produto produto = this.getProdutoSrvc().buscaPeloCodigoComRelacionamentos(new Long(paramProduto));
		
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

	public String getMsgModal() {
		return msgModal;
	}
}
