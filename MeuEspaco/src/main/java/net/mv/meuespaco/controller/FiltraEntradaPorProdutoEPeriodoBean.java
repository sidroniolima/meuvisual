package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroDePesquisa;
import net.mv.meuespaco.controller.filtro.FiltroEntradaProdutos;
import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.consulta.MovimentoPorComposicaoSubgrupo;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.service.SubgrupoService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ConversationScoped
public class FiltraEntradaPorProdutoEPeriodoBean extends ListaSimples implements Serializable
{
	private static final long serialVersionUID = 3855803300739845052L;

	private static final String ERROR_FILTRO_NAO_PREENCHIDO = "Não foi possível realizar a consulta. Redefina o filtro e tente novamente.";
	
	@Inject
	private Conversation conversation;
	
	@Inject
	private EstoqueService estoqueSrvc;
	
	@Inject
	private SubgrupoService subgrupoSrvc;
	
	private FiltroEntradaProdutos filtro;
	
	private List<MovimentoPorComposicaoSubgrupo> movimentacao;
	
	private Composicao[] composicoes;
	private List<Subgrupo> subgrupos;
	
	public FiltraEntradaPorProdutoEPeriodoBean() 
	{
		this.filtro = new FiltroEntradaProdutos();
		this.movimentacao = new ArrayList<MovimentoPorComposicaoSubgrupo>();
	}
	
	@Override
	@PostConstruct
	public void init() 
	{
		this.composicoes = Composicao.values();
		this.subgrupos = subgrupoSrvc.buscaTodosComGrupoEFamilia();
	}

	public void beginConversation()
	{
	    if (conversation.isTransient() == false){
	        conversation.end();
	    }
	    conversation.begin();
	}	
	
	@Override
	public void listarComPaginacao() 
	{
		if (this.filtro.isPreenchido())
		{
			movimentacao = this.estoqueSrvc.agrupaMovimentosPeloFiltro(filtro, this.getPaginator());
		} else 
		{
			FacesUtil.addErrorMessage(ERROR_FILTRO_NAO_PREENCHIDO);
		}
	}
	
	public String imprimir()
	{	
		return "./mov-por-subgrupo-composicao-print.xhtml";
	}

	@Override
	public FiltroDePesquisa getFiltro() 
	{
		return this.filtro;
	}
	public void setFiltro(FiltroEntradaProdutos filtro) {
		this.filtro = filtro;
	}

	public List<MovimentoPorComposicaoSubgrupo> getMovimentacao() {
		return movimentacao;
	}

	public Composicao[] getComposicoes() {
		return composicoes;
	}

	public List<Subgrupo> getSubgrupos() {
		return subgrupos;
	}
	
	public Conversation getConversation() 
	{
		return conversation;
	}
}
