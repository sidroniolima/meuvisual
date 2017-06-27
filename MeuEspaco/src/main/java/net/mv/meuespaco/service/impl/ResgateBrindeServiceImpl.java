package net.mv.meuespaco.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.annotations.ClienteLogado;
import net.mv.meuespaco.controller.filtro.IFiltroPesquisaAcao;
import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.ResgateBrindeDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.service.ResgateBrindeService;
import net.mv.meuespaco.util.Paginator;

/**
 * Abstração da camada DAO para a entidade ResgateBrinde.
 * 
 * @author sidronio
 * @created 02/06/2017
 */
@Stateless
public class ResgateBrindeServiceImpl extends SimpleServiceLayerImpl<ResgateBrinde, Long> implements ResgateBrindeService
{
	@Inject
	private ResgateBrindeDAO resgateDAO;
	
	@Inject
	@ClienteLogado
	private Cliente clienteLogado;

	@Override
	public ResgateBrinde salva(ResgateBrinde entidade) throws RegraDeNegocioException 
	{		
		return this.resgateDAO.salvar(entidade);
	}

	@Override
	public GenericDAO<ResgateBrinde, Long> getDAO() 
	{
		return this.resgateDAO;
	}

	@Override
	public void validaInsercao(ResgateBrinde entidade) throws RegraDeNegocioException 
	{
		entidade.valida();
	}

	@Override
	public void validaExclusao(ResgateBrinde entidade) throws RegraDeNegocioException 
	{
		//TODO:verificar status. 
	}
	
	@Override
	public ResgateBrinde criaResgateDeCarrinho(List<? extends IMovimentavel> itensDoCarrinho, Cliente cliente, Long saldoAtual) 
	{
		ResgateBrinde resgate = new ResgateBrinde(cliente, saldoAtual);
		
		itensDoCarrinho.stream().forEach(i -> 
		{
			try 
			{
				resgate.adicionaBrinde(
						i.getProduto(), 
						i.getQtd(), 
						i.getValorUnitario(), 
						i.getGrade());
				
			} catch (RegraDeNegocioException e) 
			{
				e.printStackTrace();
			}
		});	
		
		return resgate;
	}
	
	@Override
	public ResgateBrinde buscarComItensPeloCodigo(Long codigo) 
	{
		return this.resgateDAO
				.buscarPeloCodigoComRelacionamento(codigo, Arrays.asList("brindes"));
	}
	
	@Override
	public List<ResgateBrinde> utlimosResgatesDoCliente(Cliente cliente) 
	{
		return this.resgateDAO
				.listarUltimosResgatesPorCliente(cliente, 10);
	}

	@Override
	public ResgateBrinde buscarCompletaPeloCodigo(Long codigo) 
	{
		return this.resgateDAO
				.buscarPeloCodigoComRelacionamento(
						codigo, 
						Arrays.asList("cliente","cliente.regiao", "brindes","brindes.produto","brindes.grade"));
	}
	
	@Override
	public Long totalDePontosResgatadosDoClienteLogado() 
	{
		return this.resgateDAO.buscarPontosResgatadosDoCliente(this.clienteLogado);
	}
	
	@Override
	public List<ResgateBrinde> filtraPelaPesquisa(IFiltroPesquisaAcao filtro, Paginator paginator) 
	{
		return this.resgateDAO.filtrarPeloModoEspecifico(filtro, paginator);
	}
}
