package net.mv.meuespaco.controller;

import net.mv.meuespaco.util.IConstants;
import net.mv.meuespaco.util.Paginator;

/**
 * Classe abstrata para os Beans que envolvam 
 * uma entidade Master.
 * 
 * @author Sidronio
 *
 */
public abstract class PesquisaSingle {

	/**
	 * Paginador utilizado na navegação dos registros de modo Lazy.
	 */
	private Paginator paginator = new Paginator(IConstants.QTD_PADRAO_REGISTROS_POR_PAGINA);
	
	/**
	 * Inicia os atributos do Bean.
	 */
	abstract void init();
	
	
	/**
	 * Envia uma requisição de exclusão à camada
	 * de Serviço da Entidade.
	 */
	public abstract void excluir();
	
	/**
	 * Lista os registros com paginação.
	 */
	public abstract void listarComPaginacao();
	
	/**
	 * Mensagem de exclusão.
	 * 
	 * @param registro
	 * @return
	 */
	abstract String getMensagemDeExclusaoOk();
	
	/**
	 * Mensagem de falha na exclusão devido a um erro 
	 * de negócio na camada Service.
	 * 
	 * @param registro
	 * @param msgError
	 * @return
	 */
	abstract String getMensagemDeErroDeExclusao(String msgError);

	/** 
	 * Retorna uma instância de paginador padrão.
	 * 
	 * @return
	 */
	public Paginator getPaginator() {
		return paginator;
	}
	
	/**
	 * Retorna a descrição do registro, seja a descrição 
	 * propriamente dita ou outro atributo.
	 * 
	 * @return
	 */
	public abstract String descricaoDoRegistro();
}
