package net.mv.meuespaco.model;

/**
 * Permissões para os usuários:
 * _USER: Uusário não administrativo mas com permissões para 
 * controle do sistema.
 * _ADMIN: Acesso total ao sistema.
 * _CLIENTE: Acesso ao site apenas.
 * 
 * @author Sidronio
 * 05/01/2016
 */
public enum Permissao {

	ROLE_USER("Usuário"),
	ROLE_ADMIN("Administrador"),
	ROLE_CLIENTE("Cliente"),
	ROLE_VENDA("Venda");
	
	private String descricao;

	private Permissao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
