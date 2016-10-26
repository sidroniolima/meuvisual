package net.mv.meuespaco.model.loja;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Grade;

/**
 * DataTestBuilder da Venda utilizada para criação.
 * 
 * @author Sidronio
 * @created 23/08/2016
 */
public class VendaBuilder {

	private Venda instancia;
	
	public VendaBuilder() {
		instancia = new Venda();
	}
	
	public VendaBuilder comCodigo(Long codigo)
	{
		this.instancia.setCodigo(codigo);
		return this;
	}
	
	public VendaBuilder noHorario(LocalDateTime horario)
	{
		this.instancia.setHorarioVenda(horario);
		return this;
	}
	
	public VendaBuilder finalizadaEm(LocalDateTime horario)
	{
		this.instancia.setHorarioFinalizacao(horario);
		return this;
	}
	
	public VendaBuilder doCliente(Cliente cliente)
	{
		this.instancia.setCliente(cliente);
		return this;
	}
	
	public VendaBuilder comDesconto(BigDecimal desconto)
	{
		this.instancia.setDescontoVenda(desconto);
		return this;
	}
	
	public VendaBuilder doProduto(Produto produto, BigDecimal qtd, Grade grade)
	{
		this.instancia.addItem(produto, qtd, grade);
		return this;
	}
	
	public Venda constroi()
	{
		return instancia;
	}
}
