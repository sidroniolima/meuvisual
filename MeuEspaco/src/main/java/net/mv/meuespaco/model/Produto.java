package net.mv.meuespaco.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.Vetoed;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import net.mv.meuespaco.converter.LocalDateDBConverter;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.grade.Grade;
import net.mv.meuespaco.model.loja.Departamento;
import net.mv.meuespaco.util.IConstants;

@Entity
@Table(name="produto")
@Vetoed
public class Produto extends EntidadeModel implements Serializable {

	private static final long serialVersionUID = -5236056596359508004L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Convert(converter=LocalDateDBConverter.class)
	@Column(name="data_de_cadastro", columnDefinition="DATE")
	private LocalDate dataDeCadastro;
	
	@Column(columnDefinition = "boolean default false")
	private boolean ativo;
	
	@Column(length=60)
	private String descricao;	
	
	@Column(length=20)
	private String localizacao;
	
	@Column(name="codigo_interno", length=60)
	private String codigoInterno;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Subgrupo subgrupo;
	
	@Column(name="estoque_reposicao")
	private BigDecimal estoqueReposicao;
	
	@Column(name="estoque_maximo")
	private BigDecimal estoqueMaximo;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_grade")
	private TipoGrade tipoGrade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="departamento_codigo")
	private Departamento departamento;
	
	@Enumerated(EnumType.STRING)
	@Column(name="tipo_produto")
	private TipoProduto tipoProduto;
	
	@Enumerated(EnumType.STRING)
	@Column(name="composicao")
	private Composicao composicao;	
	
	@OneToMany(
			fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL,
			orphanRemoval=true,
			mappedBy="produto")
	private List<Grade> grades;
	
	@ElementCollection(fetch=FetchType.LAZY)
	@JoinTable(name="produto_caracteristicas", joinColumns=@JoinColumn(name="produto_codigo"))
	@MapKeyColumn(name="caracteristica")
	@MapKeyEnumerated(EnumType.STRING)
	@Column(name="valor")
	private Map<Caracteristica, String> caracteristicas;
	
	@Enumerated(EnumType.STRING)
	private Unidade unidade;
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="foto_de_produtos", joinColumns={@JoinColumn(name="produto_codigo")})
	@Column(name="foto")
	@Fetch(FetchMode.SELECT)
	private Set<String> fotos;
	
	@ManyToMany(fetch=FetchType.LAZY, 
			cascade=CascadeType.ALL)
	@JoinTable(name="produtos_relacionados", 
				joinColumns={@JoinColumn(name="produto_codigo")},
				inverseJoinColumns={@JoinColumn(name="produto_relacionado_codigo")})
	private Set<Produto> produtosRelacionados;
	
	@Column(columnDefinition = "TEXT")
	private String detalhes;
	
	@Column(columnDefinition = "TEXT")
	private String cuidados;
	
	@Column(columnDefinition = "TEXT")
	private String marketing;
	
	@Enumerated(EnumType.STRING)
	private Finalidade finalidade;

	@Column
	private BigDecimal valor;
	
	/**
	 * Construtor padrão.
	 */
	public Produto() {	
		dataDeCadastro = LocalDate.now();
		finalidade = Finalidade.CONSIGNADO;
	}
	
	/**
	 * Construtor pelo código e descrição.
	 * 
	 * @param codigo
	 * @param descricao
	 */
	public Produto(long codigo, String descricao) {
		this();
		
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	/**
	 * Construtor pelo código, descrição e código interno.
	 * 
	 * @param codigo
	 * @param descricao
	 * @param codigoInterno
	 */
	public Produto(long codigo, String descricao, String codigoInterno) {
		this();
		
		this.codigo = codigo;
		this.descricao = descricao;
		this.codigoInterno = codigoInterno;
	}
	
	/**
	 * Adiciona um Grade já formada ao produto.
	 * 
	 * @param grade
	 * @throws RegraDeNegocioException Se tentar adicionar mais de uma grade única.
	 */
	public void adicionaGrade(Grade grade) throws RegraDeNegocioException {
		
		if (null == grades) {
			grades = new ArrayList<Grade>();
		}
		
		if (null == tipoGrade) {
			tipoGrade = grade.tipoDeGrade();
		}
		
		if (grade.tipoDeGrade().equals(TipoGrade.UNICA)) {
			if (this.grades.size() > 0) {
				throw new RegraDeNegocioException("É permitido uma grade única apenas.");
			}
		}
		
		grade.setProduto(this);
		grades.add(grade);
	}
	
	/**
	 * Indica se o produto tem Grade.
	 * 
	 * @return
	 */
	public boolean temGrade() {
		return this.tamanhoDaGrade() > 0;
	}
	
	/**
	 * Retorna o tamanho da grade.
	 * 
	 * @return
	 */
	public int tamanhoDaGrade() {
		
		if (null == grades) {
			return 0;
		}
		
		return grades.size();
	}
	
	/**
	 * Remove da a grade das grades pertencentes ao 
	 * produto.
	 * 
	 * @param grade
	 */
	public void removeGrade(Grade grade) {
		grades.remove(grade);
	}
	
	@Override
	public void valida() throws RegraDeNegocioException {
		
	}
	
	/**
	 * Insere uma característica ao produto.
	 * 
	 * @param caracteristica Característica especifica.
	 * @param valor Valor da característica.
	 */
	public void adicionaCaracteristica(Caracteristica caracteristica, String valor) {
		
		if (null == caracteristicas) {
			caracteristicas = new HashMap<Caracteristica, String>();
		}
		
		caracteristicas.put(caracteristica, valor);
	}
	
	/**
	 * Remove a característica do produto.
	 * 
	 * @param caracteristica Chave para remoção.
	 */
	public void removeCaracteristica(Caracteristica caracteristica) {
		caracteristicas.remove(caracteristica);
	}
	
	/**
	 * Inativa um produto para que não seja mais exibido 
	 * nas listagens.
	 */
	public void inativa() {
		this.setAtivo(false);
	}
	
	/**
	 * Ativa um produto para que possa ser visualizado em 
	 * listagens.
	 */
	public void ativa() {
		this.setAtivo(true);
	}
	
	/**
	 * Adiciona uma foto ao produto.
	 * 
	 * @param nomeDaFoto
	 */
	public void adicionaFoto(String nomeDaFoto) {
	
		if (null == fotos) {
			fotos = new HashSet<String>();
		}
		
		fotos.add(nomeDaFoto);
	}
	
	/**
	 * Remove uma foto do produto.
	 * 
	 * @param nomeDaFoto
	 */
	public void removeFoto(String nomeDaFoto) throws RegraDeNegocioException {
		
		if (!fotos.contains(nomeDaFoto)) {
			throw new RegraDeNegocioException("Foto não encontrado no produto.");
		}
		
		fotos.remove(nomeDaFoto);
		
	}
	
	/**
	 * Adiciona um produto aos relacionados.
	 * 
	 * @param produtoRelacionado Produto relacionado.
	 * @throws RegraDeNegocioException Lança exceção caso o produto seja nulo ou
	 * 	já fora relacionado.
	 */
	public void adicionaRelacao(Produto produtoRelacionado) throws RegraDeNegocioException {
		
		if (null == produtoRelacionado) {
			throw new RegraDeNegocioException("O produto é inválido.");
		}
		
		if (this.equals(produtoRelacionado)) {
			throw new RegraDeNegocioException("Um produto não pode estar relacionado com ele mesmo.");
		}
		
		if (produtosRelacionados.contains(produtoRelacionado)) {
			throw new RegraDeNegocioException("O produto já foi relacionado.");
		}
		
		this.produtosRelacionados.add(produtoRelacionado);
	}
	
	/**
	 * Remove um produto daqueles relacionados.
	 * 
	 * @param produtoRelacionado Produto não mais relacionado
	 * @throws RegraDeNegocioException Lança exceção caso o produto seja nulo ou
	 * não estiver relacionado.
	 */
	public void removeRelacionamento(Produto produtoRelacionado) throws RegraDeNegocioException {

		if (null == produtoRelacionado) {
			throw new RegraDeNegocioException("O produto é inválido.");
		}
		
		if (!produtosRelacionados.contains(produtoRelacionado)) {
			throw new RegraDeNegocioException("O produto não está relacionado.");
		}
		
		this.produtosRelacionados.remove(produtoRelacionado);
		
	}
	
	/**
	 * Busca a foto, isto é, o nome do arquivo da foto, pelo índice. 
	 * O layout exige a busca pelo índice pois trata as fotos às vezes 
	 * de maneira específica. Caso não haja o índice, o que significa que 
	 * não há a quantidade exigida de fotos para o produto é retornada o 
	 * nome do arquivo de "produto sem foto". 
	 * 
	 * @param indice Índice da foto requerida.
	 * @return Nome do arquivo da foto solicitada. 
	 */
	/**
	 * @param indice
	 * @return
	 */
	public String buscaFotoPeloIndice(int indice) {
		
		if (fotos.isEmpty() || null == fotos) {
			return IConstants.PRODUTO_SEM_FOTO;
		}
		
		if (fotos.size() <= indice - 1) {
			return fotos.stream().findFirst().get();
		}
		
		int index = 1;
		
		for (String foto : fotos) {

			if (indice == index) {
				return foto;
			}
			
			index++;
		}
		
		return null;
	}
	
	/**
	 * Verifica se o produto pode sofrer alteração na grade. 
	 * As restrições são: o produto deve possuir apenas a grade do 
	 * tipo Única. 
	 * 
	 * @return true se puder, false caso contrário.
	 */
	public boolean podeAlterarGrade() {
		
		if (!this.tipoGrade.equals(TipoGrade.UNICA)) {
			return false;
		}
		
		if (this.grades.size() > 1) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Verifica se o produto pertence a um grupo que é 
	 * descontado da quantidade permitida para escolha ou não.
	 * 
	 * @return Se é ou não descontável.
	 * @throws RegraDeNegocioException Lança uma exceção caso não consiga 
	 * verificar por não ter informações do grupo.
	 */
	@Transient
	public boolean isDescontavel() throws RegraDeNegocioException 
	{
		
		Grupo grupo = this.subgrupo.getGrupo();
		
		if (null == grupo) {
			throw new RegraDeNegocioException("Não foi possível acessor o grupo do produto.");
		}
		
		return grupo.isDescontavel();
	}
	
	/**
	 * Informa o valor do produto baseado em seu código interno.
	 * 
	 * @return Valor do produto.
	 */
	public BigDecimal valor() {
		String valor = this.codigoInterno.substring(10, this.codigoInterno.length());
		return new BigDecimal(valor).divide(new BigDecimal(100));
	}
	
	public BigDecimal getValor() 
	{
		return valor;
	}
	public void setValor(BigDecimal valor) 
	{
		this.valor = valor;
	}

	/**
	 * @return the codigo
	 */
	public Long getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the ativo
	 */
	public boolean isAtivo() {
		return ativo;
	}
	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
	/**
	 * @return the dataDeCadastro
	 */
	public LocalDate getDataDeCadastro() {
		return dataDeCadastro;
	}
	/**
	 * @param dataDeCadastro the dataDeCadastro to set
	 */
	public void setDataDeCadastro(LocalDate dataDeCadastro) {
		this.dataDeCadastro = dataDeCadastro;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}
	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the localizacao
	 */
	public String getLocalizacao() {
		return localizacao;
	}
	/**
	 * @param localizacao the localizacao to set
	 */
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	
	/**
	 * @return the codigoInterno
	 */
	public String getCodigoInterno() {
		return codigoInterno;
	}
	/**
	 * @param codigoInterno the codigoInterno to set
	 */
	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}
	
	/**
	 * @return the subgrupo
	 */
	public Subgrupo getSubgrupo() {
		return subgrupo;
	}
	/**
	 * @param subgrupo the subgrupo to set
	 */
	public void setSubgrupo(Subgrupo subgrupo) {
		this.subgrupo = subgrupo;
	}
	
	/**
	 * @return the estoqueReposicao
	 */
	public BigDecimal getEstoqueReposicao() {
		return estoqueReposicao;
	}
	/**
	 * @param estoqueReposicao the estoqueReposicao to set
	 */
	public void setEstoqueReposicao(BigDecimal estoqueReposicao) {
		this.estoqueReposicao = estoqueReposicao;
	}
	
	/**
	 * @return the estoqueMaximo
	 */
	public BigDecimal getEstoqueMaximo() {
		return estoqueMaximo;
	}
	/**
	 * @param estoqueMaximo the estoqueMaximo to set
	 */
	public void setEstoqueMaximo(BigDecimal estoqueMaximo) {
		this.estoqueMaximo = estoqueMaximo;
	}
	
	/**
	 * @return the tipoGrade
	 */
	public TipoGrade getTipoGrade() {
		return tipoGrade;
	}
	/**
	 * @param tipoGrade the tipoGrade to set
	 */
	public void setTipoGrade(TipoGrade tipoGrade) {
		this.tipoGrade = tipoGrade;
	}
	
	/**
	 * @return the departamento
	 */
	public Departamento getDepartamento() {
		return departamento;
	}
	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	
	/**
	 * @return the tipoProduto
	 */
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}
	/**
	 * @param tipoProduto the tipoProduto to set
	 */
	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	
	/**
	 * @return the composicao
	 */
	public Composicao getComposicao() {
		return composicao;
	}
	/**
	 * @param composicao the composicao to set
	 */
	public void setComposicao(Composicao composicao) {
		this.composicao = composicao;
	}

	/**
	 * @return the grades
	 */
	public List<Grade> getGrades() {
		return grades;
	}
	/**
	 * @param grades the grades to set
	 */
	public void setGrades(List<Grade> grades) {
		this.grades = grades;
	}

	/**
	 * @return the caracteristicas
	 */
	public Map<Caracteristica, String> getCaracteristicas() {
		return caracteristicas;
	}
	/**
	 * @param caracteristicas the caracteristicas to set
	 */
	public void setCaracteristicas(Map<Caracteristica, String> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	
	/**
	 * @return the unidade
	 */
	public Unidade getUnidade() {
		return unidade;
	}
	/**
	 * @param unidade the unidade to set
	 */
	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}
	
	/**
	 * @return the fotos
	 */
	public Set<String> getFotos() {
		return fotos;
	}
	/**
	 * @param fotos the fotos to set
	 */
	public void setFotos(HashSet<String> fotos) {
		this.fotos = fotos;
	}

	/**
	 * @return the produtosRelacionados
	 */
	public Set<Produto> getProdutosRelacionados() {
		return produtosRelacionados;
	}
	/**
	 * @param produtosRelacionados the produtosRelacionados to set
	 */
	public void setProdutosRelacionados(Set<Produto> produtosRelacionados) {
		this.produtosRelacionados = produtosRelacionados;
	}
	
	/**
	 * @return the detalhes
	 */
	public String getDetalhes() {
		return detalhes;
	}
	/**
	 * @param detalhes the detalhes to set
	 */
	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}

	/**
	 * @return the cuidados
	 */
	public String getCuidados() {
		return cuidados;
	}
	/**
	 * @param cuidados the cuidados to set
	 */
	public void setCuidados(String cuidados) {
		this.cuidados = cuidados;
	}

	/**
	 * @return the marketing
	 */
	public String getMarketing() {
		return marketing;
	}
	/**
	 * @param marketing the marketing to set
	 */
	public void setMarketing(String marketing) {
		this.marketing = marketing;
	}

	public Finalidade getFinalidade() {
		return finalidade;
	}
	public void setFinalidade(Finalidade finalidade) {
		this.finalidade = finalidade;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Código: %s - Descrição: %s", this.getCodigo(), this.getDescricao()));
		builder.append("\nGrade");
		
		grades.forEach(g -> builder.append("\n" + g.toString()));
		
		return builder.toString();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

}
