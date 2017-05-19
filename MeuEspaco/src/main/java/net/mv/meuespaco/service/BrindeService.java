package net.mv.meuespaco.service;

import java.math.BigDecimal;
import java.util.List;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.util.Paginator;

/**
 * Abstração da camada Service de Brinde. 
 * 
 * @author sidronio
 * @created 11/05/2017
 */
public interface BrindeService
{
	/**
	 * Filtra os produtos de finalidade Brinde que tiverem a descrição, 
	 * detalhes ou código de acordo com o termo da pesquisa.
	 * 
	 * @param pesquisa termo da pesquisa
	 * @return lista de produtos que atendem a pesquisa.
	 */
	public List<Produto> pesquisaDiversa(String pesquisa, Paginator paginator);
	
	/**
	 * Filtra os produtos pelo valor mínimo e máximo.
	 * 
	 * @param min valor mínimo
	 * @param max valor máximo
	 * @return produtos que atendem o filtro de valor.
	 */
	public List<Produto> filtraPeloValor(BigDecimal min, BigDecimal max, Paginator paginator);
	
	/**
	 * Lista os produtos com finalidade brinde pelo subgrupo.
	 * 
	 * @param subgrupo opção selecionada.
	 * @return brindes do subgrupo.
	 */
	public List<Produto> listaPeloSubgrupo(Subgrupo subgrupo, Paginator paginator);
} 
