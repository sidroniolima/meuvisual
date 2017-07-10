package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.factory.GradeFactory;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.service.ClienteService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Abstração para os Bean da visualização dos produtos
 * para consginação e venda.
 * 
 * @author Sidronio
 * @created 17/08/16
 */
public abstract class ProdutoDetailAbstratcBean implements Serializable {

	private static final long serialVersionUID = -8005533306682249757L;
	
	private Logger log = Logger.getLogger(ProdutoDetailAbstratcBean.class.getSimpleName());

	@Inject
	protected ProdutoService produtoService;
	
	@Inject
	protected ClienteService clienteSrvc;
	
	private Produto produto;
	
	@Inject @Param
	protected Long paramCodigo;
	
	private Grade gradeSelecionada;
	private BigDecimal qtdDoProduto;
	
	private List<Tamanho> tamanhosDisponiveis;
	private List<Cor> coresDisponiveis;
	private List<String> letrasDisponiveis;
	
	private List<Cor> coresParaOTamanho;
	
	private boolean habilitaEscolha;

	private List<String> simbolosMusicaisDisponiveis;
	
	public ProdutoDetailAbstratcBean() {	}
	
	public ProdutoDetailAbstratcBean(ProdutoService produtoService, ClienteService clienteSrvc, Long paramCodigo) {
		this.produtoService = produtoService;
		this.clienteSrvc = clienteSrvc;
		this.paramCodigo = paramCodigo;
	}

	@PostConstruct
	public void init() {
		
		if (null != paramCodigo) {
			
			if (null == produto)
			{
				produto = produtoService.buscaPeloCodigoComRelacionamentos(paramCodigo);
				
				if (null == produto)
				{
					FacesUtil.addErrorMessage("Não foi possível localizar o produto.");
					return;
				}
				
				criaGradeDoProduto();
				
				tamanhosDisponiveis = produtoService.tamanhosDisponiveisDoProduto(produto);
				coresDisponiveis = produtoService.coresDisponiveisDoProduto(produto);
				letrasDisponiveis = produtoService.letrasDisponiveis(produto);
				simbolosMusicaisDisponiveis = produtoService.simbolosMusicaisDisponiveis(produto);
				
				qtdDoProduto = BigDecimal.ONE;
				
				this.verificaDisponibilidadeDeEscolha();
			}
		}
	}

	/**
	 * Verifica se o cliente pode fazer uma escolha. Caso seja uma 
	 * venda não há impedimento.
	 */
	abstract void verificaDisponibilidadeDeEscolha();

	/**
	 * Cria a grade de acordo com o tipo de grade do produto.
	 */
	private void criaGradeDoProduto() {
		gradeSelecionada = GradeFactory.cria(produto.getTipoGrade());
	}
	
	/**
	 * Encaminha a requisição para a camada Service 
	 * para filtrar as cores disponíveis por tamanho.
	 * 
	 * @return
	 */
	public void filtroDeCoresPorTamanho(ValueChangeEvent event) {
		
		Tamanho tamanhoSelecionado = (Tamanho) event.getNewValue();
		
		if (null != tamanhoSelecionado) {
			coresParaOTamanho = produtoService.filtraGradesPeloTamanho(produto, tamanhoSelecionado);
		}
		
	}

	/**
	 * Adiciona o produto ao carrinho com a grade 
	 * selecionada.
	 * @throws RegraDeNegocioException 
	 * 3
	 */
	public String addToChart() 
	{
		if (null == qtdDoProduto)
		{
			qtdDoProduto = BigDecimal.ONE;
		}

		if (null == gradeSelecionada) 
		{
			FacesUtil.addErrorMessage("A grade do produto deve ser especificada.");
		} else 
		{
			try {
				
				gradeSelecionada.valida();
				
				Grade gradeDoProduto = produtoService.gradeDoProduto(produto, gradeSelecionada);
				
				this.getCarrinhoBean().adicionaProduto(produto, qtdDoProduto, produto.getValor(), gradeDoProduto);
				return this.getUrlCarrinho();
				
			} catch (RegraDeNegocioException e) 
			{
				log.severe(String.format("ERRO ao adicionar o produto %s ao carrinho. %s", produto.getCodigoInterno(), e.getMessage()));
				FacesUtil.addErrorMessage(e.getMessage());
			}
			
		}
		
		return "";
	}
	
	/**
	 * Habilita ou desabilita escolha.
	 */
	void habilitaEscolha(boolean opcao)
	{
		this.habilitaEscolha = opcao;
	}
	
	/**
	 * Limpa o filtro de grade.
	 */
	public void limpaFiltro() {
		criaGradeDoProduto();
	}
	
	/**
	 * @return the produto
	 */
	public Produto getProduto() {
		return produto;
	}

	/**
	 * @return the gradeSelecionada
	 */
	public Grade getGradeSelecionada() {
		return gradeSelecionada;
	}
	/**
	 * @param gradeSelecionada the gradeSelecionada to set
	 */
	public void setGradeSelecionada(Grade gradeSelecionada) {
		this.gradeSelecionada = gradeSelecionada;
	}

	/**
	 * @return the tamanhosDisponiveis
	 */
	public List<Tamanho> getTamanhosDisponiveis() {
		Collections.sort(tamanhosDisponiveis);
		return tamanhosDisponiveis;
	}

	/**
	 * @return the coresDisponiveis
	 */
	public List<Cor> getCoresDisponiveis() {
		return coresDisponiveis;
	}

	/**
	 * @return the qtdDoProduto
	 */
	public BigDecimal getQtdDoProduto() {
		return qtdDoProduto;
	}
	/**
	 * @param qtdDoProduto the qtdDoProduto to set
	 */
	public void setQtdDoProduto(BigDecimal qtdDoProduto) {
		this.qtdDoProduto = qtdDoProduto;
	}

	/**
	 * @return the liberaEscolha
	 */
	public boolean isHabilitaEscolha() {
		return habilitaEscolha;
	}

	/**
	 * @return the coresParaOTamanho
	 */
	public List<Cor> getCoresParaOTamanho() {
		return coresParaOTamanho;
	}

	/**
	 * @return the letrasDisponiveis
	 */
	public List<String> getLetrasDisponiveis() {
		return letrasDisponiveis;
	}
	
	public List<String> getSimbolosMusicaisDisponiveis() {
		return simbolosMusicaisDisponiveis;
	}

	public abstract CarrinhoAbstractBean getCarrinhoBean();
	
	/**
	 * Indica a Url do carrinho para o de venda ou consignado.
	 * 
	 * @return url específica do carrinho.
	 */
	public abstract String getUrlCarrinho();

	public ClienteService getClienteSrvc() {
		return clienteSrvc;
	}
	
}
