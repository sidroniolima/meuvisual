package net.mv.meuespaco.dao;

import java.math.BigDecimal;
import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroEntradaProdutos;
import net.mv.meuespaco.controller.filtro.FiltroPesquisaMovimento;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.consulta.EstoqueDoProdutoConsulta;
import net.mv.meuespaco.model.consulta.MovimentoPorComposicaoSubgrupo;
import net.mv.meuespaco.model.estoque.Almoxarifado;
import net.mv.meuespaco.model.estoque.Movimento;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface para DAO de Estoque representando as movimentações.
 * 
 * @author Sidronio
 * 15/12/2015
 */
public interface EstoqueDAO extends GenericDAO<Movimento, Long>{

	/**
	 * Calcula o saldo entre entradas e saídas do produto para a 
	 * grade e almoxarifados especificados.
	 * 
	 * @param produto Produto para pesquisa.
	 * @param grade Grade para pesquisa.
	 * @return Saldo do produto e grade no almoxarifado especificado.
	 */
	public BigDecimal qtdEmEstoqueDoProdutoEGrade(Almoxarifado alm, Produto produto, Grade grade);
	
	/**
	 * Calcula o saldo entre entradas e saídas do produto 
	 * para o almoxarifado especificado.
	 * 
	 * @param produto Produto para pesquisa.
	 * @return Saldo do produto no almoxarifado especificado.
	 */
	public BigDecimal qtdEmEstoqueDoProduto(Almoxarifado alm, Produto produto);
	
	/**
	 * Calcula o saldo do produto por almoxarifado e grade.
	 * 
	 * @param produto Produto para consulta.
	 * @return Quantidade do produto agrupado por almoxarifado e grade.
	 */
	public List<EstoqueDoProdutoConsulta> estoqueDoProduto(Produto produto);
	
	/**
	 * Lista as grades do produto disponvíeis para escolha, isto é, com saldo 
	 * positivo.
	 * 
	 * @param produto
	 * @param almoxarifado
	 * @return
	 */
	public List<Grade> gradesComSaldoPositivo(Produto produto, Almoxarifado almoxarifado);

	/**
	 * Lista movimentações de acordo com o filtro e devolve-as 
	 * paginada.
	 * 
	 * @param filtro
	 * @param paginator
	 */
	public List<Movimento> listarPeloFiltro(FiltroPesquisaMovimento filtro, Paginator paginator);

	public List<MovimentoPorComposicaoSubgrupo> agruparMovimentacaoPeloFiltro(FiltroEntradaProdutos filtro, Paginator paginator);
	
}
