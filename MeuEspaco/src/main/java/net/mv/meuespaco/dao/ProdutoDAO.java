package net.mv.meuespaco.dao;

import java.math.BigDecimal;
import java.util.List;

import net.mv.meuespaco.controller.ProdutosEQtdPorSubgrupo;
import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.controller.filtro.FiltroProduto;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.consulta.ReferenciaProdutoComQtd;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface da camada DAO para a Entidade Produto.
 * 
 * @author Sidronio
 * 17/11/2015
 */
public interface ProdutoDAO extends GenericDAO<Produto, Long>{

	/**
	 * Filtra os registros de Produto pelo departamento e
	 * grupo.
	 * 
	 * @param dep Departamento para filtro.
	 * @param grupo Grupo para filtro.
	 * @param first Primeiro registro, utilizado na paginação.
	 * @param qtd Quantidade de registros, utilizado na paginação.
	 * @return Lista de produtos do departamento e grupo.
	 */
	List<Produto> fitrarPeloDepartamentoEGrupo(Departamento dep, Grupo grupo, int first, int qtd);

	/**
	 * Filtra os registros de Produto pelo departamento e
	 * subgrupo.
	 * 
	 * @param dep Departamento para filtro.
	 * @param subgrupo Subgrupo para o filtro.
	 * @param first Primeiro registro, utilizado na paginação.
	 * @param qtd Quantidade de registros, utilizado na paginação.
	 * 
	 * @return Lista de produtos do departamento e subgrupo.
	 */
	List<Produto> fitrarPeloDepartamentoESubgrupo(Departamento dep, Subgrupo subgrupo, int first, int qtd);

	/**
	 * Lista os últimos x (qtd) de produtos cadastrados pela 
	 * data de cadastro.
	 * 
	 * @param qtd
	 * @return Lista de produtos e fotos.
	 */
	List<Produto> listarUltimosCadastros(int qtd);
	
	/**
	 * Filtra os registros de produto pelo modo específico, 
	 * utilizando o código interno ou descrição.
	 * 
	 * @param filtro Filtro da pesquisa.
	 * @param firstResult Primeiro registro.
	 * @param qtdPorPagina Quantidade de registros por página.
	 * @return Lista de registros paginados.
	 */
	List<Produto> filtrarPeloModoEspecifico(FiltroProduto filtro, int firstResult, int qtdPorPagina);

	/**
	 * Pesquisa um registro de um produto pelo seu código interno.
	 * @param codigoInterno
	 * @return
	 */
	Produto buscarPeloCodigoInterno(String codigoInterno);

	/**
	 * Filtra os produtos pelo Subgrupo com filtro e paginado.
	 * 
	 * @param dep
	 * @param subgrupo
	 * @param filtro
	 * @param paginator Paginador para construir o índice.
	 * @return
	 */
	List<Produto> fitrarPelaNavegacao(Departamento dep, Grupo grupo, Subgrupo subgrupo, FiltroListaProduto filtro, Paginator paginator);

	/**
	 * Lista os produtos com suas características de acordo com o departamento,
	 * grupo ou subgrupo.
	 * 
	 * @param subgrupo 
	 * @param grupo 
	 * @param dep 
	 * 
	 * @return
	 */
	List<Produto> listarCaracteristicasDeProdutos(Departamento dep, Grupo grupo, Subgrupo subgrupo);
	
	/**
	 * Lista os produtos que atendam a pesquisa pelo código interno ou descrição.
	 * 
	 * @param pesquisa Código interno ou descrição.
	 * @param paginator define o primeiro resultado e a quantidade de resultados.
	 * @return Lista de produtos que satisfazem a condição da pesquisa.
	 */
	List<Produto> filtrarPeloCodigoInternoOuPelaDescricao(String pesquisa, Paginator paginator);

	/**
	 * Atualiza via Update o tipo de grade do produto.
	 * @param codigo do produto
	 * @param novoTipo novo tipo da grade.
	 * @return quantidade de registros alterados (deve ser 1).
	 */
	int atualizaGradeDoProduto(Long codigo, String novoTipo);

	/**
	 * Lista os produtos que atendam a pesquisa pelo código interno ou descrição.
	 * 
	 * @param pesquisa Código interno ou descrição.
	 * @param paginator define o primeiro resultado e a quantidade de resultados.
	 * @param finalidade venda ou consignado. 
	 * @return Lista de produtos que satisfazem a condição da pesquisa.
	 */
	List<Produto> filtrarPeloCodigoInternoOuPelaDescricao(String pesquisa, Finalidade finalidade, Paginator paginator);

	/**
	 * Lista os produtos de acordo com a finalidade filtrando os campos descritivos: 
	 * descrição, detalhes e código.
	 * 
	 * @param pesquisa termo da pesquisa
	 * @param finalidade do produto.
	 * @return lista de produtos.
	 */
	List<Produto> filtrarProdutosPorFinalidadeEDescricoes(String pesquisa, Finalidade finalidade, Paginator paginator);
	
	/**
	 * Lista os produtos por finalidade e faixa de valor, mínimo e máximo.
	 * 
	 * @param finalidade opção de finalidade do produto.
	 * @param min valor mínimo.
	 * @param max valor máximo.
	 * @return lista de produtos.
	 */
	List<Produto> filtrarProdutosPorFinalidadeEValor(Finalidade finalidade, BigDecimal min, BigDecimal max, Paginator paginator);
	
	/**
	 * Lista os produtos por finalidade e subgrupo.
	 * 
	 * @param finalidade opção de finalidade do produto.
	 * @param subgrupo opção do subgrupo.
	 * @return lista de produtos.
	 */
	List<Produto> filtrarProdutosPorFinalidadeESubgrupo(Finalidade finalidade, Subgrupo subgrupo, Paginator paginator);

	/**
	 * Lista N produtos mais vendidos por finalidade.
	 * 
	 * @param finalidade do produto.
	 * @param numero quantidade de registros.
	 * @return
	 */
	List<Produto> listarNProdutosMaisVendidosPorFinalidade(Finalidade finalidade, int numero);

	/**
	 * Lista a qtd de  produtos ativos agrupados pelo grupo e subgrupo.
	 * 
	 * @return qtd de produtos ativos agrupados.
	 */
	List<ProdutosEQtdPorSubgrupo> listarQtdDeProdutosPorSubgrupoEGrupo();

	/**
	 * Detalha a qtd de  produtos ativos agrupados pelo grupo e subgrupo.
	 * 
	 * @return referências dos produtos agrupados.
	 */
	List<ReferenciaProdutoComQtd> detalharQtdDeProdutosPorSubgrupoEGrupo(FiltroProduto filtro);
}
