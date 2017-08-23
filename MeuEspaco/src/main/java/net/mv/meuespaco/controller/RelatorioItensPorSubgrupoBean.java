package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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
}
