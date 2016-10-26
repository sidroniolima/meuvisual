package net.mv.meuespaco.model.dashboard;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.log4j.helpers.DateTimeDateFormat;
import org.apache.shiro.subject.Subject;

import net.mv.meuespaco.model.Usuario;

/**
 * Classe que encapsula informações do login: usuário, data e hora do login e
 * logout e número da sessão.
 * 
 * @author Sidronio
 * @created 28/06/2016
 */
public class LoginInfo {

	private String sessionId; 
	private Usuario usuario;
	private LocalDateTime dataHoraLogin;
	private LocalDateTime dataHoraLogout;
	private Subject subject;
	
	public LoginInfo(String sessionId, Usuario usuario, LocalDateTime dataHoraLogin, Subject subject) {
		this.sessionId = sessionId;
		this.usuario = usuario;
		this.dataHoraLogin = dataHoraLogin;
		this.subject = subject;
	}
	
	public LoginInfo(String sessionId, Usuario usuario, LocalDateTime dataHoraLogin, LocalDateTime dataHoraLogout, Subject subject) {
		this.sessionId = sessionId;
		this.usuario = usuario;
		this.dataHoraLogin = dataHoraLogin;
		this.dataHoraLogout = dataHoraLogout;
		this.subject = subject;
	}
	
	public LoginInfo(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public LocalDateTime getDataHoraLogin() {
		return dataHoraLogin;
	}
	public void setDataHoraLogin(LocalDateTime dataHoraLogin) {
		this.dataHoraLogin = dataHoraLogin;
	}
	
	public LocalDateTime getDataHoraLogout() {
		return dataHoraLogout;
	}
	public void setDataHoraLogout(LocalDateTime dataHoraLogout) {
		this.dataHoraLogout = dataHoraLogout;
	}
	
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	/**
	 * Retorna o horário da última operação do usuário.
	 * 
	 * @return horário da última operação.
	 */
	public LocalDateTime getHorarioDaUltimaAcao()
	{
		Instant instant = Instant.ofEpochMilli(this.subject.getSession().getLastAccessTime().getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginInfo other = (LoginInfo) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

}
