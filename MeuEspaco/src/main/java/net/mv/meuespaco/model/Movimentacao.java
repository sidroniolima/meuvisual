package net.mv.meuespaco.model;

import net.mv.meuespaco.model.grade.Grade;

public class Movimentacao {

	private Produto produto;
	private Grade grade;
	private Long qtd;
	
	public Movimentacao() {	}
	
	public Movimentacao(Produto produto, Grade grade, Long qtd) {
		this.produto = produto;
		this.grade = grade;
		this.qtd = qtd;
	}

	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	public Long getQtd() {
		return qtd;
	}
	public void setQtd(Long qtd) {
		this.qtd = qtd;
	}
	
}
