/**
 * 
 */
package net.mv.meuespaco.controller;

/**
 * @author Sidronio
 *
 */
public abstract class CadastroMasterDetail extends CadastroSingle {

	/**
	 * Informa se é uma Edição de item.
	 * 
	 * @return
	 */
	abstract boolean isEdicaoDeItem();
	
	
	/**
	 * Edita um item (detail) selecionado.
	 */
	abstract void editaItem();
	
	/**
	 * Utilizada na finalização da inclusão de um item (detail).
	 * 
	 * @return
	 */
	abstract String getMensagemDeInclusaoDeItem(String registro);
	
	/**
	 * Utilizada na alteração de um item (detail).
	 * 
	 * @return
	 */
	abstract String getMensagemDeAlteracaoDeItem(String registro);
	
	/**
	 * Utilizada na exclusão de um item (detail).
	 * 
	 * @return
	 */
	abstract String getMensagemDeExclusaoDeItem(String registro);
	
	/**
	 * Innclui um item (detail).
	 * 
	 * @return
	 */
	abstract void adicionaItem();
	
	/**
	 * Remove um item (detail).
	 * 
	 * @return
	 */
	abstract void removeItem();
	
	/**
	 * Cria uma instância do item (detail).
	 */
	abstract void novoItem();
	
}
