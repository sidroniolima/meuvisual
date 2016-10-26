package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.annotations.UsuarioLogado;
import net.mv.meuespaco.controller.PesquisaUsuarioBean.FiltroUsuario;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.UsuarioDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.service.UsuarioService;

/**
 * Implementação da camada Service da entidade Usuario.
 * 
 * @author Sidronio
 * 05/01/2016
 */
@Stateless
public class UsuarioServiceImpl extends SimpleServiceLayerImpl<Usuario, Long> implements UsuarioService, Serializable {

	private static final long serialVersionUID = 8881068088713450511L;
	
	@Inject
	private UsuarioDAO usuarioDAO;

	@Inject
	@UsuarioLogado
	private Usuario usuarioLogado;

	@Override
	public void validaInsercao(Usuario usuario) throws RegraDeNegocioException {
		usuario.valida();
	}

	@Override
	public void validaExclusao(Usuario usuario) throws RegraDeNegocioException {
		throw new RegraDeNegocioException("O usuário só é excluído pelo cliente. "
				+ "Porém cuidado. Apenas exclua um pré-cadastro, caso contrário inative-o.");
	}

	@Override
	public Usuario buscaUsuarioPorLoginESenha(String login, String senha) 
	{
		Usuario usuario = this.usuarioDAO.buscarPeloLoginESenha(login, senha);
		return usuario;
	}
	
	@Override
	public Usuario buscaPeloCodigoComPermissoes(Long codigo) {
		
		return usuarioDAO.buscarPeloCodigoComRelacionamento(
				codigo, 
				Arrays.asList("permissoes"));
	}
	
	@Override
	public Usuario buscaPeloNomeComPermissoes(String login) {
		return usuarioDAO.filtrar(
				new Usuario(login), 
				Arrays.asList("login"), 
				Arrays.asList("permissoes"), 
				null, 
				null).get(0);
	}
	
	@Override
	public List<Usuario> filtraUsuario(FiltroUsuario filtro) {
		
		return usuarioDAO.filtrarPelaPesquisa(filtro.getLogin(), filtro.getNome());
	}

	@Override
	public GenericDAO getDAO() {
		return usuarioDAO;
	}

	@Override
	public void inativaUsuarios()
	{
		usuarioDAO.inativaUsuarios();
	}
	
	@Override
	public void reativaUsuarios() 
	{
		usuarioDAO.reativaUsuarios();
	}
	
	@Override
	public Usuario getUsuarioLogado()
	{
		return usuarioLogado;
	}
}
