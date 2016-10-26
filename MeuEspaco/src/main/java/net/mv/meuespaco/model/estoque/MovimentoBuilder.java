package net.mv.meuespaco.model.estoque;

import java.math.BigDecimal;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.grade.Grade;

public class MovimentoBuilder {

	private Movimento instancia = new Movimento();
	
	public MovimentoBuilder saida() {
		this.instancia.setTipoMovimento(TipoMovimento.SAIDA);
		
		return this;
	}
	
	public MovimentoBuilder entrada() {
		this.instancia.setTipoMovimento(TipoMovimento.ENTRADA);
		
		return this;
	}
	
	public MovimentoBuilder doTipo(TipoMovimento tipo) {
		this.instancia.setTipoMovimento(tipo);
		return this;
	}
	
	public MovimentoBuilder doAlmoxarifado(Almoxarifado almoxarifado) {
		this.instancia.setAlmoxarifado(almoxarifado);
		
		return this;
	}
	
	public MovimentoBuilder doProduto(Produto produto) {
		this.instancia.setProduto(produto);
		
		return this;
	}
	
	public MovimentoBuilder paraGrade(Grade grade) {
		this.instancia.setGrade(grade);
		
		return this;
	}
	
	public MovimentoBuilder comQuantidade(BigDecimal qtd) {
		this.instancia.setQtd(qtd);
		
		return this;
	}
	
	public MovimentoBuilder daOrigem(OrigemMovimento origem) {
		this.instancia.setOrigem(origem);
		
		return this;
	}
	
	public MovimentoBuilder doUsuario(Usuario usuario)
	{
		this.instancia.setUsuario(usuario);
		return this;
	}
	
	public MovimentoBuilder comMotivo(String motivo)
	{
		this.instancia.setMotivo(motivo);
		return this;
	}
	
	public Movimento cria() {
		return instancia;
	}
	
}
