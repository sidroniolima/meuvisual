package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.time.LocalDate;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.CreditoDAO;
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
		return new ListagemCreditos(creditoDAO.buscaCreditosDoClientePorPeriodo(cliente, inicio, fim));
	}

}
