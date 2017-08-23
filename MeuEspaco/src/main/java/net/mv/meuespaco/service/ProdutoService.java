package net.mv.meuespaco.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.primefaces.model.UploadedFile;

import net.mv.meuespaco.controller.PesquisaProdutoBean.FiltroProduto;
import net.mv.meuespaco.controller.ProdutosEQtdPorSubgrupo;
import net.mv.meuespaco.controller.filtro.FiltroListaProduto;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Caracteristica;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Grupo;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface da camada Service para a Entidade Produto.
 * 
 * @author Sidronio
 * 17/11/2015
 */
public interface ProdutoService extends SimpleServiceLayer<Produto, Long>{

	/**
	 * Retorna um registro com os relacionamentos buscado 
	 * pelo código.
	 * 
	 * @param paramCodigo
	 * @return
	 */
	public Produto buscaPeloCodigoComRelacionamentos(Long paramCodigo);
	
	/**
	 * Filtra as grades do produto de acordo com a cor.
	 * 
	 * @param cor Cor para filtro.
	 * @param produto Produto a ser pesquisado.
	 * @return Retorna a lista de tamanhos disponíveis para a cor.
	 */
	public List<Tamanho> filtraGradesPorCor(Produto produto, Cor cor);
	
	/**
	 * Filtra as grades do produto de acordo com o tamanho.
	 * 
	 * @param tamanho Tamanho para filtro.
	 * @param produto Produto a ser pesquisado.
	 * 
	 * @return Retorna a lista de cores disponíveis para a grade.
	 */
	public List<Cor> filtraGradesPeloTamanho(Produto produto, Tamanho tamanho);
	
	/**
	 * Lista as cores disponíveis do produto pela grade de cor 
	 * ou tamanho e cor.
	 * 
	 * @param produto Produto para ser verificado as grades.
	 * @return Lista de cores caso o produto possua essa grade.
	 */
	public List<Cor> coresDisponiveisDoProduto(Produto produto);
	
	/**
	 * Lista os tamanhos disponíveis do produto.
	 * 
	 * @param produto Produto para ser verificado as grades.
	 * @return Lista de tamanhos disponíveis caso o produto possua essa grade.
	 */
	public List<Tamanho> tamanhosDisponiveisDoProduto(Produto produto);
	
	/**
	 * Localiza uma grade do produto pela selecionada.
	 * 
	 * @param produto
	 * @param gradeSelecionada
	 * @return
	 */
	public Grade gradeDoProduto(Produto produto, Grade gradeSelecionada);
	
	/**
	 * Busca um produto pelo código e retorna seus dados, com 
	 * unidade e grades.
	 * 
	 * @param codigo Código do produto para consulta.
	 * @return Produto com unidade e grades.
	 */
	public Produto buscaProdutoComGrade(Long codigo);
	
	/**
	 * Utilizada para inativar produto caso 
	 * não haja quantidade em estoque do almoxarifado 
	 * principal para venda em nenhuma das grades.
	 * 
	 * @param produto Produto para inativação.
	 * @throws RegraDeNegocioException 
	 */
	public void inativaProduto(Produto produto) throws RegraDeNegocioException;

	/**
	 * Ativa um produto que fora desativado por não estar 
	 * disponível em estoque.
	 * 
	 * @param produto
	 * @throws RegraDeNegocioException 
	 */
	public void ativaProduto(Produto produto) throws RegraDeNegocioException;

	/**
	 * Busca um produto pelo código com departamento, subgrupo e 
	 * fotos.
	 * 
	 * @param codigo Código para busca.
	 * @return Produto, subgrupo, departamento e fotos.
	 */
	public Produto buscaProdutoComFotos(Long codigo);

	/**
	 * Adiciona a foto ao produto e chama a função de gravação 
	 * da imagem em disco.
	 * 
	 * @param produto Produto para adição da foto.
	 * @param file Foto para upload.
	 * @throws IOException 
	 * @throws RegraDeNegocioException 
	 */
	public void adicionaFotoAoProduto(Produto produto, UploadedFile file) throws IOException, RegraDeNegocioException;

	/**
	 * Remove a foto do produto e chama a função de atualização 
	 * do produto.
	 * 
	 * @param produto Produto a qual pertence a foto.
	 * @param fotoSelecionada Foto selecionada para remoção.
	 * @throws RegraDeNegocioException Caso a foto não pertença ao produto será
	 * lançada uma exceção.
	 */
	public void removeFotoDoProduto(Produto produto, String fotoSelecionada) throws RegraDeNegocioException;

	/**
	 * Busca um produto pelo código e traz as informações básicas 
	 * e as relacionados ao site, isto é, fotos e relacionamentos.
	 * @param codigo Código para pesquisa.
	 * @return Produto com informações adicionais.
	 */
	public Produto buscaProdutoComInfoParaOSite(Long codigo);
	
	/**
	 * Lista dos últimos 10 produtos cadastrados para exibição na 
	 * página Index como destaque.
	 * 
	 * @return Lista dos 10 últimos produtos cadastrados.
	 */
	public List<Produto> ultimosDezProdutosCadastrados();

	/**
	 * Filtra os registros de Produtos de acordo com o filtro 
	 * na pesquisa e retorna os dados paginados. 
	 * 
	 * @param filtro
	 * @param paginator
	 * @return
	 */
	public List<Produto> filtraPelaPesquisa(FiltroProduto filtro, Paginator paginator);

	/**
	 * Busca um produto pelo seu código interno.
	 * @param codigoInterno
	 * @return Produto localizado.
	 */
	public Produto buscaPeloCodigoInterno(String codigoInterno);

	/**
	 * Lista os produtos por Departamento e Subgrupo fazendo filtro com paginação 
	 * dos resultados.
	 * @param dep
	 * @param subgrupo
	 * @param filtro
	 * @param paginator
	 * @return
	 */
	public List<Produto> listaProdutosPelaNavegacao(
			Departamento dep, 
			Grupo grupo, 
			Subgrupo subgrupo, 
			FiltroListaProduto filtro,
			Paginator paginator);

	/**
	 * Lista as características dos produtos de um determinado departamento e de 
	 * um grupo ou subgrupo.
	 * 
	 * @param dep
	 * @param grupo
	 * @param subgrupo
	 * @return
	 */
	public Map<Caracteristica, List<String>> listarCaracteristicasPorDepGrupoESubgrupo(Departamento dep, Grupo grupo,
			Subgrupo subgrupo);
	
	/**
	 * Lista o resultado da pesquisa pelo Código Interno ou Descrição dos produtos.
	 * 
	 * @param pesquisa Código Interno ou Descrição.
	 * @param paginator Paginador.
	 * @param finalidade Venda ou consignado.
	 * @return Lista de produtos.
	 */
	public List<Produto> filtrarProdutosPelaPesquisaDoUsuario(String pesquisa, Paginator paginator, Finalidade finalidade);
	
	/**
	 * Lista o resultado da pesquisa pelo Código Interno ou Descrição dos produtos.
	 * 
	 * @param pesquisa Código Interno ou Descrição.
	 * @param paginator Paginador.
	 * @return Lista de produtos.
	 */
	public List<Produto> filtrarProdutosPelaPesquisaDoUsuario(String pesquisa, Paginator paginator);

	/**
	 * Retorna as Letras disponíveis do Produto, segundo as grades.
	 * 
	 * @param produto Produto de consulta.
	 * @return Lista com as letras disponíveis.
	 */
	public List<String>  letrasDisponiveis(Produto produto);

	/**
	 * Retorna um produto pelo código com o subgrupo e grades.
	 * 
	 * @param codigo Código para pesquisa.
	 * @return Produto localizado.
	 */
	public Produto buscaPeloCodigoComGradeESubgrupo(Long codigo);

	/**
	 * Remove uma grade do produto. 
	 * Caso haja alguma movimentação para esta grade será 
	 * lançada um exceção impedindo a tentativa de exclusão.
	 * 
	 * @param produto produto para remoção.
	 * @param gradeSelecionada grade para remoção.
	 * @throws RegraDeNegocioException caso haja movimentação para esta grade.
	 */
	public void removeGrade(Produto produto, Grade gradeSelecionada) throws RegraDeNegocioException;

	/**
	 * Altera a grade do produto para o tipo de grade selecionado.
	 * 
	 * @param produto produto para alteração.
	 * @param novoTipo novo tipo de grade.
	 */
	public void alteraGradeDoProduto(Produto produto, TipoGrade novoTipo) throws RegraDeNegocioException;
	
	/**
	 * Atualiza o tipo do produto via Sql.
	 * 
	 * @param produto produto para alteração.
	 * @param novoTipo novo tipo de grade.
	 * @throws RegraDeNegocioException lança exceção caso não atualize 
	 * nenhum ou mais registros.
	 */
	public void atualizaTipoGradeDoProduto(Produto produto, TipoGrade novoTipo) throws RegraDeNegocioException;
	
	/**
	 * Verifica quais grades do produto tem estoque e saldo de reserva 
	 * positivo, isto é, pode ser escolhida. 
	 * 
	 * @param produto.
	 * @return
	 */
	public List<Grade> verificaGradesDisponiveis(Produto produto);

	/**
	 * Lista os símbolos musicais disponíveis para o produto.
	 * 
	 * @param produto
	 * @return
	 */
	public List<String> simbolosMusicaisDisponiveis(Produto produto);

	/**
	 * Filtra os produtos de finalidade Brinde que tiverem a descrição, 
	 * detalhes ou código de acordo com o termo da pesquisa.
	 * 
	 * @param pesquisa termo da pesquisa
	 * @return lista de produtos que atendem a pesquisa.
	 */
	public List<Produto> pesquisaDiversa(String pesquisa, Finalidade finalidade, Paginator paginator);
	
	/**
	 * Filtra os produtos pelo valor mínimo e máximo.
	 * 
	 * @param min valor mínimo
	 * @param max valor máximo
	 * @return produtos que atendem o filtro de valor.
	 */
	public List<Produto> filtraPeloValor(BigDecimal min, BigDecimal max, Finalidade finalidade, Paginator paginator);
	
	/**
	 * Lista os produtos pelo subgrupo.
	 * 
	 * @param subgrupo opção selecionada.
	 * @return brindes do subgrupo.
	 */
	public List<Produto> listaPeloSubgrupo(Subgrupo subgrupo, Finalidade finalidade, Paginator paginator);
	
	/**
	 * Lista os brindes em destaque de acordo com a quantidade 
	 * solicitada.
	 * 
	 * @param numero
	 * @return n brindes em destaque.
	 */
	public List<Produto> listaBrindesEmDestaque(int numero);

	/**
	 * Lista a quantidade de produtos ativos agrupados pelo sugrupo e grupo  
	 * 
	 * @return lista de quantidade de produtos por sub e grupo.
	 */
	public List<ProdutosEQtdPorSubgrupo> listaProdutosEQtdPorSubgrupo();
}