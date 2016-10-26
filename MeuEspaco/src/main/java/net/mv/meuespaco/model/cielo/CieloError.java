package net.mv.meuespaco.model.cielo;

import com.google.gson.annotations.SerializedName;

public class CieloError {

	@SerializedName("Code")
	private String code;
	
	@SerializedName("Message")
	private String message;
	
	public CieloError(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "Código: " + code + ", mensagem: " + message;
	}
}
