package net.mv.meuespaco.model.estoque;

import java.util.ArrayList;
import java.util.List;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Representa um ajuste de estoque, seja ele entrada,
 * saída ou transferência entre almoxarifados.
 * 
 * @author Sidronio
 * 15/12/2015
 */
public class Ajuste {

	private Almoxarifado almEntrada;
	private Almoxarifado almSaida;
	
	private TipoMovimento tipoMovimento;
	private List<ItemAjuste> itens;
	
	private Usuario usuario;
	
	private String motivo;
	
	public Ajuste() {
		itens = new ArrayList<ItemAjuste>();
	}	
	
	/**
	 * Valida de acordo com regras de tipo e campos requeridos 
	 * um ajuste.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void valida() throws RegraDeNegocioException {
		
		if (null == tipoMovimento) {
			throw new RegraDeNegocioException("O tipo de movimento, entrada, saída ou transferência deve ser informado.");
		}
		
		if (tipoMovimento.equals(TipoMovimento.ENTRADA) || tipoMovimento.equals(TipoMovimento.TRASFERENCIA)) {
			if (null == almEntrada) {
				throw new RegraDeNegocioException("A entrada deve ser em algum almoxarifado. Selecione-o.");
			}
		}
		
		if (tipoMovimento.equals(TipoMovimento.SAIDA) || tipoMovimento.equals(TipoMovimento.TRASFERENCIA)) {
			if (null == almSaida) {
				throw new RegraDeNegocioException("A saída deve ser de algum almoxarifado. Selecione-o.");
			}
		}
		
		if (itens.isEmpty()) {
			throw new RegraDeNegocioException("A ajuste deve ter ao menos um item.");
		}

		if (null == motivo || motivo.isEmpty())
		{
			throw new RegraDeNegocioException("Informe o motivo para ajuste de estoque.");
		}
		
		if (null == usuario)
		{
			throw new RegraDeNegocioException("Não foi possível identificar o usuário. Entrar em "
					+ "contato com o administrador.");
		}
	}
	
	/**
	 * Adiciona um item ao ajuste caso este seja 
	 * válido.
	 * 
	 * @param item Item.
	 * @throws RegraDeNegocioException 
	 */
	public void adicionaItem(ItemAjuste item) throws RegraDeNegocioException {
		itens.add(item);
	}
	
	/**
	 * Remove um item do ajuste.
	 *  
	 * @param item Item.
	 */
	public void removeItem(ItemAjuste item) {
		itens.remove(item);
	}
	
	/**
	 * Remove um item pelo produto e grade.
	 * 
	 * @param produto Produto.
	 * @param grade Grade.
	 */
	public void removeItem(Produto produto, Grade grade) {
		int index = -1;
		
		for (ItemAjuste item : itens) {
			index++;
			
			if (item.getProduto().equals(produto) || item.getGrade().equals(grade)) {
				break;
			}
			
		}
		
		if (index > 0) {
			itens.remove(index);
		}
	}
	
	/**
	 * @return the itens
	 */
	public List<ItemAjuste> getItens() {
		return itens;
	}

	/**
	 * @return the almEntrada
	 */
	public Almoxarifado getAlmEntrada() {
		return almEntrada;
	}
	/**
	 * @param almEntrada the almEntrada to set
	 */
	public void setAlmEntrada(Almoxarifado almEntrada) {
		this.almEntrada = almEntrada;
	}

	/**
	 * @return the almSaida
	 */
	public Almoxarifado getAlmSaida() {
		return almSaida;
	}
	/**
	 * @param almSaida the almSaida to set
	 */
	public void setAlmSaida(Almoxarifado almSaida) {
		this.almSaida = almSaida;
	}

	/**
	 * @return the tipoMovimento
	 */
	public TipoMovimento getTipoMovimento() {
		return tipoMovimento;
	}
	/**
	 * @param tipoMovimento the tipoMovimento to set
	 */
	public void setTipoMovimento(TipoMovimento tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	/**
	 * Define a origem da movimentação como Ajuste
	 * 
	 * @return Ajuste.
	 */
	public OrigemMovimento getOrigemMovimento() {
		return OrigemMovimento.AJUSTE;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
}
