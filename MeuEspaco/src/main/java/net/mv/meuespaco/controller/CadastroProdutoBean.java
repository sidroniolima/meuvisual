package net.mv.meuespaco.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.Param;

import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.factory.GradeFactory;
import net.mv.meuespaco.model.Caracteristica;
import net.mv.meuespaco.model.Composicao;
import net.mv.meuespaco.model.Finalidade;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.Subgrupo;
import net.mv.meuespaco.model.TipoGrade;
import net.mv.meuespaco.model.TipoProduto;
import net.mv.meuespaco.model.Unidade;
import net.mv.meuespaco.model.grade.Cor;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.grade.Tamanho;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.service.DepartamentoService;
import net.mv.meuespaco.service.ProdutoService;
import net.mv.meuespaco.service.SubgrupoService;
import net.mv.meuespaco.util.FacesUtil;

@Named
@ViewScoped
public class CadastroProdutoBean extends CadastroMasterDetail implements Serializable {

	private static final long serialVersionUID = -3847806573379448681L;

	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private SubgrupoService subgrupoService;
	
	@Inject
	private DepartamentoService depSrvc;
	
	private Produto produto;
	
	private Grade grade;
	private Grade gradeSelecionada;
	
	@Inject @Param
	private Long paramCodigo;
	
	private TipoGrade[] tiposGrades;
	
	private Tamanho[] tamanhos;
	private Cor[] cores;
	private TipoProduto[] tiposProdutos;
	private Composicao[] composicoes;
	private Unidade[] unidades;
	private Finalidade[] finalidades;
	
	private Caracteristica[] caracteristicas;
	private Caracteristica caracteristica;
	private Caracteristica caracteristicaSelecionada;
	private String valorCaracteristica;
	
	private List<Subgrupo> subgrupos;
	private List<Departamento> departamentos;
	
	@PostConstruct
	@Override
	public void init() {
		
		if (isEdicao()) {
			
			produto = produtoService.buscaPeloCodigoComRelacionamentos(paramCodigo);
			novoItem();
		
		} else {
			novoRegistro();
		}
		
		tiposGrades = TipoGrade.values();
		tamanhos = Tamanho.values();
		cores = Cor.values();
		
		tiposProdutos = TipoProduto.values();
		composicoes = Composicao.values();
		unidades = Unidade.values();
		finalidades = Finalidade.values();
		
		caracteristicas = Caracteristica.values();
		
		subgrupos = subgrupoService.buscaTodosComGrupoEFamilia();
		departamentos = depSrvc.buscaTodas();
	}

	@Override
	void novoRegistro() {
		
		produto = new Produto();
		
		novoItem();
	}

	@Override
	public void salvar() {
		
		try {
			
			this.produtoService.salva(produto);
			
			if(isEdicao()) {
				FacesUtil.addSuccessMessage(getMensagemDeAlteracao(produto.getDescricao()));
			} else {
				FacesUtil.addSuccessMessage(getMensagemDeInclusao(produto.getDescricao()));
			}
			
		} catch (RegraDeNegocioException e) {
			
			FacesUtil.addErrorMessage(e.getMessage());
		}

		novoRegistro();
	}

	@Override
	String getMensagemDeInclusao(String registro) {
		return String.format("Produto %s incluído com sucesso.", registro);
	}

	@Override
	String getMensagemDeAlteracao(String registro) {
		return String.format("Produto %s alterado com sucesso.", registro);
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
		return String.format("Grade %s adicionada ao produto.", registro);
	}

	@Override
	String getMensagemDeAlteracaoDeItem(String registro) {
		return String.format("Grade %s alterada.", registro);
	}

	@Override
	String getMensagemDeExclusaoDeItem(String registro) {
		return String.format("Grade %s removida do produto.", registro);
	}

	@Override
	public void adicionaItem() {
		
		try {
			grade.valida();
			
			if (isEdicaoDeItem()) {
				
				FacesUtil.addSuccessMessage(getMensagemDeAlteracaoDeItem(grade.toString()));
			} else {
				
				produto.adicionaGrade(grade);
				
				FacesUtil.addSuccessMessage(getMensagemDeInclusaoDeItem(grade.toString()));
			}
			
			novoItem();
			
		} catch (RegraDeNegocioException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
	
	}

	@Override
	public void removeItem() {
		produto.removeGrade(gradeSelecionada);
		
		FacesUtil.addSuccessMessage(getMensagemDeExclusaoDeItem(gradeSelecionada.toString()));
		
		novoItem();
	}

	@Override
	void novoItem() {
		
		if (null != produto.getTipoGrade()) {
		
			grade = GradeFactory.cria(produto.getTipoGrade());
			
			gradeSelecionada = null;
		}
	}
	
	/**
	 * Listener da seleção do tipo de grade utilizado 
	 * para criação da grade pela fábrica de acordo com 
	 * o tipo.
	 * 
	 * @param event
	 */
	public void handleSelecaoDeTipoGrade(ValueChangeEvent event) {
		
		TipoGrade tipo = (TipoGrade) event.getNewValue();
		
		grade = GradeFactory.cria(tipo);
		
	}
	
	/**
	 * Adiciona uma característica ao produto.
	 */
	public void adicionaCaracteristica() {
		produto.adicionaCaracteristica(caracteristica, valorCaracteristica);
		
		FacesUtil.addSuccessMessage(
				String.format("Característica %s de valor %s adicionada ao produto.", 
						caracteristica.getDescricao(), valorCaracteristica));
	}

	/**
	 * Remove uma característica do produto.
	 */
	public void removeCaracteristica() {
		produto.removeCaracteristica(caracteristicaSelecionada);
		
		FacesUtil.addSuccessMessage(
				String.format("Característica %s removida do produto.", 
						caracteristicaSelecionada.getDescricao()));
	}
	
	/**
	 * @return the produto
	 */
	public Produto getProduto() {
		return produto;
	}
	/**
	 * @param produto the produto to set
	 */
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	/**
	 * @return the grade
	 */
	public Grade getGrade() {
		return grade;
	}
	/**
	 * @param grade the grade to set
	 */
	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	/**
	 * @return the gradeSelecionada
	 */
	public Grade getGradeSelecionada() {
		return gradeSelecionada;
	}
	/**
	 * @param gradeSelecionada the gradeSelecionada to set
	 */
	public void setGradeSelecionada(Grade gradeSelecionada) {
		this.gradeSelecionada = gradeSelecionada;
	}

	/**
	 * @return the tiposGrades
	 */
	public TipoGrade[] getTiposGrades() {
		return tiposGrades;
	}

	/**
	 * @return the tamanhos
	 */
	public Tamanho[] getTamanhos() {
		return tamanhos;
	}

	/**
	 * @return the cores
	 */
	public Cor[] getCores() {
		return cores;
	}

	/**
	 * @return the subgrupos
	 */
	public List<Subgrupo> getSubgrupos() {
		return subgrupos;
	}

	/**
	 * @return the departamentos
	 */
	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	/**
	 * @return the tiposProdutos
	 */
	public TipoProduto[] getTiposProdutos() {
		return tiposProdutos;
	}

	/**
	 * @return the composicoes
	 */
	public Composicao[] getComposicoes() {
		return composicoes;
	}

	/**
	 * @return the valorCaracteristica
	 */
	public String getValorCaracteristica() {
		return valorCaracteristica;
	}

	/**
	 * @param valorCaracteristica the valorCaracteristica to set
	 */
	public void setValorCaracteristica(String valorCaracteristica) {
		this.valorCaracteristica = valorCaracteristica;
	}
	/**
	 * @return the caracteristicas
	 */
	public Caracteristica[] getCaracteristicas() {
		return caracteristicas;
	}

	/**
	 * @return the caracteristica
	 */
	public Caracteristica getCaracteristica() {
		return caracteristica;
	}
	/**
	 * @param caracteristica the caracteristica to set
	 */
	public void setCaracteristica(Caracteristica caracteristica) {
		this.caracteristica = caracteristica;
	}

	/**
	 * @return the caracteristicaSelecionada
	 */
	public Caracteristica getCaracteristicaSelecionada() {
		return caracteristicaSelecionada;
	}
	/**
	 * @param caracteristicaSelecionada the caracteristicaSelecionada to set
	 */
	public void setCaracteristicaSelecionada(Caracteristica caracteristicaSelecionada) {
		this.caracteristicaSelecionada = caracteristicaSelecionada;
	}

	/**
	 * @return the unidades
	 */
	public Unidade[] getUnidades() {
		return unidades;
	}

	public Finalidade[] getFinalidades() {
		return finalidades;
	}
}
