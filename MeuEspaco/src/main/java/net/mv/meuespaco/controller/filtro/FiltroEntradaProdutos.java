package net.mv.meuespaco.controller.filtro;

public class FiltroEntradaProdutos implements IFiltro, FiltroDePesquisa 
{
	private FiltroProduto filtroProduto;
	private FiltroPeriodo filtroPeriodo;
	
	public FiltroEntradaProdutos() 
	{
		this.filtroProduto = new FiltroProduto();
		this.filtroPeriodo = new FiltroPeriodo();
	}
	
	public FiltroProduto getFiltroProduto() {
		return filtroProduto;
	}
	public void setFiltroProduto(FiltroProduto filtroProduto) {
		this.filtroProduto = filtroProduto;
	}
	public FiltroPeriodo getFiltroPeriodo() {
		return filtroPeriodo;
	}
	public void setFiltroPeriodo(FiltroPeriodo filtroPeriodo) {
		this.filtroPeriodo = filtroPeriodo;
	}
	
	@Override
	public boolean isPreenchido() 
	{
		return this.filtroPeriodo.isPreenchido() && this.filtroProduto.isPreenchido();
	}

	@Override
	public String toString() {
		return "FiltroEntradaProdutos [filtroProduto=" + filtroProduto.toString() + ", filtroPeriodo=" + filtroPeriodo.toString() + "]";
	}
	
	
}
