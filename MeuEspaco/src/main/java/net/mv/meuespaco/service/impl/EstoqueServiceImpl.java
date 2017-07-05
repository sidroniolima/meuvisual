package net.mv.meuespaco.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaMovimento;
import net.mv.meuespaco.dao.EstoqueDAO;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Usuario;
import net.mv.meuespaco.model.consulta.EstoqueDoProdutoConsulta;
import net.mv.meuespaco.model.estoque.Ajuste;
import net.mv.meuespaco.model.estoque.Almoxarifado;
import net.mv.meuespaco.model.estoque.IMovimentavel;
import net.mv.meuespaco.model.estoque.ItemAjuste;
import net.mv.meuespaco.model.estoque.Movimento;
import net.mv.meuespaco.model.estoque.MovimentoBuilder;
import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.estoque.TipoMovimento;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.service.AlmoxarifadoService;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.Paginator;

/**
 * Implemantação da camada de serviços do Estoque.
 * 
 * @author Sidronio
 * 14/12/2015
 */
@ApplicationScoped
public class EstoqueServiceImpl implements EstoqueService, Serializable {

	private static final long serialVersionUID = -5302992549229889324L;
	
	@Inject
	private EstoqueDAO estoqueDAO;
	
	@Inject
	private AlmoxarifadoService almService;
	
	@Inject
	private ProdutoService produtoSrvc;
	
	public void saida(
			Almoxarifado almoxarifadoOrigem, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem) throws RegraDeNegocioException {
		
		Movimento movimentoDeSaida = new MovimentoBuilder()
				.saida()
				.doAlmoxarifado(almoxarifadoOrigem)
				.doProduto(produto)
				.paraGrade(grade)
				.comQuantidade(qtd)
				.daOrigem(origem)
				.cria();
		
		this.salva(movimentoDeSaida);
	}
	
	public void entrada(
			Almoxarifado almoxarifadoOrigem, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem) throws RegraDeNegocioException {
		
		Movimento movimentoDeEntrada = new MovimentoBuilder()
				.entrada()
				.doAlmoxarifado(almoxarifadoOrigem)
				.doProduto(produto)
				.paraGrade(grade)
				.comQuantidade(qtd)
				.daOrigem(origem)
				.cria();
		
		this.salva(movimentoDeEntrada);
		
	}
	
	public void transferencia(
			Almoxarifado almoxarifadoOrigem, 
			Almoxarifado almoxarifadoDestino, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem) throws RegraDeNegocioException {
		
		Movimento movimentoDeSaida = new MovimentoBuilder()
				.saida()
				.doAlmoxarifado(almoxarifadoOrigem)
				.doProduto(produto)
				.paraGrade(grade)
				.comQuantidade(qtd)
				.daOrigem(origem)
				.cria();
		
		this.salva(movimentoDeSaida);
		
		Movimento movimentoDeEntrada = new MovimentoBuilder()
				.entrada()
				.doAlmoxarifado(almoxarifadoDestino)
				.doProduto(produto)
				.paraGrade(grade)
				.comQuantidade(qtd)
				.daOrigem(origem)
				.cria();
		
		this.salva(movimentoDeEntrada);
	}

	/* Salva o movimento.
	 * @see twarehouse.service.EstoqueService#salva(twarehouse.molde.estoque.Movimento)
	 */
	@Override
	public void salva(Movimento movimento) throws RegraDeNegocioException {
		
		this.valida(movimento);
		this.estoqueDAO.salvar(movimento);
	}

	/* Valida o movimento.
	 * @see twarehouse.service.EstoqueService#valida(twarehouse.molde.estoque.Movimento)
	 */
	@Override
	public void valida(Movimento movimento) throws RegraDeNegocioException {
		
		if (null == movimento.getAlmoxarifado()) {
			throw new RegraDeNegocioException("Um movimento deve ser de algum almoxarifado.");
		}
		
		if(null == movimento.getProduto()) {
			throw new RegraDeNegocioException("Deve ser definido qual o produto da movimentação.");
		}
		
		if (null == movimento.getGrade()) {
			throw new RegraDeNegocioException("A movimentação deve ser relacionada a uma grade.");
		}
		
		if(null == movimento.getQtd() || movimento.getQtd().compareTo(BigDecimal.ZERO) <= 0) {
			throw new RegraDeNegocioException("A quantidade precisa ser maior que zero.");
		}		
		
		if (null == movimento.getTipoMovimento()) {
			throw new RegraDeNegocioException("O movimento deve ser de entrada, saída ou transferência.");
		}
		
		if (null == movimento.getOrigem()) {
			throw new RegraDeNegocioException("A origem da movimentação deve ser informada.");
		}		
	}
	
	@Override
	public void ajusta(Ajuste ajuste) throws RegraDeNegocioException {
		
		ajuste.valida();
		
		for (ItemAjuste item : ajuste.getItens()) {
		
			if (TipoMovimento.ENTRADA.equals(ajuste.getTipoMovimento())) {
					
				this.entrada(ajuste.getAlmEntrada(), 
						item.getProduto(), 
						item.getGrade(), 
						item.getQtd(), 
						OrigemMovimento.AJUSTE,
						ajuste.getUsuario(),
						ajuste.getMotivo());
					
			}
			
			if (TipoMovimento.SAIDA.equals(ajuste.getTipoMovimento())) {
				
				this.saida(ajuste.getAlmSaida(), 
						item.getProduto(), 
						item.getGrade(), 
						item.getQtd(), 
						OrigemMovimento.AJUSTE,
						ajuste.getUsuario(),
						ajuste.getMotivo());
			}
			
			if (TipoMovimento.TRASFERENCIA.equals(ajuste.getTipoMovimento())) {
				
				this.transferencia(ajuste.getAlmSaida(),
						ajuste.getAlmEntrada(),
						item.getProduto(), 
						item.getGrade(), 
						item.getQtd(), 
						OrigemMovimento.AJUSTE,
						ajuste.getUsuario(),
						ajuste.getMotivo());
			}
			
			this.verificaDisponibilidadedoProduto(item.getProduto());
		}
		
	}
	
	@Override
	public BigDecimal qtdDisponivelParaVenda(Produto produto, Grade grade) {
		
		Almoxarifado almPrincipal = almService.almoxarifadoPrincipal(); 
		return this.estoqueDAO.qtdEmEstoqueDoProdutoEGrade(almPrincipal, produto, grade);
	}
	
	@Override
	public boolean verificaSeHaDisponibilidadeParaAGrade(Produto produto, Grade grade) {
		return this.qtdDisponivelParaVenda(produto, grade).compareTo(BigDecimal.ZERO) > 0;
	}
	
	@Override
	public void verificaDisponibilidadedoProduto(Produto produto) throws RegraDeNegocioException {
		
		if (!this.estaDisponivelParaVenda(produto) && produto.isAtivo()) 
		{
			produtoSrvc.inativaProduto(produto);
		} 

		if (this.estaDisponivelParaVenda(produto) && !produto.isAtivo()) 
		{
			produtoSrvc.ativaProduto(produto);
		}
	}
	
	@Override
	public boolean estaDisponivelParaVenda(Produto produto) {
		
		Almoxarifado alm = this.almService.almoxarifadoPrincipal();
		
		BigDecimal qtd = estoqueDAO.qtdEmEstoqueDoProduto(alm, produto);
		
		return qtd.compareTo(BigDecimal.ZERO) > 0;
	}
	
	@Override
	public List<EstoqueDoProdutoConsulta> estoqueDoProdutoPorAlmoxarifadoEGrade(Produto produto) {
		return estoqueDAO.estoqueDoProduto(produto);
	}
	
	@Override
	public boolean verificaSeHaMovimentacaoDoProdutoParaAGrade(Produto produto, Grade grade) {
		
		Movimento movimento = new Movimento(produto, grade);
		
		return estoqueDAO.filtrar(
				movimento, 
				Arrays.asList("produto","grade"), 
				null, 
				null, 
				null).size() > 0;
	}
	
	@Override
	public List<Grade> gradesDisponíveis(Produto produto) {
		
		return null;
	}
	
	@Override
	public void saida(
			Almoxarifado almoxarifadoOrigem, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem,
			Usuario usuario,
			String motivo) throws RegraDeNegocioException {
		
		Movimento movimentoDeSaida = new MovimentoBuilder()
				.saida()
				.doAlmoxarifado(almoxarifadoOrigem)
				.doProduto(produto)
				.paraGrade(grade)
				.comQuantidade(qtd)
				.daOrigem(origem)
				.doUsuario(usuario)
				.comMotivo(motivo)				
				.cria();
		
		this.salva(movimentoDeSaida);
	}
	
	@Override
	public void entrada(
			Almoxarifado almoxarifadoOrigem, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem,
			Usuario usuario,
			String motivo) throws RegraDeNegocioException {
		
		Movimento movimentoDeEntrada = new MovimentoBuilder()
				.entrada()
				.doAlmoxarifado(almoxarifadoOrigem)
				.doProduto(produto)
				.paraGrade(grade)
				.comQuantidade(qtd)
				.daOrigem(origem)
				.doUsuario(usuario)
				.comMotivo(motivo)				
				.cria();
		
		this.salva(movimentoDeEntrada);
		
	}
	
	@Override
	public void transferencia(
			Almoxarifado almoxarifadoOrigem, 
			Almoxarifado almoxarifadoDestino, 
			Produto produto, 
			Grade grade,
			BigDecimal qtd, 
			OrigemMovimento origem,
			Usuario usuario,
			String motivo) throws RegraDeNegocioException {
		
		Movimento movimentoDeSaida = new MovimentoBuilder()
				.saida()
				.doAlmoxarifado(almoxarifadoOrigem)
				.doProduto(produto)
				.paraGrade(grade)
				.comQuantidade(qtd)
				.daOrigem(origem)
				.doUsuario(usuario)
				.comMotivo(motivo)
				.cria();
		
		this.salva(movimentoDeSaida);
		
		Movimento movimentoDeEntrada = new MovimentoBuilder()
				.entrada()
				.doAlmoxarifado(almoxarifadoDestino)
				.doProduto(produto)
				.paraGrade(grade)
				.comQuantidade(qtd)
				.daOrigem(origem)
				.cria();
		
		this.salva(movimentoDeEntrada);
	}
	
	@Override
	public List<Movimento> listaMovimentosPeloFiltro(FiltroPesquisaMovimento filtro, Paginator paginator) {
		return this.estoqueDAO.listarPeloFiltro(filtro, paginator);
	}
	
	@Override
	public void atualizaHorario() {
		
		for (int i = 700000; i < 800000; i++) {
			
			Movimento mov = this.estoqueDAO.buscarPeloCodigo(new Long(i));
			
			if (null != mov)
			{
				mov.atualizaHorario();
				this.estoqueDAO.salvar(mov);
			}
		}
	}
	
	@Override
	public void estorna(List<? extends IMovimentavel> itens, OrigemMovimento origem) throws RegraDeNegocioException 
	{
		Almoxarifado alm = this.almService.almoxarifadoPrincipal();
		
		for (IMovimentavel item : itens) {
			
			this.entrada(
					alm, 
					item.getProduto(), 
					item.getGrade(), 
					item.getQtd(), 
					origem);
			
			this.verificaDisponibilidadedoProduto(item.getProduto());
			
		}
	}
	
	@Override
	public void movimenta(List<? extends IMovimentavel> itens, OrigemMovimento origem) throws RegraDeNegocioException 
	{
		Almoxarifado alm = this.almService.almoxarifadoPrincipal();
		
		for (IMovimentavel item : itens) {
			
			this.saida(
					alm, 
					item.getProduto(), 
					item.getGrade(), 
					item.getQtd(), 
					origem);
			
			this.verificaDisponibilidadedoProduto(item.getProduto());
			
		}
	}
}
