package net.mv.meuespaco.dao;

import java.util.List;

import net.mv.meuespaco.model.Usuario;

/**
 * Interface DAO da entidade Usuario.
 * 
 * @author Sidronio
 * 05/01/2016
 */
public interface UsuarioDAO extends GenericDAO<Usuario, Long> {

	/**
	 * Filtra os registros na Pesquisa pelo Filtro, por login ou nome.
	 * 
	 * @param login Login do usuário.
	 * @param nome Nome do usuário.
	 * @return Lista de registros filtrados.
	 */
	List<Usuario> filtrarPelaPesquisa(String login, String nome);

	/**
	 * Inativa usuários dos clientes inativos..
	 */
	public void inativaUsuarios();

	/**
	 * Busca um usuário pelo seu login e senha.
	 * 
	 * @param login fornecido.
	 * @param senha fornecida.
	 * @return Usuário do login e senha caso exista.
	 */
	Usuario buscarPeloLoginESenha(String login, String senha);

	/**
	 * Reativa usuários que estão inativos pelos clientes ativos. 
	 */
	void reativaUsuarios();

}
