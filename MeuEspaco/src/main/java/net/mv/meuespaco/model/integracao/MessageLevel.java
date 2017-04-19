package net.mv.meuespaco.model.integracao;

/**
 * Nível da mensagem.
 * 
 * @author sidronio
 * @create 30/03/2017
 */
public enum MessageLevel {

	NORMAL("Normal"),
	PRIORITARIA("Prioritária");
	
	private String descricao;

	private MessageLevel(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() 
	{
		return descricao;
	}
}
