package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroProduto;
import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.consulta.ReferenciaProdutoComQtd;
import net.mv.meuespaco.service.ProdutoService;

@Named
@ViewScoped
public class RelatorioItensPorSubgrupoBean implements Serializable
{
	private static final long serialVersionUID = -3798625793093551082L;

	@Inject
	private ProdutoService produtoSrvc;
	
	private List<ProdutosEQtdPorSubgrupo> itens;
	private LocalDate dataRelatorio;
	private ProdutosEQtdPorSubgrupo itemSelecionado;
	
	private List<ReferenciaProdutoComQtd> produtosExpandidos;
	
	@PostConstruct
	public void init() 
	{
		this.listar();
		this.dataRelatorio = LocalDate.now();
	}

	public void listar() 
	{
		this.itens = this.produtoSrvc.listaProdutosEQtdPorSubgrupo();
	}
	
	public void expandirItem()
	{
		FiltroProduto filtro = new FiltroProduto(
				itemSelecionado.getGrupoDescricao(), 
				itemSelecionado.getSubgrupoDescricao(),
				itemSelecionado.getDepartamentoDescricao(),
				Composicao.valueOf(itemSelecionado.getComposicao()),
				itemSelecionado.getCaracteristica(),
				Finalidade.CONSIGNADO,
				true);
		
		this.produtosExpandidos = this.produtoSrvc.detalhaProdutosEQtdPorSubgrupoPorReferencia(filtro);
	}
	
	public String   imprimir()
	{
		return "relatorio-itens-agrupados-print";
	}
	
	public List<ProdutosEQtdPorSubgrupo> getItens() 
	{
		return itens;
	}

	public LocalDate getDataRelatorio() 
	{
		return dataRelatorio;
	}

	public ProdutosEQtdPorSubgrupo getItemSelecionado() {
		return itemSelecionado;
	}
	public void setItemSelecionado(ProdutosEQtdPorSubgrupo itemSelecionado) {
		this.itemSelecionado = itemSelecionado;
	}
	
	public List<ReferenciaProdutoComQtd> getProdutosExpandidos() 
	{
		return produtosExpandidos;
	}
}
