package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateful;
import javax.enterprise.context.ApplicationScoped;

import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.service.ReservaProdutoService;

/**
 * Implementação da camada Service da Reserva 
 * de produtos.
 * 
 * @author Sidronio
 * 14/12/2015
 */
@Stateful
@ApplicationScoped
public class ResevaProdutoServiceImpl implements ReservaProdutoService, Serializable {

	private static final long serialVersionUID = 1L;
	
	private Map<Produto, Map<Grade, BigDecimal>> reserva;
	
	private static final Logger logger = Logger.getLogger(ResevaProdutoServiceImpl.class.getName());
	
	public ResevaProdutoServiceImpl() {
		this.reserva = new HashMap<Produto, Map<Grade, BigDecimal>>();
	}

	@Override
	public BigDecimal qtdReservadaDoProduto(Produto produto, Grade grade) {
		
		Map<Grade, BigDecimal> grades = reserva.get(produto);
		
		if (null == grades) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal qtd = grades.get(grade);
		
		return qtd == null ? BigDecimal.ZERO : qtd ;
	}

	@Override
	public void adicionaReserva(Produto produto, Grade grade, BigDecimal qtd) {

		Map<Grade, BigDecimal> mapaDeGrade = (Map<Grade, BigDecimal>) reserva.get(produto);

		if (null == mapaDeGrade) {
			mapaDeGrade = new HashMap<Grade, BigDecimal>();
			
			mapaDeGrade.put(grade, qtd);
			reserva.put(produto, mapaDeGrade);
		} else {
			
				mapaDeGrade.merge(grade, qtd, BigDecimal::add);
		}
		
		logger.log(Level.INFO, 
				String.format("Reserva de %s peça(s) para o produto %s com grade %s.", 
				qtd,
				produto.getDescricao(),
				grade.toString()));
		
		//this.imprimeReserva();
	}

	@Override
	public Map<Produto, Map<Grade, BigDecimal>> getReserva() {
		return reserva;
	}

	
	@Override
	public void removeReserva(Produto produto, Grade grade, BigDecimal qtd) {

		Map<Grade, BigDecimal> mapaDeGrade = (Map<Grade, BigDecimal>) reserva.get(produto);
		
		logger.log(Level.INFO, 
				String.format("Remoção de reserva de %s peça(s) para o produto %s com grade %s.", 
				qtd,
				produto.getDescricao(),
				grade.toString()));
		
		BigDecimal qtdNova = mapaDeGrade.get(grade).subtract(qtd);
	
		mapaDeGrade.replace(grade, qtdNova);
		
		//this.imprimeReserva();
	}
	
	@Override
	public Map<Grade, BigDecimal> gradesReservadasDoProduto(Produto produto) {
		return reserva.get(produto);
	}
	
	@Override
	public void imprimeReserva()
	{
		/*
		reserva.forEach((k,v) -> {
			System.out.println(k.getDescricao());
				v.forEach((r,s) -> {
				System.out.println("\t -> " + r + ":" + s);
				});
			System.out.println();
			}
		); */
	}
}
