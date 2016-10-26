package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.exception.DeleteException;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.service.UsuarioService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Controller para Pesquisa de entidades de Usuario.
 * 
 * @author Sidronio
 * 05/01/2016
 */
@Named
@ViewScoped
public class PesquisaUsuarioBean extends PesquisaSingle implements Serializable {

	private static final long serialVersionUID = -7342441908928320340L;

	@Inject
	private UsuarioService usuarioSrvc;
	
	@Inject
	private LoginBean loginBean;
	
	private Usuario usuarioSelecionado;
	
	private List<Usuario> usuarios;
	
	private FiltroUsuario filtro;
	
	/**
	 * Classe auxiliar da pesquisa para filtro de registros.
	 * 
	 * @author Sidronio
	 * 17/03/2016
	 */
	public class FiltroUsuario {
		
		private String login;
		private String nome;
		
		/**
		 * Construtor padrão.
		 */
		public FiltroUsuario() {	}

		public FiltroUsuario(String login, String nome) {
			this.login = login;
			this.nome = nome;
		}
		
		/**
		 * @return the login
		 */
		public String getLogin() {
			return login;
		}
		/**
		 * @param login the login to set
		 */
		public void setLogin(String login) {
			this.login = login;
		}

		/**
		 * @return the nome
		 */
		public String getNome() {
			return nome;
		}
		/**
		 * @param nome the nome to set
		 */
		public void setNome(String nome) {
			this.nome = nome;
		}
		
	}
	
	@Override
	@PostConstruct
	void init() {
		
		this.listarComPaginacao();
		this.filtro = new FiltroUsuario();
	}

	@Override
	public void excluir() {
		
		try {
			this.usuarioSrvc.exclui(usuarioSelecionado.getCodigo());
			
			usuarios.remove(usuarioSelecionado);
			
			FacesUtil.addSuccessMessage(this.getMensagemDeExclusaoOk());
		}
		catch (RegraDeNegocioException | DeleteException e) {
			FacesUtil.addErrorMessage(this.getMensagemDeErroDeExclusao(e.getMessage()));
		}
	}
	
	/**
	 * Filtra os dados de acordo com o filtro.
	 */
	public void filtrar() {
		
		usuarios = this.usuarioSrvc.filtraUsuario(filtro);
		
	}

	@Override
	public void listarComPaginacao() {
		usuarios = usuarioSrvc.listarComPaginacao(
				this.getPaginator(), 
				Arrays.asList("login"), 
				null, 
				null);
	}
	
	/**
	 * Altera a senha do usuário selecionado. Porém somente administradores 
	 * poderão alterar senhas.
	 * @throws RegraDeNegocioException Lança uma exceção caso o usuário não
	 * seja administrador.
	 */
	public String alteraSenha() throws RegraDeNegocioException {
		
		if (!loginBean.getUserLogged().isAdmin()) {
			throw new RegraDeNegocioException("Somente usuários adminstrativos podem alterar senhas.");
		}
		
		return "altera-senha";
	}


	@Override
	String getMensagemDeExclusaoOk() {
		return String.format("Usuário %s excluído com sucesso.", this.descricaoDoRegistro());
	}

	@Override
	String getMensagemDeErroDeExclusao(String msgError) {
		return String.format("Não foi possível excluir o usuário %s. %s", this.descricaoDoRegistro(), msgError);
	}

	@Override
	public String descricaoDoRegistro() {
		return usuarioSelecionado.getLogin();
	}

	/**
	 * @return the usuarioSelecionado
	 */
	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}
	/**
	 * @param usuarioSelecionado the usuarioSelecionado to set
	 */
	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	/**
	 * @return the usuarios
	 */
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	/**
	 * @return the filtro
	 */
	public FiltroUsuario getFiltro() {
		return filtro;
	}
	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(FiltroUsuario filtro) {
		this.filtro = filtro;
	}
}
