package net.mv.meuespaco.model.loja;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.enterprise.inject.Vetoed;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.mv.meuespaco.converter.LocalDateTimeDBConverter;
import net.mv.meuespaco.exception.RegraDeNegocioException;
import net.mv.meuespaco.model.EntidadeModel;
import net.mv.meuespaco.model.Produto;
import net.mv.meuespaco.model.grade.Grade;

/**
 * Entidade que representa uma Escolha de um cliente.
 * 
 * @author Sidronio
 * 08/01/2015
 */
/**
 * @author Sidronio
 *
 */
/**
 * @author sidronio
 *
 */
@Entity
@Vetoed
@Table(name="escolha")
public class Escolha extends EntidadeModel implements Serializable {

	private static final long serialVersionUID = 5716724944541807340L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status_escolha")
	private StatusEscolha status;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Cliente cliente;
	
	@Convert(converter=LocalDateTimeDBConverter.class)
	@Column(name="data", columnDefinition="DATETIME")
	private LocalDateTime data;
	
	@Convert(converter=LocalDateTimeDBConverter.class)
	@Column(name="data_finalizacao", columnDefinition="DATETIME")
	private LocalDateTime dataFinalizacao;
	
	@Convert(converter=LocalDateTimeDBConverter.class)
	@Column(name="data_envio", columnDefinition="DATETIME")
	private LocalDateTime dataEnvio;
	
	@OneToMany(
			fetch=FetchType.LAZY,
			mappedBy="escolha",
			cascade={CascadeType.ALL},
			orphanRemoval=true)
	private List<ItemEscolha> itens;
	
	@Column(name="qtd_maxima_permitida")
	private BigDecimal qtdMaximaPermitida;
	
	@Column(name="valor_maximo_permitido")
	private BigDecimal valorMaximoPermitido;
	
	private String observacao;
	
	/**
	 * Cria um escolha com a data atual e o status 
	 * INICIAL
	 */
	public Escolha() {
		this.status = StatusEscolha.PARCIAL;
		this.data = LocalDateTime.now();
		this.itens = new ArrayList<ItemEscolha>();
	}
	
	/**
	 * Constrói um Escolha pelo cliente.
	 * 
	 * @param cliente
	 */
	public Escolha(Cliente cliente) {
		this();
		this.cliente = cliente;
	}

	/**
	 * Adiciona um item pelo produto e quantidade informados.
	 * 
	 * @param produto
	 * @param qtd
	 * @throws RegraDeNegocioException 
	 */
	public void adicionaItem(Produto produto, BigDecimal qtd, Grade grade) throws RegraDeNegocioException{
		
		ItemEscolha item = new ItemEscolha(produto, qtd, grade);
		
		item.valida();
		
		item.setEscolha(this);
		itens.add(item);
	}
	
	/**
	 * Calcula a quantidade de itens do escolha. 
	 * 
	 * @return Quantidade de itens.
	 */
	public BigDecimal qtdDeItens() {
		
		return itens
				.stream()
				.map(ItemEscolha::getQtd)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula a quantidade de itens descontáveis, isto é, o 
	 * que não é material de apoio.
	 * 
	 * @return Qtd de itens descontáveis.
	 * @throws RegraDeNegocioException Lança uma exceção caso não seja possível verificar
	 * se o produto é descontável, por não ser possível acessar esta informação do grupo. 
	 */
	public BigDecimal qtdDeItensDescontaveis() throws RegraDeNegocioException {
		BigDecimal soma = BigDecimal.ZERO;
		
		for (ItemEscolha item : itens) {
			
			if (item.getProduto().isDescontavel()) {
				soma = soma.add(item.getQtd());
			}
		}
		
		return soma;
	}
	
	/**
	 * Calcula a quantidade de itens descontáveis, isto é, o 
	 * que é material de apoio.
	 * 
	 * @return Qtd de itens não descontáveis.
	 * @throws RegraDeNegocioException Lança uma exceção caso não seja possível verificar
	 * se o produto é ou não descontável, por não ser possível acessar esta informação do grupo. 
	 */
	public BigDecimal qtdDeItensNaoDescontaveis() throws RegraDeNegocioException {
		
		BigDecimal soma = BigDecimal.ZERO;
		
		for (ItemEscolha item : itens) {
			
			if (!item.getProduto().isDescontavel()) {
				soma = soma.add(item.getQtd());
			}
		}
		
		return soma;
		
	}
	
	/**
	 * Valida um escolha verificando o cliente, a data e 
	 * se há ao menos um item.
	 * 
	 * @throws RegraDeNegocioException
	 */
	public void valida() throws RegraDeNegocioException {
		
		if (null == cliente) {
			throw new RegraDeNegocioException("A escolha deve pertencer a um cliente.");
		}
		
		if (null == data) {
			throw new RegraDeNegocioException("A escolha deve ter uma data.");
		}
		
		if (!this.temItens()) {
			throw new RegraDeNegocioException("A escolha deve conter ao menos um item.");
		}		
	}
	
	/**
	 * Verifica se a escolha tem itens.
	 * 
	 * @return True se a qtd de itens for maior que 0.
	 */
	private boolean temItens() {
		return this.qtdDeItens().compareTo(BigDecimal.ZERO) > 0;
	}
	
	/**
	 * Adiciona os itens do carrinho à Escolha ou atualiza os produtos 
	 * já inseridos.
	 * 
	 * @param carrinho
	 * @throws RegraDeNegocioException
	 */
	public void adicionaItensDoCarrinho(List<ItemCarrinho> carrinho) throws RegraDeNegocioException 
	{
		for (ItemCarrinho item : carrinho) 
		{
			if (!this.atualizaItem(item)) 
			{
				this.adicionaItem(item.getProduto(), item.getQtd(), item.getGrade());
			}
		}
		
		this.atualizaSituacao();
	}
	
	/**
	 * Atualiza a data de envio e o status da escolha de acordo com seus itens, 
	 * alterando-o para enviada caso já tenha atingido a qtd máxima.
	 * 
	 * @throws RegraDeNegocioException caso não seja possível calcular a qtd de 
	 * itens descontáveis. 
	 */
	private void atualizaSituacao() throws RegraDeNegocioException 
	{
		if (this.getQtdMaximaPermitida().compareTo(this.qtdDeItensDescontaveis()) == 0)
		{
			status = StatusEscolha.ENVIADA;
			dataEnvio = LocalDateTime.now();
		}
	}

	/**
	 * Atualiza a quantidade de um item já inserido para 
	 * a mesma grade.
	 * 
	 * @param itemCarrinho
	 * @return
	 */
	private boolean atualizaItem(ItemCarrinho itemCarrinho) {
		
		for (ItemEscolha itemEscolha : this.getItens()) {
			
			if (itemEscolha.getProduto().equals(itemCarrinho.getProduto()) && 
					itemEscolha.getGrade().equals(itemCarrinho.getGrade())) {
				
				itemEscolha.atualizaQtd(itemCarrinho.getQtd());
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Finaliza a escolha ajsutando a data e hora de finalização 
	 * e seu status. 
	 */
	public void finaliza() 
	{		
		this.dataFinalizacao = LocalDateTime.now();
		this.status = StatusEscolha.FINALIZADA;
	}

	/**
	 * Verifica se os todos os itens da Escolha foram atendidos 
	 * de forma completa ou parcial.
	 * 
	 * @return True se completamente atendidos.
	 */
	public boolean foramTodosOsItensAtendidos() {
	
		for (ItemEscolha item : itens) {
			
			if (!item.isAtendido()) {
				return false;
			}
			
		}

		return true;
	}
	
	/**
	 * Verifica se a escolha pode ser atendida, ao verificar 
	 * se seu status está Enviada e ainda não foi atendida; ou 
	 * se está em separação, caso em que foi parciamente atendida e será 
	 * continuada. Demais status não permitirão atender.
	 * 
	 * @return
	 */
	public boolean isPodeSerAtendida() {
		
		if (getStatus().equals(StatusEscolha.ENVIADA) || 
				getStatus().equals(StatusEscolha.EM_SEPARACAO)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Calcula a quantidade de itens atendidos.
	 * @return
	 */
	public BigDecimal qtdDeItensAtendidos() {
	
		BigDecimal soma = BigDecimal.ZERO;
		
		for (ItemEscolha i : itens) {
			soma = soma.add(i.getQtdAtendido());
		}
		
		return soma;
	}
	
	/**
	 * Atende todos os itens da escolha de uma só vez.
	 */
	public void atendeTodosItens() {
		
		for (ItemEscolha itemEscolha : itens) {
			itemEscolha.atendeOuCancelaAtendimento();
		}
	}
	
	/**
	 * Atende a escolha e forma parcial e permite retornar.
	 */
	public void atendeParcialmente()
	{
		this.setStatus(StatusEscolha.EM_SEPARACAO);
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
	 * @return the status
	 */
	public StatusEscolha getStatus() {
		
		if (null == status) {
			
			if (cliente.getQtdDePecasParaEscolha() == this.qtdDeItens().doubleValue()) {
				return StatusEscolha.ENVIADA;
			}
			
			if (dataEnvio.isBefore(LocalDateTime.now())) {
				return StatusEscolha.ENVIADA;
			} else {
				return StatusEscolha.PARCIAL;
			}
			
		}
		
		return status;
	}
	
	/**
	 * Calcula o saldo do valor que é permitido para a escolha do que 
	 * já foi escolhido. Considerando os itens descontáveis.
	 * 
	 * @return Saldo do valor permitido.
	 * @throws RegraDeNegocioException 
	 */
	public BigDecimal calculaSaldoDoValorPermitido() throws RegraDeNegocioException {
		return valorMaximoPermitido.subtract(this.valorDosItensDescontaveis());
	}
	
	/**
	 * Calcula o valor dos itens da Escolha.
	 * 
	 * @return
	 */
	public BigDecimal valorDosItens() {
		
		return this.getItens()
				.stream()
				.map(i -> i.valorTotal())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * Calcula o valor dos itens descontáveis utilizado 
	 * nos cálculos para limite de peças escolhidas.
	 * 
	 * @return Valor dos itens descontáveis.
	 * @throws RegraDeNegocioException Lança uma exceção caso naõ seja possível verificar 
	 * se o produto é ou não descontável.
	 */
	public BigDecimal valorDosItensDescontaveis() throws RegraDeNegocioException {
		
		BigDecimal soma = BigDecimal.ZERO;
		
		for (ItemEscolha item : this.getItens()) 
		{
			if (item.isDescontavel()) 
			{
				soma = soma.add(item.valorTotal());
			}
		}
		
		return soma;
	}	
	
	/**
	 * Calcula o valor dos itens atendidos.
	 * 
	 * @return valor dos itens atendidos.
	 */
	public BigDecimal valorDosItensAtendidos()
	{
		return this.itens
			.stream()
			.map(ItemEscolha::valorAtendido)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusEscolha status) {
		this.status = status;
	}
	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	/**
	 * @return the data
	 */
	public LocalDateTime getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(LocalDateTime data) {
		this.data = data;
	}

	/**
	 * @return the dataFinalizacao
	 */
	public LocalDateTime getDataFinalizacao() {
		return dataFinalizacao;
	}
	/**
	 * @param dataFinalizacao the dataFinalizacao to set
	 */
	public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
		this.dataFinalizacao = dataFinalizacao;
	}
	
	/**
	 * @return the dataEnvio
	 */
	public LocalDateTime getDataEnvio() {
		return dataEnvio;
	}
	/**
	 * @param dataEnvio the dataEnvio to set
	 */
	public void setDataEnvio(LocalDateTime dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	/**
	 * @return the itens
	 */
	public List<ItemEscolha> getItens() {
		return itens;
	}
	/**
	 * @param itens the itens to set
	 */
	public void setItens(List<ItemEscolha> itens) {
		this.itens = itens;
	}

	/**
	 * @return the observacao
	 */
	public String getObservacao() {
		return observacao;
	}
	/**
	 * @param observacao the observacao to set
	 */
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public BigDecimal getQtdMaximaPermitida() {
		return qtdMaximaPermitida;
	}
	public void setQtdMaximaPermitida(BigDecimal qtdMaximaPermitida) {
		this.qtdMaximaPermitida = qtdMaximaPermitida;
	}

	public BigDecimal getValorMaximoPermitido() {
		return valorMaximoPermitido;
	}
	public void setValorMaximoPermitido(BigDecimal valorMaximoPermitido) {
		this.valorMaximoPermitido = valorMaximoPermitido;
	}
	
	@Override
	public String toString() 
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		StringJoiner joiner = new StringJoiner(";");
		joiner.add(this.codigoFormatado());
		joiner.add(this.getCliente().getCodigoSiga());
		joiner.add(this.getData().format(dtf));
		joiner.add(this.qtdDeItens().toString());
		joiner.add(this.valorDosItens().toString());
		
		return joiner.toString();
	}
	
	/**
	 * Gera as informações da escolha com quantidade e valor
	 * dos itens atendidos.
	 * 
	 * @return
	 */
	public String toCsv() 
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		StringJoiner joiner = new StringJoiner(";");
		joiner.add(this.codigoFormatado());
		joiner.add(this.getCliente().getCodigoSiga());
		joiner.add(this.getCliente().getNome());
		joiner.add(this.getData().format(dtf));
		joiner.add(this.qtdDeItensAtendidos().toString());
		joiner.add(this.valorDosItensAtendidos().toString());
		
		return joiner.toString();
	}

	/**
	 * Gera a string no formato csv das informações da escolha e dos itens.
	 * 
	 * @return string no formato csv
	 */
	public String generateCsv()
	{
		String escolha = this.toCsv().concat(";");
		StringBuilder builder = new StringBuilder();
		
		if (null == this.itens)
		{
			return builder.toString();
		}
		
		this.itens
			.stream()
			.sorted((i1,i2) -> i1.getProduto().getCodigoInterno().compareTo(i2.getProduto().getCodigoInterno()) )
			.filter(ItemEscolha::isAtendido)
			.map(i -> i.toCsv())
			.forEach(i -> builder.append(escolha.concat(i).concat("\n")));
		
		return builder.toString();
	}
}
