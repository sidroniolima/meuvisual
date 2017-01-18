package net.mv.meuespaco.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaVenda;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.VendaDAO;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.IntegracaoException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.cielo.CieloException;
import net.mv.meuespaco.model.cielo.Pagamento;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.CarrinhoVenda;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cupom;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.model.loja.ItemVenda;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.IntegracaoCieloService;
import net.mv.meuespaco.service.VendaService;
import net.mv.meuespaco.util.Paginator;

/**
 * Implementação Service para a entidade Venda.
 * 
 * @author Sidronio
 * @created 23/08/2016
 */
@Stateless
public class VendaServiceImpl extends SimpleServiceLayerImpl<Venda, Long> implements VendaService{

	@Inject
	private VendaDAO vendaDAO;
	
	@Inject
	private EstoqueService estoqueSrvc;
	
	@Inject
	private IntegracaoCieloService cieloSrvc;
	
	public VendaServiceImpl() {	}
	
	public VendaServiceImpl(VendaDAO vendaDAO, EstoqueService estoqueSrvc, IntegracaoCieloService cieloSrvc) {
		this.vendaDAO = vendaDAO;
		this.estoqueSrvc = estoqueSrvc;
		this.cieloSrvc = cieloSrvc;
	}

	@Override
	public void validaInsercao(Venda venda) throws RegraDeNegocioException {
		venda.valida();
	}

	@Override
	public void validaExclusao(Venda venda) throws RegraDeNegocioException {
		venda.valida();
	}
	
	@Override
	public List<Venda> vendasDoCliente(Cliente cliente) {
		return vendaDAO.listarVendasPorCliente(cliente);
	}
	
	@Override
	public Venda criaVendaPeloCarrinho(Carrinho carrinho, Cliente cliente) throws RegraDeNegocioException 
	{
		
		if (carrinho.isVazio())
		{
			throw new RegraDeNegocioException("Mão foi possível criar a venda. É necessário pelo menos um item no carrinho.");
		}
		
		Venda venda = new Venda(cliente);
		venda.setDescontoVenda(carrinho.getDesconto());

		for (ItemCarrinho item : carrinho.getItens()) {
			venda.addItem(item.getProduto(), item.getQtd(), item.getGrade());
		}
		
		return venda;
	}
	
	@Override
	public void exclui(Long id) throws RegraDeNegocioException, DeleteException 
	{
		Venda vendaTemp = this.buscaPeloCodigo(id);
		
		if (null == vendaTemp)
		{
			throw new RegraDeNegocioException("Não foi possível localizar a venda pelo código.");
		}
		
		super.exclui(id);
		this.estoqueSrvc.estornaVenda(vendaTemp.getItens());
	}

	@Override
	public GenericDAO getDAO() {
		return vendaDAO;
	}

	@Override
	public Venda buscaCompletaPeloCodigo(Long codigo) 
	{
		//return vendaDAO.buscarPeloCodigo(codigo);
		return vendaDAO.buscarCompleta(codigo);
		//TODO: implementar a entidade para foto para evitar 
		//duplicação nos dados dos itens que necessitam da foto.
	}
	
	@Override
	public List<ItemVenda> buscaItensCompleto(Long codigo) {
		return vendaDAO.buscarItensCompletos(codigo);
	}
	
	@Override
	public List<Venda> filtraPelaPesquisa(FiltroPesquisaVenda filtro, Paginator paginator) {
		return vendaDAO.filtrarPeloModoEspecifico(filtro, paginator);
	}

	@Override
	public Venda criaVendaPeloCarrinho(CarrinhoVenda carrinho, Cliente cliente, Cupom cupom)
			throws RegraDeNegocioException 
	{
		return this.criaVendaPeloCarrinho(carrinho, cliente);
	}
	
	@Override
	public Venda buscaUtilmaDoCliente(Cliente cliente) 
	{
		Venda venda = this.vendaDAO.buscarUltimaDoCliente(cliente);
		return venda;
	}

	@Override
	public void registraPagamento(Venda venda, String paymentId, LocalDateTime horario)
			throws RegraDeNegocioException 
	{
		venda.registraPagamento(paymentId, horario);
		this.salva(venda);
		
	}
	
	@Override
	public void cancelaVenda(Venda venda) throws RegraDeNegocioException, IntegracaoException, CieloException 
	{
		if (venda.isPaga())
		{
			this.cieloSrvc.cancelaCompra(venda.getPaymentId(), venda.valorComDesconto());	
		}
		
		venda.cancela();
		this.salva(venda);
		estoqueSrvc.estornaVenda(venda.getItens());
	}
	
	@Override
	public Venda buscaPeloPaymentId(String paymentId) 
	{
		return this.vendaDAO.buscarPeloPaymentId(paymentId); 
	}
	
	@Override
	public void registraPagamento(Venda venda, Pagamento pagamento) throws CieloException, IntegracaoException, RegraDeNegocioException 
	{
		Pagamento resposta = this.cieloSrvc.efetuaPagamento(pagamento);
		
		if (!resposta.isAutorizado())
		{
			throw new RegraDeNegocioException("Infelizmente seu pagamento não foi aprovado. Tente novamente.");
		}
				
		venda.registraPagamento(resposta.paymentId(), resposta.horarioDoPagamento());
		this.salva(venda);
		this.estoqueSrvc.movimentaVenda(venda.getItens());
	}
	
	@Override
	public Pagamento consultaPagamento(Venda venda) throws CieloException, IntegracaoException 
	{
		return this.cieloSrvc.consultaPagamento(venda.getPaymentId());
	}

	@Override
	public Venda criaVendaDoNada() {
		// TODO Auto-generated method stub
		return null;
	}
}
