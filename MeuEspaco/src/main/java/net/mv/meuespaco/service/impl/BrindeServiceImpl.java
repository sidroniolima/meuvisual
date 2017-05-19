package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.ProdutoDAO;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.service.BrindeService;
import net.mv.meuespaco.util.IConstants;
import net.mv.meuespaco.util.Paginator;

/**
 * Implementação da camada Service de Brinde.
 * 
 * @author sidronio
 * @created 11/05/2017
 *
 */
@Stateless
public class BrindeServiceImpl implements BrindeService, Serializable 
{
	private static final long serialVersionUID = -4490572607678213461L;

	@Inject
	private ProdutoDAO produtoDAO;
	
	@Override
	public List<Produto> pesquisaDiversa(String pesquisa, Paginator paginator) 
	{
		return this.produtoDAO.filtrarProdutosPorFinalidadeEDescricoes(pesquisa, Finalidade.BRINDE, paginator);
	}

	@Override
	public List<Produto> filtraPeloValor(BigDecimal min, BigDecimal max, Paginator paginator) 
	{
		return this.produtoDAO.filtrarProdutosPorFinalidadeEValor(Finalidade.BRINDE, min, max, paginator);
	}

	@Override
	public List<Produto> listaPeloSubgrupo(Subgrupo subgrupo, Paginator paginator) 
	{
		return this.produtoDAO.filtrarProdutosPorFinalidadeESubgrupo(Finalidade.BRINDE, subgrupo, paginator);
	}
}
