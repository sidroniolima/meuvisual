package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroProduto;
import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Implementa a camada Controller da Entidade Produto. 
 * Exibe os registros cadastrados com paginação e permite 
 * a edição e exclusão deles.
 * 
 * @author Sidronio
 * 25/11/2015
 */
@Named
@ViewScoped
public class PesquisaProdutoBean extends PesquisaSingle implements Serializable {

	private static final long serialVersionUID = 5720020735647512195L;

	@Inject
	private ProdutoService produtoService;
	
	private List<Produto> produtos;
	
	private Produto produtoSelecionado;
	
	private FiltroProduto filtro;
	
	@Override
	@PostConstruct
	public void init() {
		filtro = new FiltroProduto();
		
		if (null == produtos) {
			this.listarComPaginacao();
		}
		
	}

	@Override
	public void excluir() {
		
		try {
			
			produtoService.exclui(produtoSelecionado.getCodigo());
			
			produtos.remove(produtoSelecionado);
			
			FacesUtil.addSuccessMessage(this.getMensagemDeExclusaoOk());
			
		} catch (RegraDeNegocioException | DeleteException e){
			FacesUtil.addErrorMessage(this.getMensagemDeErroDeExclusao(e.getMessage()));
		}
		
	}

	@Override
	public void listarComPaginacao() {
		
		if (filtro.isPreenchido()) {
			
			this.filtrar();
			
		} else {
		
			produtos = produtoService.listarComPaginacao(
					this.getPaginator(), 
					Arrays.asList("codigoInterno"), 
					Arrays.asList("departamento", "subgrupo", "subgrupo.grupo", "grupo.familia", "grades"), 
					Arrays.asList("departamento", "subgrupo", "subgrupo.grupo", "subgrupo.grupo.familia"));
		}
	}
	
	/**
	 * Filtra os registros de acordo com a pesquisa.
	 */
	public void filtrar() {
		
		produtos = this.produtoService.filtraPelaPesquisa(filtro, getPaginator());
	}

	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Produto %s excluído com sucesso.", this.descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("Produto %s não pode ser excluído.", this.descricaoDoRegistro(), msgError);
	}

	@Override
	public String descricaoDoRegistro() {
		return this.produtoSelecionado.getDescricao();
	}

	/**
	 * @return the produtoSelecionado
	 */
	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}
	/**
	 * @param produtoSelecionado the produtoSelecionado to set
	 */
	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	/**
	 * Lista dos produtos de acordo com a paginação.
	 * 
	 * @return the produtos
	 */
	public List<Produto> getProdutos() {
		return produtos;
	}

	/**
	 * @return the filtro
	 */
	public FiltroProduto getFiltro() {
		return filtro;
	}
	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(FiltroProduto filtro) {
		this.filtro = filtro;
	}

}
