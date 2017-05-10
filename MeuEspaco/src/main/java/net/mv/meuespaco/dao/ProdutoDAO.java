package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.controller.PesquisaProdutoBean.FiltroProduto;
import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
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

}
