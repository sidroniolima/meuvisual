package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Permissao;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.service.UsuarioService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Camada Controller para Cadastro de Usuario.
 * 
 * @author Sidronio
 * 05/01/2016
 */
@Named
@ViewScoped
public class CadastroUsuarioBean extends CadastroSingle implements Serializable {

	private static final long serialVersionUID = -3531294156263555459L;

	@Inject
	private UsuarioService usuarioSrvc;
	
	private Usuario usuario;
	
	@Param @Inject
	private Long paramCodigo;
	
	private Permissao[] permissoes;
	
	@Override
	@PostConstruct
	public void init() {
		
		if (isEdicao()) {
			usuario = usuarioSrvc.buscaPeloCodigoComPermissoes(paramCodigo);
		
		} else {
			novoRegistro();
		}
		
		permissoes = Permissao.values();
	}

	@Override
	void novoRegistro() {
		usuario = new Usuario();
	}

	@Override
	public void salvar() {
		
		try {

			usuarioSrvc.salva(usuario);
			
			if (isEdicao()) {
			
				FacesUtil.addSuccessMessage(this.getMensagemDeAlteracao(usuario.getLogin()));
			
			} else {
				FacesUtil.addSuccessMessage(this.getMensagemDeInclusao(usuario.getLogin()));
			}
			
			novoRegistro();
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(
					String.format("Não foi possível salvar o registro. %s", e.getMessage()));
		}
		
	}
	
	/**
	 * Salva o registro de usuário após a mudança de senha.
	 * 
	 */
	public void alterarSenha() {
		
		try {

			usuarioSrvc.salva(usuario);
			
			FacesUtil.addSuccessMessage(this.getMensagemDeAlteracaoDeSenha(usuario.getLogin()));
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(
					String.format("Não foi possível alterar a senha deste usuário. %s", e.getMessage()));
		}
		
	}
	
	/**
	 * Gera a mensagem de alteração de senha bem sucedida.
	 * 
	 * @param login
	 * @return
	 */
	private String getMensagemDeAlteracaoDeSenha(String login) {
		return String.format("Senha alterada para o usuário %s.", login);
	}

	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Usuário %s incluído com sucesso.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("Usuário %s alterado com sucesso.", registro);
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the permissoes
	 */
	public Permissao[] getPermissoes() {
		return permissoes;
	}
	
}
