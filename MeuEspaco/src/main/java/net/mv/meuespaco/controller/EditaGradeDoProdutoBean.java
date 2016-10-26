package net.mv.meuespaco.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.factory.GradeFactory;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.util.FacesUtil;

/**
 * Classe utilizada para edição da grade do produto.
 * 
 * @author Sidronio
 * 30/03/2016
 */
@Named
@ViewScoped
public class EditaGradeDoProdutoBean extends CadastroMasterDetail implements Serializable {

	private static final long serialVersionUID = -645654619819425549L;

	@Inject @Param
	private Long paramCodigo;
	
	@Inject
	private ProdutoService produtoSrvc;
	
	private Produto produto;
	
	private Grade gradeSelecionada;
	private Grade grade;
	private TipoGrade tipoGradeSelecionado;
	
	private TipoGrade [] tiposGrades;
	private Tamanho[] tamanhos;
	private Cor[] cores;
	
	@Override
	@PostConstruct
	public void init() {
		
		if (null != paramCodigo) {

			iniciaDados();
			
			tiposGrades = TipoGrade.values();
			tamanhos = Tamanho.values();
			cores = Cor.values();
		}
		
	}
	
	/**
	 * Inicia ou reinicia os dados.
	 */
	public void iniciaDados() {
		produto = produtoSrvc.buscaPeloCodigoComGradeESubgrupo(paramCodigo);
		novoItem();
	}
	
	/**
	 * Handle da seleção do tipo de grade. Cria a grade de acordo com o 
	 * selecionado. 
	 */
	public void handleSelecaoDeTipoGrade(ValueChangeEvent event) {
		
		if (null == event.getNewValue()) {
			return;
		}
		
		tipoGradeSelecionado = TipoGrade.valueOf((String) event.getNewValue());
	}
	
	/**
	 * Transforma a grade do produto para o novo tipo selecionado.
	 */
	public void transformaGradeDoProduto() {
		
		if (null == tipoGradeSelecionado) {
			FacesUtil.addErrorMessage("Selecione o tipo de grade para a transformação.");
			
			return;
		}
		
		try {
			
			produtoSrvc.alteraGradeDoProduto(produto, tipoGradeSelecionado);
			FacesUtil.addSuccessMessage("Transformação da grade do produto realizada.");
						
			iniciaDados();
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(e.getMessage());
		}
		
		
	}

	@Override
	public boolean isEdicaoDeItem() {
		return null != gradeSelecionada;
	}
	
	@Override
	public void editaItem() {
		grade = gradeSelecionada;
	}
	
	@Override
	String getMensagemDeInclusaoDeItem(String registro) {
		return String.format("Grade %s incluída com sucesso.", registro);
	}
	
	@Override
	String getMensagemDeAlteracaoDeItem(String registro) {
		return String.format("Grade %s alterada com sucesso.", registro);
	}
	
	@Override
	String getMensagemDeExclusaoDeItem(String registro) {
		return String.format("Grade %s excluída com sucesso.", registro);
	}
	
	@Override
	public void adicionaItem() {
		
		try {
			
			if (!isEdicaoDeItem()) 
			{
				produto.adicionaGrade(grade);
			
				FacesUtil.addSuccessMessage(getMensagemDeInclusaoDeItem(grade.toString()));
				
				this.novoItem();
			} else 
			{
				FacesUtil.addSuccessMessage(getMensagemDeAlteracaoDeItem(grade.toString()));
				this.novoItem();
			}

		} catch (RegraDeNegocioException e) {

			FacesUtil.addErrorMessage(String.format("Não foi possível adicionar a grade %s. %s", 
					grade.toString(),
					e.getMessage()));
		}
		
	}
	
	@Override
	public void removeItem() {
		
		try {
		
			produtoSrvc.removeGrade(produto, gradeSelecionada);
			
			FacesUtil.addSuccessMessage(String.format("Grade %s removida com sucesso.", gradeSelecionada.toString()));
			
			novoItem();
		
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}

	}
	
	@Override
	void novoItem() {
		gradeSelecionada = null;
		grade = GradeFactory.cria(produto.getTipoGrade());
	}
	
	@Override
	void novoRegistro() {
		
	}
	
	@Override
	public void salvar() {
		
		try {
			
			produtoSrvc.salva(produto);
			
			FacesUtil.addSuccessMessage(this.getMensagemDeAlteracao(produto.getCodigoInterno()));
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(
					String.format("Não foi possível salvar o produto %s. %s", 
							produto.getCodigoInterno(), 
							e.getMessage()));
		}
		
	}
	
	@Override
	String getMensagemDeInclusao(String registro) {
		return null;
	}
	
	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("A grade do produto %s foi alterada com sucesso.", registro);
	}

	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public TipoGrade[] getTiposGrades() {
		return tiposGrades;
	}

	public Grade getGradeSelecionada() {
		return gradeSelecionada;
	}
	public void setGradeSelecionada(Grade gradeSelecionada) {
		this.gradeSelecionada = gradeSelecionada;
	}

	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Tamanho[] getTamanhos() {
		return tamanhos;
	}

	public Cor[] getCores() {
		return cores;
	}

	public TipoGrade getTipoGradeSelecionado() {
		return tipoGradeSelecionado;
	}
	
}
