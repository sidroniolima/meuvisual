package net.mv.meuespaco.model.cielo;

import com.google.gson.annotations.SerializedName;

public class Customer {

	@SerializedName("Name")
	private String name;

	public Customer() {	}
	
	public Customer(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
