package net.mv.meuespaco.util;

/**
 * Paginador utilizado na navegação dos registros. 
 * 
 * @author Sidronio
 *
 */
public class Paginator {

	private int firstResult = 0;
	private int qtdPorPagina = 0;
	private int totalDeRegistros = 0;
	
	private boolean habilitado = true;
	private int lastResult;
	private int page = 0;
	private int totalPages = 0;
	
	private int index;
	
	public Paginator() {	}
	
	public Paginator(int qtdPorPagina) {
		this.qtdPorPagina = qtdPorPagina;
		this.firstResult = 0;
		this.lastResult = (firstResult + qtdPorPagina) - 1;
	}

	public Paginator(int qtdPorPagina, int totalDeRegistros) {
		this(qtdPorPagina);
		this.totalDeRegistros = totalDeRegistros;
		
		if (lastResult > totalDeRegistros) 
		{
			lastResult = totalDeRegistros - 1;
		}
	}

	public void goTo(int index) {
		this.setIndex(index);
		if (index == 0) 
		{
			firstResult = 0;
			lastResult = qtdPorPagina - 1;
		} else 
		{
			firstResult = (index * qtdPorPagina);
			lastResult = firstResult + qtdPorPagina - 1;
		}
		
		if (lastResult > totalDeRegistros) 
		{
			lastResult = totalDeRegistros - 1;
		}
	}
	
	public void next() {
		this.setIndex(++index);
		if (firstResult + qtdPorPagina < getTotalDeRegistros()) 
		{
			firstResult = getLastResult() + 1;
			lastResult = firstResult + qtdPorPagina -1;
		}

	}
	
	public void previous() {
		this.setIndex(--index);
		if (firstResult - qtdPorPagina <= 0) 
		{
			firstResult = 0;
			lastResult = qtdPorPagina - 1;
		} else {
			firstResult = firstResult - qtdPorPagina;
			lastResult = (firstResult + qtdPorPagina) - 1;
		}
		
	}
	
	/**
	 * Reinicia o Paginator.
	 */
	public void reset() {
		
		firstResult = 0;
		this.setIndex(0);
		
		lastResult = firstResult + qtdPorPagina - 1;
	
		/*
		if (lastResult > totalDeRegistros) 
		{
			lastResult = totalDeRegistros - 1;
		}*/
	}
	
	/**
	 * Desabilita o Paginator, setando o início para 0 
	 * e a quantidade de registros para infinito.
	 */
	public void desabilita() {
		this.firstResult = 0;
		this.setIndex(0);
		this.qtdPorPagina = Integer.MAX_VALUE;
		
		habilitado = false;
	}
	
	/**
	 * Permite apenas a visualização de 18 partes. O restante 
	 * deverá ser navegado pelas setas direcionais.
	 * 
	 * @return
	 */
	public int partesVisiveis() {
		int partes = getPartes();
		return partes > 15 ? 15 : partes;
	}
	
	/**
	 * Calcula o número de partes (páginas) que o paginador tem baseado
	 * na quantidade de registros e a quantidade por página.
	 * 
	 * @return qtd de partes.
	 */
	public int getPartes() {
		
		int divisao = totalDeRegistros/qtdPorPagina;
		
		if (totalDeRegistros % qtdPorPagina != 0) {
			divisao++;
		}
		
		return divisao;
	}
	
	/**
	 * Posiciona o paginador na última parte (página).
	 */
	public void goToLast() {
		this.goTo(this.getPartes()-1);
	}
	
	public void nextPage()
	{
		if (!this.isLastPage())
		{
			this.page++;
		}
	}
	
	private boolean isLastPage() 
	{
		return this.page == this.totalPages;
	}
	
	private boolean isFirstPage()
	{
		return this.page == 0;
	}
	
	public void previousPage()
	{
		if (!this.isFirstPage())
			this.page--;	
	}

	public void goToPage(int pageNumber)
	{
		if (pageNumber >= 0 && pageNumber <= this.totalPages)
		{
			this.page = pageNumber;
		}
	}
	
	public void goToLastPage()
	{
		this.page = this.totalPages;
	}
	
	/**
	 * Verifica se o paginator deve ser particionado.
	 * 
	 * @return true se sim.
	 */
	public boolean isParticionado() {
		return this.getPartes() > this.partesVisiveis();
	}
	
	public int getLastResult() {
		return lastResult;
	}
	public int getFirstResult() {
		return this.firstResult;
	}
	public int getQtdPorPagina() {
		return this.qtdPorPagina;
	}
	
	public int getTotalDeRegistros() {
		return totalDeRegistros;
	}
	public void setTotalDeRegistros(int totalDeRegistros) {
		if (this.totalDeRegistros > 0 && this.totalDeRegistros != totalDeRegistros)
		{
			this.reset();
		}
		this.totalDeRegistros = totalDeRegistros;
	}
	
	public int getPage() {
		return this.page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the habilitado
	 */
	public boolean isHabilitado() {
		return habilitado;
	}
	
	/**
	 * Seta as informações da Page.
	 * 
	 * @param totalElements
	 * @param totalPages
	 */
	public void setPaged(int totalElements, int totalPages) 
	{
		this.totalDeRegistros = totalElements;
		this.totalPages = totalPages;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "Paginator [getLastResult()=" + getLastResult()
				+ ", getFirstResult()=" + getFirstResult()
				+ ", getQtdPorPagina()=" + getQtdPorPagina()
				+ ", page = " + getPage() + " ]";
	}

}
