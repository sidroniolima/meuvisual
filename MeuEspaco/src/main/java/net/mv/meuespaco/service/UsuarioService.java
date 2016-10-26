package net.mv.meuespaco.service;

import java.util.List;

import net.mv.meuespaco.controller.PesquisaUsuarioBean.FiltroUsuario;
import net.mv.meuespaco.model.Usuario;

/**
 * Abstração da camada de Serviço da entidade Usuario.
 * 
 * @author Sidronio
 * 05/01/2016
 */
public interface UsuarioService extends SimpleServiceLayer<Usuario, Long>{

	/**
	 * Pesquisa um usuário pelo Login e Senha.
	 * 
	 * @param login
	 * @param senha
	 * @return Usuário.
	 */
	public Usuario buscaUsuarioPorLoginESenha(String login, String senha);

	/**
	 * Pesquisa um usuário pelo código e retorna as informações 
	 * com permissões.
	 * 
	 * @param codigo
	 * @return Usuario com permissões.
	 */
	public Usuario buscaPeloCodigoComPermissoes(Long codigo);

	/**
	 * Pesquisa um usuário pelo Nome.
	 * 
	 * @param nome
	 * @return
	 */
	public Usuario buscaPeloNomeComPermissoes(String nome);

	/**
	 * Filtra um usuário pela pesquisa.
	 * 
	 * @param filtro
	 * @return Registros que atendem o filtro.
	 */
	public List<Usuario> filtraUsuario(FiltroUsuario filtro);

	/**
	 * Inativa os usuários dos clientes inativos.
	 */
	public void inativaUsuarios();
	
	/**
	 * Retorna o usuário logado através do cliente.
	 * 
	 * @return usuário logado.
	 */
	Usuario getUsuarioLogado();
	
	/**
	 * Reativa usuários de clientes que voltaram 
	 * a vender, isto é, passaram de inativo para ativo.
	 */
	public void reativaUsuarios();
}
