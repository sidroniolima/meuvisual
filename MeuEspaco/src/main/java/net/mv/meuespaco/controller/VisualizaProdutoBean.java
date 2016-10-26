package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.consulta.EstoqueDoProdutoConsulta;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@RequestScoped
public class VisualizaProdutoBean implements Serializable{

	private static final long serialVersionUID = -8291863139907727373L;

	@Inject
	private ProdutoService produtoSrvc;
	
	@Inject
	private EstoqueService estoqueSrvc;
	
	@Param @Inject
	private Long paramCodigo;
	
	private Produto produto;
	
	private List<EstoqueDoProdutoConsulta> estoque = new ArrayList<EstoqueDoProdutoConsulta>();
	
	@PostConstruct
	public void init() {
		
		if (null != paramCodigo) {
			
			produto = produtoSrvc.buscaPeloCodigoComRelacionamentos(paramCodigo);
			
			this.mostraEstoque(produto);
			
		} else {
			
			FacesUtil.addErrorMessage("Não foi possível localizar o produto.");
			
		}
	}

	/**
	 * Pesquisa a quantidade do produto por grade e monta 
	 * a lista de estoque por grade do produto e almoxarifado.
	 * 
	 * @param produto Produto visualizado.
	 */
	private void mostraEstoque(Produto produto) {
		estoque = estoqueSrvc.estoqueDoProdutoPorAlmoxarifadoEGrade(produto);
	}

	/**
	 * @return the produto
	 */
	public Produto getProduto() {
		return produto;
	}

	/**
	 * @return the estoque
	 */
	public List<EstoqueDoProdutoConsulta> getEstoque() {
		return estoque;
	}
	
}
