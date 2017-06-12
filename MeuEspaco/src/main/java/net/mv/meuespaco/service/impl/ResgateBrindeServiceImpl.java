package net.mv.meuespaco.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import net.mv.meuespaco.dao.GenericDAO;
import net.mv.meuespaco.dao.ResgateBrindeDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.loja.Cliente;
import net.mv.meuespaco.model.loja.ResgateBrinde;
import net.mv.meuespaco.service.ResgateBrindeService;

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
						i.valorTotal(), 
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
				.buscarPeloCodigoComRelacionamento(codigo, Arrays.asList("brindes","brindes.produto","brindes.grade"));
	}
}