package net.mv.meuespaco.service;

import java.math.BigDecimal;
import java.util.List;

import net.mv.meuespaco.controller.filtro.FiltroEntradaProdutos;
import net.mv.meuespaco.controller.filtro.FiltroPesquisaMovimento;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.consulta.EstoqueDoProdutoConsulta;
import net.mv.meuespaco.model.consulta.MovimentoPorComposicaoSubgrupo;
import net.mv.meuespaco.model.estoque.Ajuste;
import net.mv.meuespaco.model.estoque.Almoxarifado;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.estoque.Movimento;
import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.util.Paginator;

/**
 * Interface da camada de Serviço do Estoque responsável 
 * pela movimentação e pela informação de quantidades.
 * 
 * @author Sidronio
 * 14/12/2015
 */
/**
 * @author sidronio
 *
 */
public interface EstoqueService {

	/**
	 * Informa a quantidade do produto para a grade disponível 
	 * no almoxarifado Venda.
	 * 
	 * @param produto Produto para consulta.
	 * @param grade Grade definida do produto na escolha.
	 * @return Quantidade do produto no estoque venda para a grade escolhida.
	 */
	public BigDecimal qtdDisponivelParaVenda(Produto produto, Grade grade);
	
	/**
	 * Salva uma instância de movimento.
	 * 
	 * @param movimento
	 */
	public void salva(Movimento movimento) throws RegraDeNegocioException;
	
	/**
	 * Valida o movimento.
	 * 
	 * @param movimento
	 */
	public void valida(Movimento movimento) throws RegraDeNegocioException;
	
	/**
	 * Gera uma saída de um produto de um almoxarifado.
	 * 
	 * @param almoxarifadoOrigem
	 * @param produto
	 * @param qtd
	 * @param origem
	 */
	public void saida(
			Almoxarifado almoxarifadoOrigem, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem) throws RegraDeNegocioException;
	
	/**
	 * Gera uma entrada do produto no almoxarifado especificado.
	 * 
	 * @param almoxarifadoOrigem
	 * @param produto
	 * @param qtd
	 * @param origem
	 */
	public void entrada(
			Almoxarifado almoxarifadoOrigem, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem) throws RegraDeNegocioException;
	
	/**
	 * Transfere a quantidade de um produto entre dois almoxaifados 
	 * passados.
	 * 
	 * @param almoxarifadoOrigem
	 * @param almoxarifadoDestino
	 * @param produto
	 * @param qtd
	 * @param origem
	 */
	public void transferencia(
			Almoxarifado almoxarifadoOrigem, 
			Almoxarifado almoxarifadoDestino, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem) throws RegraDeNegocioException;

	/**
	 * Ajuste de estoque por meio da movimentação 
	 * de produtos por grades.
	 * 
	 * @param ajuste Ajuste com seus itens.
	 * @throws RegraDeNegocioException 
	 */
	public void ajusta(Ajuste ajuste) throws RegraDeNegocioException;
	

	/**
	 * Verifica a disponibilidade do produto pela grade no estoque Principal
	 * e chama a inativação caso sua quantidade em estoque seja 0.
	 * 
	 * @param produto Produto escolhido.
	 * @throws RegraDeNegocioException 
	 */
	public void verificaDisponibilidadedoProduto(Produto produto) throws RegraDeNegocioException;
	
	/**
	 * Verifica se pela quantidade disponível para venda do produto e 
	 * grade o produto está disponível para venda.
	 * 
	 * @param produto
	 */
	public boolean estaDisponivelParaVenda(Produto produto);
	
	/**
	 * Retorna uma lista da quantidade do produto por almoxarifado 
	 * e grade.
	 * 
	 * @param produto Produto para consulta.
	 * @return Estoque do produto por almoxarifado e grade.
	 */
	public List<EstoqueDoProdutoConsulta> estoqueDoProdutoPorAlmoxarifadoEGrade(Produto produto);
	
	/**
	 * Verifica se há movimentação do produto para a grade.
	 * 
	 * @param produto produto para pesquisa.
	 * @param grade grade para pesquisa.
	 * @return true caso haja, false caso contrário.
	 */
	public boolean verificaSeHaMovimentacaoDoProdutoParaAGrade(Produto produto, Grade grade);

	/**
	 * Informa as grades com saldo em estoque positivo.
	 * 
	 * @param produto Produto.
	 * @return Lista de grades.
	 */
	public List<Grade> gradesDisponíveis(Produto produto);
	
	/**
	 * Verifica se há quantidade em estoque do produto para a grade 
	 * selecionada.
	 * 
	 * @param produto selecionado.
	 * @param grade selecionada.
	 * @return se há ou não qtd em estoque.
	 */
	public boolean verificaSeHaDisponibilidadeParaAGrade(Produto produto, Grade grade);
	
	/**
	 * Gera uma saída de um produto de um almoxarifado 
	 * mapeando o usuário e informando o motivo para ajustes.
	 * 
	 * @param almoxarifadoOrigem
	 * @param produto
	 * @param qtd
	 * @param origem
	 * @param usuario
	 * @param motivo	 
	 */
	public void saida(
			Almoxarifado almoxarifadoOrigem, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem,
			Usuario usuario,
			String motivo) throws RegraDeNegocioException;
	
	/**
	 * Gera uma entrada do produto no almoxarifado especificado 
	 * mapeando o usuário e informando o motivo para ajustes.
	 * 
	 * @param almoxarifadoOrigem
	 * @param produto
	 * @param qtd
	 * @param origem
	 * @param usuario
	 * @param motivo	
	 */
	public void entrada(
			Almoxarifado almoxarifadoOrigem, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem,
			Usuario usuario,
			String motivo) throws RegraDeNegocioException;
	
	/**
	 * Transfere a quantidade de um produto entre dois almoxaifados 
	 * passados. Mapeando o usuário e informando o motivo para ajustes.
	 * 
	 * @param almoxarifadoOrigem
	 * @param almoxarifadoDestino
	 * @param produto
	 * @param qtd
	 * @param origem
	 * @param usuario
	 * @param motivo
	 */
	public void transferencia(
			Almoxarifado almoxarifadoOrigem, 
			Almoxarifado almoxarifadoDestino, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem,
			Usuario usuario,
			String motivo) throws RegraDeNegocioException;

	/**
	 * Lista os registros de movimento de acordo com o filtro da pesquisa.
	 * 
	 * @param filtro da pesquisa. 
	 * @param paginator da pesquisa.
	 */
	public List<Movimento> listaMovimentosPeloFiltro(FiltroPesquisaMovimento filtro, Paginator paginator);

	/**
	 * Atualiza as movimentações com o horário no formato LocalDateTime.
	 */
	public void atualizaHorario();
	
	/**
	 * Movimenta os itens do resgate.
	 * 
	 * @param itens itens do resgate
	 * @param origem
	 * @throws RegraDeNegocioException
	 */
	public void movimenta(List<? extends IMovimentavel> itens, OrigemMovimento origem) throws RegraDeNegocioException; 
	
	/**
	 * Estorna a movimentação dos itens do resgate.
	 * 
	 * @param itens itens do resgate
	 * @param origem
	 * @throws RegraDeNegocioException
	 */
	public void estorna(List<? extends IMovimentavel> itens, OrigemMovimento origem) throws RegraDeNegocioException;

	
	/**
	 * Agrupa os moviemntos de entrada pelo filtro de produto e período.
	 * 
	 * @param filtro de entada de produtos por período.
	 * @param paginator paginador do controller
	 */
	public List<MovimentoPorComposicaoSubgrupo> agrupaMovimentosPeloFiltro(FiltroEntradaProdutos filtro, Paginator paginator); 
}
