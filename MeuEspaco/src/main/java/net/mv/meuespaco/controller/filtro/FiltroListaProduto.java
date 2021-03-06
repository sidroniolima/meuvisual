package net.mv.meuespaco.controller.filtro;

import java.util.HashMap;
import java.util.Map;

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
	
	private String ordenacao;
	
	private Map<String, String> ordens = new HashMap<String, String>();
	
	public FiltroListaProduto() 
	{	
		ordens.put("-valor", "Menor preço");
		ordens.put("+valor", "Maior preço");
	}
	
	/**
	 * Verifica a ordem do campo na lista de ordenação.
	 * 
	 * @param campo
	 * @return DESC ou ASC
	 */
	public static String verificaOrdem(String key)
	{
		return key.charAt(0) == '+' ? "DESC" : "ASC";
	}
	
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

	public String getOrdenacao() {
		return ordenacao;
	}
	public void setOrdenacao(String ordenacao) {
		this.ordenacao = ordenacao;
	}

	public Map<String, String> getOrdens() {
		return ordens;
	}
}