package net.mv.meuespaco.controller.filtro;

import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.TipoProduto;

/**
 * Filtro para listagem dos produtos de acordo com parâmetros do 
 * usuário.
 * 
 * @author Sidronio
 * @updated 10/08/2016
 */
public abstract class FiltroListaProduto {
	
	private TipoProduto tipo;
	private String caracteristica;
	private Composicao composicao;
	
	public FiltroListaProduto() {	}
	
	/**
	 * Limpa os valores do filtro.
	 */
	public void limpa() {
		tipo = null;
		caracteristica = "";
		composicao = null;
	}	

	public TipoProduto getTipo() {
		return tipo;
	}

	public void setTipo(TipoProduto tipo) {
		this.tipo = tipo;
	}

	public String getCaracteristica() {
		return caracteristica;
	}

	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}

	public Composicao getComposicao() {
		return composicao;
	}

	public void setComposicao(Composicao composicao) {
		this.composicao = composicao;
	}

	public abstract Finalidade getFinalidade();


}