package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.loja.Carrinho;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ItemCarrinho;
import net.mv.meuespaco.service.CarrinhoService;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.EscolhaService;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.ReservaProdutoService;

//@SessionScoped
@Stateless
public class CarrinhoServiceImpl implements CarrinhoService, Serializable {

	private static final long serialVersionUID = 1290368886466543948L;
	
	@Inject
	private ReservaProdutoService reservaProdutoSrvc;
	
	@Inject
	private EstoqueService estoqueSrvc;
	
	@Inject
	private EscolhaService pedidoSrvc;
	
	@Inject
	private ClienteService clienteSrvc;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;
	
	private List<ItemCarrinho> carrinho = new ArrayList<ItemCarrinho>();
	
	private Map<Subgrupo, BigDecimal> resumo = new HashMap<Subgrupo, BigDecimal>();

	@Override
	public void adicionaProduto(Produto produto, BigDecimal qtd, Grade gradeSelecionada) throws RegraDeNegocioException {
		
		if (null == produto) {
			throw new RegraDeNegocioException("O item não possui o produto. Selecione um produto.");
		}
		
		if (null == qtd || qtd.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("A quantidade do item deve ser maior ou igual a 1.");
		}
		
		if (null == gradeSelecionada) {
			throw new RegraDeNegocioException("A grade do produto deve ser especificada.");
		}
		
		BigDecimal qtdDisponivel = this.qtdDisponivelDoProduto(produto, gradeSelecionada);
		
		if (qtdDisponivel.compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("Não há produtos disponíveis desta grade.");
		}
		
		if (qtd.compareTo(qtdDisponivel) > 0) {
			
			throw new RegraDeNegocioException(
					String.format("Para a grade seleciona há apenas %s produtos disponíveis.", 
							qtdDisponivel.setScale(0)));
		}
		
		if (produto.isDescontavel()) {
			
			double saldoParaEscolha = clienteSrvc.qtdDisponivelParaEscolha() - this.qtdDeItensDescontaveis();
			
			if (qtd.compareTo(new BigDecimal(saldoParaEscolha)) > 0) {
				
				throw new RegraDeNegocioException(
						String.format("O saldo para escolha de peças foi ultrapassado. O saldo é de %s.", 
								(int) saldoParaEscolha));
			}
		}
		
		this.reservaProdutoSrvc.adicionaReserva(produto, gradeSelecionada, qtd);
		
		ItemCarrinho novoItem = new ItemCarrinho(produto, qtd, gradeSelecionada);
		
		Optional<ItemCarrinho> itemExistente = 
				carrinho.stream().filter(i -> i.equals(novoItem)).findFirst(); 
		
		if (itemExistente.isPresent()) {
			
			itemExistente.get().atualizaQtd(novoItem.getQtd());
			
			resumo.put(itemExistente.get().getProduto().getSubgrupo(), itemExistente.get().getQtd());
			
		} else {
			
			carrinho.add(novoItem);
			resumo.put(novoItem.getProduto().getSubgrupo(), novoItem.getQtd());
		}
		
	}

	@Override
	public void removeItem(ItemCarrinho item) {
		
		if (null != item) 
		{
			carrinho.remove(item);
			resumo.remove(item.getProduto().getSubgrupo(), item.getQtd());
			this.reservaProdutoSrvc.removeReserva(item.getProduto(), item.getGrade(), item.getQtd());
			
		} 
	}

	@Override
	public void alteraQtd(ItemCarrinho item, BigDecimal novaQtd) {
		item.setQtd(novaQtd);
	}

	@Override
	public void finalizaEscolha(Usuario usuario) throws RegraDeNegocioException {

		pedidoSrvc.criaEscolhaPeloCarrinho(this.getCarrinho(), clienteLogado);
		this.esvazia();
	}
	
	@Override
	public void finalizaEscolha(Carrinho carrinho, Cliente cliente) throws RegraDeNegocioException 
	{
		pedidoSrvc.criaEscolhaPeloCarrinho(carrinho, cliente);
	}

	@Override
	public double qtdDeItens() {
		return carrinho
				.stream()
				.mapToDouble(i -> i.getQtd().doubleValue()).sum();
	}
	
	@Override
	public double qtdDeItensDescontaveis() throws RegraDeNegocioException {
		double qtdDescontavelNoCarrinho = 0;
		
		for (ItemCarrinho i : carrinho) {
			
			if (i.getProduto().isDescontavel()) {
				qtdDescontavelNoCarrinho += i.getQtd().doubleValue();
			}
			
		}
		return qtdDescontavelNoCarrinho;
	}
	
	@Override
	public void esvazia() {
		
		for (ItemCarrinho item : carrinho) {
			
			this.reservaProdutoSrvc.removeReserva(item.getProduto(), item.getGrade(), item.getQtd());
		}
		
		carrinho.clear();
		resumo = new HashMap<Subgrupo, BigDecimal>();
	}

	/**
	 * @return the carrinho
	 */
	@Override
	public List<ItemCarrinho> getCarrinho() {
		return carrinho;
	}
	
	@Override
	public BigDecimal qtdDisponivelDoProduto(Produto produto, Grade grade) {
		BigDecimal qtd = estoqueSrvc.qtdDisponivelParaVenda(produto, grade)
				.subtract(reservaProdutoSrvc.qtdReservadaDoProduto(produto, grade));
		
		return qtd;
	}

	@Override
	public boolean temQtdDisponivelDoProduto(Produto produto, Grade grade) {
		return this.qtdDisponivelDoProduto(produto, grade).compareTo(BigDecimal.ZERO) > 0;
	}
	
	/**
	 * @return the resumo
	 */
	@Override
	public Map<Subgrupo, BigDecimal> getResumo() {
		return resumo;
	}
	
}
