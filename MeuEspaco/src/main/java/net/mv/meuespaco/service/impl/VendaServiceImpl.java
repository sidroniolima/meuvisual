package net.mv.meuespaco.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaVenda;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.VendaDAO;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.CarrinhoVenda;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.Cupom;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.model.loja.ItemVenda;
import net.mv.meuespaco.model.loja.Venda;
import net.mv.meuespaco.service.EstoqueService;
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
	
	public VendaServiceImpl() {	}
	
	public VendaServiceImpl(VendaDAO vendaDAO, EstoqueService estoqueSrvc) {
		this.vendaDAO = vendaDAO;
		this.estoqueSrvc = estoqueSrvc;
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
		Venda venda = new Venda(cliente);
		venda.setDescontoVenda(carrinho.getDesconto());

		for (ItemCarrinho item : carrinho.getItens()) {
			venda.addItem(item.getProduto(), item.getQtd(), item.getGrade());
		}
		
		Venda salva = this.salva(venda);
		this.estoqueSrvc.movimentaVenda(venda.getItens());
		return salva;
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
	public void registraPagamento(Venda venda, String paymentId) throws RegraDeNegocioException 
	{
		venda.registraPagamento(paymentId);
		this.salva(venda);
	}
}
