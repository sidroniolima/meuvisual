package net.mv.meuespaco.model.cielo;

import com.google.gson.annotations.SerializedName;

public class DebitPayment extends Payment {

	@SerializedName("ReturnUrl")
	private String returnUrl;
	
	@SerializedName("AuthenticationUrl")
	private String authenticationUrl;

	@Override
	public PaymentType getType() {
		return PaymentType.DebitCard;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payment fromJson(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getReturnUrl() {
		return returnUrl;
	}
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getAuthenticationUrl() {
		return authenticationUrl;
	}
	public void setAuthenticationUrl(String authenticationUrl) {
		this.authenticationUrl = authenticationUrl;
	}
	
}
