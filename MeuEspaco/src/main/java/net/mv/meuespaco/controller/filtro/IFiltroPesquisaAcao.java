package net.mv.meuespaco.controller.filtro;

import java.time.LocalDate;

import net.mv.meuespaco.model.loja.StatusEscolha;

/**
 * Interface para filtro de pesquisa de ações do cliente  
 * como Escolha, Compra e Resgate de Brinde.
 * 
 * @author sidronio
 * @created 26/06/2017
 */
public interface IFiltroPesquisaAcao 
{
	public Long getCodigo();

	public StatusEscolha getStatus();
	
	public LocalDate getDataInicial();

	public LocalDate getDataFinal();

	public String getCodigoCliente(); 
	
	public boolean isPreenchido();
}
