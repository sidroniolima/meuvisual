package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.CreditoDAO;
import net.mv.meuespaco.integracao.Credito;
import net.mv.meuespaco.integracao.ListagemCreditos;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.service.CreditoService;

@Stateless
public class CreditoServiceImpĺ implements CreditoService, Serializable
{
	private static final long serialVersionUID = -8574670731232468030L;

	@Inject
	private CreditoDAO creditoDAO;

	@Override
	public ListagemCreditos listagemDeCreditoDoClientePorPeriodo(Cliente cliente, LocalDate inicio, LocalDate fim) 
	{
		List<Credito> creditos = creditoDAO.buscaCreditosDoClientePorPeriodo(cliente, inicio, fim);
		
		creditos.stream().peek(c -> System.out.println(c.getNome()));
		
		return new ListagemCreditos(creditos);
	}

}
