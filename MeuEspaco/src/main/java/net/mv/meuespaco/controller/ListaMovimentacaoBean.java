package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaMovimento;
import net.mv.meuespaco.model.estoque.Movimento;
import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.estoque.TipoMovimento;
import net.mv.meuespaco.service.EstoqueService;

@Named
@ViewScoped
public class ListaMovimentacaoBean extends ListaSimples implements Serializable{

	private static final long serialVersionUID = -1135064553762646909L;

	@Inject
	private EstoqueService estoqueSrvc;
	
	private List<Movimento> movimentos;
	
	private TipoMovimento[] tipos;
	private OrigemMovimento[] origens;
	
	@Override
	@PostConstruct
	public void init() {
		tipos = TipoMovimento.values();
		origens = OrigemMovimento.values();
		
		this.setFiltro(new FiltroPesquisaMovimento());
	}

	@Override
	public void listarComPaginacao() 
	{
		movimentos = estoqueSrvc.listaMovimentosPeloFiltro(
				(FiltroPesquisaMovimento) this.getFiltro(), this.getPaginator());
	}

	public TipoMovimento[] getTipos() {
		return tipos;
	}

	public OrigemMovimento[] getOrigens() {
		return origens;
	}

	public List<Movimento> getMovimentos() {
		return movimentos;
	}
}
