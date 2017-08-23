package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;

public class FiltroPeriodo implements IFiltro
{
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	
	public LocalDate getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(LocalDate dataInicial) {
		this.dataInicial = dataInicial;
	}
	public LocalDate getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public boolean isComposto()
	{
		return null != this.dataInicial && null != this.dataFinal;
	}
	
	@Override
	public boolean isPreenchido() {
		return (null != this.dataInicial || null != this.dataFinal) && !(null == this.dataInicial && null != this.dataFinal);
	}
	
	@Override
	public String toString() {
		return "FiltroPeriodo [dataInicial=" + dataInicial + ", dataFinal=" + dataFinal + "]";
	}
}
