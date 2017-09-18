package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.mv.meuespaco.controller.filtro.FiltroPesquisaMovimento;
import net.mv.meuespaco.model.consulta.ReferenciaProdutoComQtd;
import net.mv.meuespaco.model.estoque.OrigemMovimento;
import net.mv.meuespaco.model.estoque.TipoMovimento;
import net.mv.meuespaco.service.EstoqueService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ConversationScoped
public class RelatorioEntradaProdutoPorHorarioBean implements Serializable 
{
	private static final long serialVersionUID = -4230331341798014722L;

	private static final String FILTRO_NAO_PREENCHIDO = "O filtro deve ser preenchido para a consulta.";

	private static final String LISTA_VAZIA = "Não foi retornado nenhum resultado para o filtro. Tente novamente.";

	private static final String FILTRO_NAO_COMPOSTO = "Para esse relatório é necessário preencher a data e hora inicial e final.";
	
	@Inject
	private Conversation conversation;
	
	@Inject
	private EstoqueService estoqueSrvc;
	private List<ReferenciaProdutoComQtd> referencias;
	private FiltroPesquisaMovimento filtro;
	
	private OrigemMovimento[] origens;
	private TipoMovimento[] tipos;
	
	public RelatorioEntradaProdutoPorHorarioBean() 
	{
		referencias = new ArrayList<ReferenciaProdutoComQtd>();
		filtro = new FiltroPesquisaMovimento();
		origens = OrigemMovimento.values();
		tipos = TipoMovimento.values();
	}
	
	public void listar()
	{
		if (!filtro.isPreenchido())
		{
			FacesUtil.addErrorMessage(FILTRO_NAO_PREENCHIDO);
			return;
		}
		
		if (!filtro.isPeriodoComposto())
		{
			FacesUtil.addErrorMessage(FILTRO_NAO_COMPOSTO);
			return;
		}

		referencias = this.estoqueSrvc.listaReferenciasAgruapadasDeMovimentacao(filtro); 
		
		if (referencias.isEmpty())
		{
			FacesUtil.addErrorMessage(LISTA_VAZIA);
		}
	}
	
	public void beginConversation()
	{
	    if (conversation.isTransient() == false){
	        conversation.end();
	    }
	    conversation.begin();
	}
	
	public String imprimir()
	{	
		return "./relatorio-entrada-referencia-por-horario-print.xhtml";
	}
	
	public List<ReferenciaProdutoComQtd> getReferencias() 
	{
		return referencias;
	}

	public FiltroPesquisaMovimento getFiltro() {
		return filtro;
	}
	public void setFiltro(FiltroPesquisaMovimento filtro) {
		this.filtro = filtro;
	}

	public OrigemMovimento[] getOrigens() {
		return origens;
	}

	public TipoMovimento[] getTipos() {
		return tipos;
	}
	
	public Conversation getConversation() 
	{
		return conversation;
	}
}
