package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroDePesquisa;
import net.mv.meuespaco.controller.filtro.FiltroPesquisaMovimento;
import net.mv.meuespaco.model.estoque.Movimento;
import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.estoque.TipoMovimento;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class ListaMovimentacaoBean extends ListaSimples implements Serializable{

	private static final long serialVersionUID = -1135064553762646909L;

	private static final String SEM_REGISTROS_PARA_O_FILTRO = "Não foi encontrado nenhum resultado para esse filtro.";

	private static final String FILTRO_NAO_PREENCHIDO = "É necessário preencher o período, a origem e o tipo no filtro.";

	@Inject
	private EstoqueService estoqueSrvc;
	
	private List<Movimento> movimentos  = new ArrayList<Movimento>();
	
	private TipoMovimento[] tipos;
	private OrigemMovimento[] origens;
	
	private FiltroPesquisaMovimento filtro;
	
	@Override
	@PostConstruct
	public void init() 
	{
		filtro = new FiltroPesquisaMovimento();
		
		tipos = TipoMovimento.values();
		origens = OrigemMovimento.values();
	}

	@Override
	public void listarComPaginacao() 
	{
		if (!((FiltroPesquisaMovimento) this.getFiltro()).isPreenchido())
		{
			FacesUtil.addErrorMessage(FILTRO_NAO_PREENCHIDO);
			return;
		}
		
		movimentos = estoqueSrvc.listaMovimentosPeloFiltro(
				(FiltroPesquisaMovimento) this.getFiltro(), this.getPaginator());
		
		if (this.movimentos.isEmpty())
		{
			FacesUtil.addErrorMessage(SEM_REGISTROS_PARA_O_FILTRO);
		}
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
	
	@Override
	public FiltroDePesquisa getFiltro() 
	{
		return this.filtro;
	}
}
