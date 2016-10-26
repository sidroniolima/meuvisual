package net.mv.meuespaco.shiro;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import net.mv.meuespaco.model.Permissao;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.service.UsuarioService;

public class AutenticadorAutorizador extends AuthorizingRealm {
	
	private UsuarioService usuarioSrvc;

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return true;
	}
	
	private UsuarioService getUsuarioService() {
		
		Properties props = new Properties(); 
		
		props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		
	    try {
	    	
	        InitialContext ctx = new InitialContext(props);
	        
	        UsuarioService usuarioSrvc = (UsuarioService) ctx.lookup("java:module/UsuarioServiceImpl");
	        
	        return usuarioSrvc;
	        
	    } catch(NamingException e) { 
	    	
	        throw new RuntimeException(e);
	    }
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		setCachingEnabled(true);
		
		SimpleAuthorizationInfo info;
		
		if (principals == null) {
			throw new AuthorizationException("Principals is missing...");
		}
		
		String nome = (String) principals.getPrimaryPrincipal();
		
		Usuario usuario = null;
		
		usuarioSrvc = this.getUsuarioService();
		
		usuario = usuarioSrvc.getUsuarioLogado();
		
		if (null == usuario)
		{
			usuario = usuarioSrvc.buscaPeloNomeComPermissoes(nome);
			throw new AuthorizationException("Usuário não existe.");
		} 
		
		if (!usuario.temPermissoes()) {
			
			throw new AuthorizationException("Não há permissões associadas ao usuário.");
		}
		
		Set<String> roles = new HashSet<String>();
		
		for (Permissao permissao : usuario.getPermissoes()) {
			
			roles.add(permissao.toString());
		}
		
		info = new SimpleAuthorizationInfo(roles);
		info.setStringPermissions(roles);
		
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		
		UsernamePasswordToken usernameToken = (UsernamePasswordToken) token;
		
		String login = usernameToken.getUsername();
		String senha = new String(usernameToken.getPassword());
		
		usuarioSrvc = this.getUsuarioService();
		Usuario usuario = usuarioSrvc.buscaUsuarioPorLoginESenha(login, senha);
		
		if (null != usuario) {
			AuthenticationInfo info = new SimpleAuthenticationInfo(login, senha, getName());
			
			return info;
		}
		
		throw new AuthenticationException();
	}
	
}
